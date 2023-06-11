package com.caseybrugna.nyc_events;

import io.github.cdimascio.dotenv.Dotenv;
//import io.github.cdimascio.dotenv.DotenvBuilder;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import com.wrapper.spotify.model_objects.credentials.ClientCredentials;
import com.wrapper.spotify.requests.data.tracks.GetTrackRequest;
import com.wrapper.spotify.model_objects.specification.Track;

public class SpotifyAPIClient {
    private static final String CLIENT_ID;
    private static final String CLIENT_SECRET;

    static {
        Dotenv dotenv = Dotenv.configure().load();
        CLIENT_ID = dotenv.get("CLIENT_ID");
        CLIENT_SECRET = dotenv.get("CLIENT_SECRET");
    }

    public static void main(String[] args) {
        // Create the SpotifyApi object and set the client credentials
        SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setClientId(CLIENT_ID)
                .setClientSecret(CLIENT_SECRET)
                .build();

        // Retrieve a client token for authorization
        ClientCredentialsRequest clientCredentialsRequest = spotifyApi.clientCredentials().build();
        try {
            ClientCredentials clientCredentials = clientCredentialsRequest.execute();

            // Set the access token on the SpotifyApi object
            spotifyApi.setAccessToken(clientCredentials.getAccessToken());

            // Example API request: Get information about a track
            String trackId = "11dFghVXANMlKmJXsNCbNl"; 
            GetTrackRequest getTrackRequest = spotifyApi.getTrack(trackId).build();
            Track track = getTrackRequest.execute();

            // Print the track name and artist
            System.out.println("Track: " + track.getName());
            System.out.println("Artist: " + track.getArtists()[0].getName());

        } catch (Exception e) {
            System.out.println("An error occurred while retrieving client credentials: " + e.getMessage());
        }
    }
}
