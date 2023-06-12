package com.caseybrugna.nyc_events;

import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.text.ParseException;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.ClientCredentials;
import com.wrapper.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;

import com.wrapper.spotify.requests.data.search.SearchItemRequest;
import com.wrapper.spotify.model_objects.specification.Artist;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.Track;
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

}

/*
 * // ***** TEST CODE GOES HERE *** //
 * 
 * 
 * 
 * // Example API request: Search for an artist
 * String artistName = "John Summit";
 * SearchItemRequest searchItemRequest = spotifyApi.searchItem(artistName,
 * "artist").build();
 * 
 * // Execute the search request and get the paging object
 * Paging<Artist> artistPaging = searchItemRequest.execute().getArtists();
 * 
 * // Get the array of artists from the paging object
 * com.wrapper.spotify.model_objects.specification.Artist[] artists =
 * artistPaging.getItems();
 * System.out.println("artistPaging Length: " + artists.length);
 * 
 * if (artists.length > 0) {
 * Artist artist = artists[0];
 * 
 * // Print the artist's name and ID
 * System.out.println("Artist: " + artist.getName());
 * System.out.println("ID: " + artist.getId());
 * } else {
 * System.out.println("Artist not found");
 * }
 * 
 * 
 * 
 * 
 * // *** END TEST CODE *** //
 */