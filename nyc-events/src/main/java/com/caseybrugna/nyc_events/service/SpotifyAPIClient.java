package com.caseybrugna.nyc_events.service;

import io.github.cdimascio.dotenv.Dotenv;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.ClientCredentials;
import com.wrapper.spotify.model_objects.specification.Artist;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import com.wrapper.spotify.requests.data.artists.GetArtistRequest;
import com.wrapper.spotify.requests.data.artists.GetArtistsTopTracksRequest;
import com.wrapper.spotify.requests.data.search.simplified.SearchArtistsRequest;
import com.wrapper.spotify.requests.data.tracks.GetSeveralTracksRequest;

import com.neovisionaries.i18n.CountryCode;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

/**
 * A class to interact with the Spotify API and retrieve artist and track data.
 */
public class SpotifyAPIClient {
    private static final String CLIENT_ID;
    private static final String CLIENT_SECRET;
    private final SpotifyApi spotifyApi;
    private static final Logger LOGGER = Logger.getLogger(SpotifyAPIClient.class.getName());

    static {
        Dotenv dotenv = Dotenv.configure()
                .directory("src/main/resources")
                .load();
        CLIENT_ID = dotenv.get("CLIENT_ID");
        CLIENT_SECRET = dotenv.get("CLIENT_SECRET");
    }

    /**
     * Constructor for the SpotifyAPIClient class.
     * Initializes the SpotifyApi instance with client ID and client secret from the
     * .env file.
     * Retrieves a client token for authorization and sets it on the SpotifyApi
     * object.
     *
     * @throws RuntimeException If there's an error while retrieving client
     *                          credentials.
     */
    public SpotifyAPIClient() {
        spotifyApi = new SpotifyApi.Builder()
                .setClientId(CLIENT_ID)
                .setClientSecret(CLIENT_SECRET)
                .build();
        setClientCredentials();
    }

    /**
     * Method to set client credentials.
     */
    private void setClientCredentials() {
        ClientCredentialsRequest clientCredentialsRequest = spotifyApi.clientCredentials().build();
        try {
            ClientCredentials clientCredentials = clientCredentialsRequest.execute();
            spotifyApi.setAccessToken(clientCredentials.getAccessToken());
        } catch (IOException | SpotifyWebApiException | org.apache.hc.core5.http.ParseException e) {
            LOGGER.log(Level.SEVERE, "Error occurred while setting client credentials: " + e.getMessage(), e);
        }
    }

    /**
     * Searches for an artist in the Spotify API using the provided artist name.
     * Returns the ID of the first artist found with a matching name.
     *
     * @param artistName The name of the artist to search for.
     * @return The ID of the artist, or null if no artist was found.
     */
    public String getArtistID(String artistName) {
        SearchArtistsRequest searchRequest = spotifyApi.searchArtists(artistName).build();
        try {
            Paging<Artist> artistSearchResults = searchRequest.execute();
            return Arrays.stream(artistSearchResults.getItems())
                    .filter(artist -> artist.getName().equalsIgnoreCase(artistName))
                    .map(Artist::getId)
                    .findFirst()
                    .orElse(null);
        } catch (IOException | SpotifyWebApiException | org.apache.hc.core5.http.ParseException e) {
            LOGGER.log(Level.SEVERE, "Error occurred while searching for the artist: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Retrieves the top tracks for a given artist from the Spotify API.
     *
     * @param artistID The ID of the artist to retrieve top tracks for.
     * @return An array of track IDs for the artist's top tracks.
     */
    public String[] getArtistTopTracks(String artistID) {
        GetArtistsTopTracksRequest request = spotifyApi.getArtistsTopTracks(artistID, CountryCode.US).build();
        try {
            Track[] tracks = request.execute();
            return Arrays.stream(tracks).map(Track::getId).toArray(String[]::new);
        } catch (IOException | SpotifyWebApiException | org.apache.hc.core5.http.ParseException e) {
            LOGGER.log(Level.SEVERE, "Error occurred while fetching the artist's top tracks: " + e.getMessage(), e);
            return new String[0]; // Return an empty array in case of error
        }
    }

    /**
     * Retrieves the titles of a given set of tracks from the Spotify API.
     *
     * @param trackIDs An array of track IDs to retrieve titles for.
     * @return An array of track titles corresponding to the provided track IDs.
     */
    public String[] getTrackTitles(String[] trackIDs) {
        GetSeveralTracksRequest request = spotifyApi.getSeveralTracks(trackIDs).build();
        try {
            Track[] tracks = request.execute();
            return Arrays.stream(tracks).map(Track::getName).toArray(String[]::new);
        } catch (IOException | SpotifyWebApiException | org.apache.hc.core5.http.ParseException e) {
            LOGGER.log(Level.SEVERE, "Error occurred while fetching track titles: " + e.getMessage(), e);
            return new String[0]; // Return an empty array in case of error
        }
    }

    /**
     * Retrieves the top three genres associated with a given artist from the
     * Spotify API.
     *
     * @param artistID The ID of the artist to retrieve genres for.
     * @return An array of the top three genres associated with the artist.
     */
    public String[] getArtistGenres(String artistID) {
        GetArtistRequest getArtistRequest = spotifyApi.getArtist(artistID).build();
        try {
            Artist artist = getArtistRequest.execute();
            String[] genres = artist.getGenres();
            return Arrays.copyOfRange(genres, 0, Math.min(genres.length, 3));
        } catch (IOException | SpotifyWebApiException | org.apache.hc.core5.http.ParseException e) {
            LOGGER.log(Level.SEVERE, "Error occurred while fetching the artist's genres: " + e.getMessage(), e);
            return new String[0]; // Return an empty array in case of error
        }
    }

    /**
     * Retrieves the popularity score of a given artist from the Spotify API.
     *
     * @param artistID The ID of the artist to retrieve the popularity score for.
     * @return The artist's popularity score, or -1 if an error occurred.
     */
    public int getPopularityScore(String artistID) {
        GetArtistRequest getArtistRequest = spotifyApi.getArtist(artistID).build();
        try {
            Artist artist = getArtistRequest.execute();
            return artist.getPopularity();
        } catch (IOException | SpotifyWebApiException | org.apache.hc.core5.http.ParseException e) {
            LOGGER.log(Level.SEVERE, "Error occurred while fetching the popularity score: " + e.getMessage(), e);
            return -1; // Return -1 in case of error
        }
    }

    /**
     * Retrieves the Spotify link for a given artist from the Spotify API.
     *
     * @param artistID The ID of the artist to retrieve the Spotify link for.
     * @return The artist's Spotify link, or null if an error occurred.
     */
    public String getArtistSpotifyUrl(String artistID) {
        GetArtistRequest getArtistRequest = spotifyApi.getArtist(artistID).build();
        try {
            Artist artist = getArtistRequest.execute();
            return artist.getExternalUrls().get("spotify");
        } catch (IOException | SpotifyWebApiException | org.apache.hc.core5.http.ParseException e) {
            LOGGER.log(Level.SEVERE, "Error occurred while fetching the artist's external URLs: " + e.getMessage(), e);
            return null; // Return null in case of error
        }
    }

    public Map<String, String> getTrackMap(String artistID) {

        Map<String, String> trackMap = new HashMap<>();
        String[] topTrackIDs = getArtistTopTracks(artistID);
        String[] topTrackTitles = getTrackTitles(topTrackIDs);

        int amtOfTracks = Math.min(5, Math.min(topTrackIDs.length, topTrackTitles.length));
        for (int i = 0; i < amtOfTracks; i++) {
            trackMap.put(topTrackTitles[i], topTrackIDs[i]);
        }

        return trackMap;

    }
}
