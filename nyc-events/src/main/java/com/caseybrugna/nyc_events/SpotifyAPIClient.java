package com.caseybrugna.nyc_events;

import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.util.Arrays;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.ClientCredentials;
import com.wrapper.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import com.wrapper.spotify.requests.data.search.SearchItemRequest;
import com.wrapper.spotify.requests.data.tracks.GetSeveralTracksRequest;
import com.wrapper.spotify.model_objects.specification.Artist;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.requests.data.artists.GetArtistRequest;
import com.wrapper.spotify.requests.data.artists.GetArtistsTopTracksRequest;

import com.neovisionaries.i18n.CountryCode;

/**
 * A class to interact with the Spotify API and retrieve artist and track data.
 */
public class SpotifyAPIClient {
    private static final String CLIENT_ID;
    private static final String CLIENT_SECRET;
    private final SpotifyApi spotifyApi;

    static {
        Dotenv dotenv = Dotenv.configure()
                .directory("src/main/resources")
                .load();
        CLIENT_ID = dotenv.get("CLIENT_ID");
        CLIENT_SECRET = dotenv.get("CLIENT_SECRET");
    }

    /**
     * Constructor for the SpotifyAPIClient class.
     * Initializes the SpotifyApi instance with client ID and client secret from the .env file.
     * Retrieves a client token for authorization and sets it on the SpotifyApi object.
     *
     * @throws RuntimeException If there's an error while retrieving client credentials.
     */
    public SpotifyAPIClient() {
        spotifyApi = new SpotifyApi.Builder()
                .setClientId(CLIENT_ID)
                .setClientSecret(CLIENT_SECRET)
                .build();

        // Retrieve a client token for authorization
        ClientCredentialsRequest clientCredentialsRequest = spotifyApi.clientCredentials().build();
        try {
            ClientCredentials clientCredentials = clientCredentialsRequest.execute();

            // Set the access token on the SpotifyApi object
            spotifyApi.setAccessToken(clientCredentials.getAccessToken());

        } catch (Exception e) {
            System.out.println("An error occurred while retrieving client credentials: " + e.getMessage());
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
        try {
            // Create a SearchItemRequest to search for the artist
            SearchItemRequest searchRequest = spotifyApi.searchItem(artistName, "artist").build();

            // Execute the search request and retrieve the search results
            Paging<Artist> artistSearchResults = searchRequest.execute().getArtists();

            // Iterate over the search results to find the matching artist
            for (Artist artist : artistSearchResults.getItems()) {
                // Check if the artist name matches exactly
                if (artist.getName().equalsIgnoreCase(artistName)) {
                    // Return the artist ID
                    return artist.getId();
                }
            }
        } catch (Exception e) {
            System.out.println("An error occurred while searching for the artist: " + e.getMessage());
        }

        // If no matching artist is found, return null
        return null;
    }

    /**
     * Retrieves the top tracks for a given artist from the Spotify API.
     *
     * @param artistID The ID of the artistto retrieve top tracks for.
     * @return An array of track IDs for the artist's top tracks.
     */
    public String[] getArtistTopTracks(String artistID) {
        try {
            GetArtistsTopTracksRequest request = spotifyApi.getArtistsTopTracks(artistID, CountryCode.US).build();
            Track[] tracks = request.execute();

            String[] trackIDs = new String[tracks.length];
            for (int i = 0; i < tracks.length; i++) {
                trackIDs[i] = tracks[i].getId();
            }

            return trackIDs;

        } catch (IOException | SpotifyWebApiException | org.apache.hc.core5.http.ParseException e) {
            System.err.println("An error occurred while fetching the artist's top tracks: " + e.getMessage());
            return new String[0]; // Return an empty array in case of error
        }
    }

    /**
     * Retrieves the titles of a given set of tracks from the Spotify API.
     *
     * @param trackIDs An array of track IDs to retrieve titles for.
     * @return An array of track titles corresponding to the provided track IDs.
     */
    public String[] getArtistTopTrackTitles(String[] trackIDs) {
        try {
            GetSeveralTracksRequest request = spotifyApi.getSeveralTracks(trackIDs).build();
            Track[] tracks = request.execute();

            String[] trackTitles = new String[tracks.length];
            for (int i = 0; i < tracks.length; i++) {
                trackTitles[i] = tracks[i].getName();
            }

            return trackTitles;
        } catch (IOException | SpotifyWebApiException | org.apache.hc.core5.http.ParseException e) {
            System.err.println("An error occurred while fetching track titles: " + e.getMessage());
            return new String[0]; // Return an empty array in case of error
        }
    }

    /**
     * Retrieves the top three genres associated with a given artist from the Spotify API.
     *
     * @param artistID The ID of the artist to retrieve genres for.
     * @return An array of the top three genres associated with the artist.
     */
    public String[] getArtistGenres(String artistID) {
        try {
            // Create a GetArtistRequest with the artist ID
            GetArtistRequest getArtistRequest = spotifyApi.getArtist(artistID).build();

            // Execute the request and retrieve the artist object
            Artist artist = getArtistRequest.execute();

            // Get the genres associated with the artist
            String[] genres = artist.getGenres();

            // Return the top three genres
            if (genres.length > 3) {
                return Arrays.copyOfRange(genres, 0, 3);
            } else {
                return genres;
            }
        } catch (Exception e) {
            System.err.println("An error occurred while fetching the artist's genres: " + e.getMessage());
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
        try {
            // Create a GetArtistRequest with the artist ID
        GetArtistRequest getArtistRequest = spotifyApi.getArtist(artistID).build();

        // Execute the request and retrieve the artist object
        Artist artist = getArtistRequest.execute();

        int popularityScore = artist.getPopularity();
        return popularityScore;

        } catch (Exception e) {
            System.err.println("An error occurred while fetching the popularity score: " + e.getMessage());
            return -1; // Return -1 in case of error
        }
    }

    /**
     * Retrieves the Spotify link for a given artist from the Spotify API.
     *
     * @param artistID The ID of the artist to retrieve the Spotify link for.
     * @return The artist's Spotify link, or null if an error occurred.
     */
    public String getExternalUrl(String artistID) {
        try {
            // Create a GetArtistRequest with the artist ID
            GetArtistRequest getArtistRequest = spotifyApi.getArtist(artistID).build();

            // Execute the request and retrieve the artist object
            Artist artist = getArtistRequest.execute();

            String spotifyLink = artist.getExternalUrls().get("spotify");

            return spotifyLink;

        } catch (Exception e) {
            System.err.println("An error occurred while fetching the artist's external URLs: " + e.getMessage());
            return null; // Return null in case of error
        }
    }

}
