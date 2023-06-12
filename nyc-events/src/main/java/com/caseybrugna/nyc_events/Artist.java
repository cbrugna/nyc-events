package com.caseybrugna.nyc_events;

//import java.util.List;
//import java.util.ArrayList;

public class Artist {
    private String name;
    // private SpotifyAPIClient spotifyApiClient;
    private String artistID;
    private String[] topTrackIDs;
    private String[] topTrackTitles;
    private boolean hasSpotifyProfile;

    public Artist(String name, SpotifyAPIClient spotifyApiClient) {
        this.name = name;
        // this.spotifyApiClient = spotifyApiClient;
        try {
            this.artistID = spotifyApiClient.getArtistID(name);
            this.topTrackIDs = spotifyApiClient.getArtistTopTracks(this.artistID);
            this.topTrackTitles = spotifyApiClient.getArtistTopTrackTitles(topTrackIDs);
            this.hasSpotifyProfile = true;
        } catch (Exception e) {
            this.artistID = null;
            this.hasSpotifyProfile = false;
        }
    }

    public String getName() {
        return name;
    }

    public String getArtistID() {
        return artistID;
    }

    public String[] getTopTrackIDs() {
        return topTrackIDs;
    }

    public String[] getTopTrackTitles() {
        return topTrackTitles;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        if (hasSpotifyProfile == true) {
            builder.append("Artist{name='").append(name).append("'");
            builder.append(", artistID='").append(artistID).append("'");
            builder.append(", topTrackIDs=[");
            for (String trackID : topTrackIDs) {
                builder.append(trackID).append(", ");
            }
            if (topTrackIDs.length > 0) {
                // Remove the trailing comma and space
                builder.delete(builder.length() - 2, builder.length());
            }
            builder.append("]}");
        } else {
            builder.append("Artist{name='").append(name).append("]}");
        }

        return builder.toString();
    }
}

/**
 * public class Artist {
 * private String name;
 * private String artistID;
 * private List<String> topTrackIDs;
 * 
 * public Artist(String name, SpotifyAPIClient spotifyApiClient) {
 * this.name = name;
 * this.artistID = spotifyApiClient.getArtistID(name);
 * this.topTrackIDs = fetchTopTrackIDs();
 * }
 * 
 * private List<String> fetchTopTrackIDs() {
 * List<String> topTrackIDs = new ArrayList<>();
 * 
 * 
 * }
 * 
 * 
 * public String getName() {
 * return name;
 * }
 * 
 * public String getArtistID() {
 * return artistID;
 * }
 * 
 * @Override
 *           public String toString() {
 *           return "Artist{" +
 *           "name='" + name + '\'' +
 *           "artistID=" + artistID +
 *           '}';
 *           }
 *           }
 */
