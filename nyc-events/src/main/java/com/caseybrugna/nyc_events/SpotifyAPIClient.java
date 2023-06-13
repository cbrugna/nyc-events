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

    public int getPopularityScore(String artistID) {
        try {
            // Create a GetArtistRequest with the artist ID
        GetArtistRequest getArtistRequest = spotifyApi.getArtist(artistID).build();

        // Execute the request and retrieve the artist object
        Artist artist = getArtistRequest.execute();

        int popularityScore = artist.getPopularity();
        return popularityScore;

        } catch (Exception e) {
            System.err.println("An error occurred while fetching the popularuty score: " + e.getMessage());
            return -1; // Return an empty array in case of error
        }
        

        
    }

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
            return null; // Return an empty array in case of error
        }
        
    }

}

