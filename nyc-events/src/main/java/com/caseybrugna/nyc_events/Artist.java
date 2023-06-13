package com.caseybrugna.nyc_events;

//import java.util.ArrayList;

//import java.util.List;
//import java.util.ArrayList;

public class Artist {
    private String name;
    // private SpotifyAPIClient spotifyApiClient;
    private String artistID;
    private String[] topTrackIDs;
    private String[] topTrackTitles;
    private boolean hasSpotifyProfile;
    private String[] artistGenres;
    private int popularityScore;
    private String externalUrl;

    public Artist(String name, SpotifyAPIClient spotifyApiClient) {
        this.name = name;
        // this.spotifyApiClient = spotifyApiClient;
        try {
            this.artistID = spotifyApiClient.getArtistID(name);
            this.topTrackIDs = spotifyApiClient.getArtistTopTracks(this.artistID);
            this.topTrackTitles = spotifyApiClient.getArtistTopTrackTitles(topTrackIDs);
            this.artistGenres = spotifyApiClient.getArtistGenres(this.artistID);
            this.popularityScore = spotifyApiClient.getPopularityScore(this.artistID);
            this.externalUrl = spotifyApiClient.getExternalUrl(this.artistID);

            this.hasSpotifyProfile = true;
        } catch (Exception e) {
            this.artistID = null;
            this.hasSpotifyProfile = false;
            this.externalUrl = createGoogleSearch(name);
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

    public String[] getArtistGenres() {
        return artistGenres;
    }

    public int getPopularityScore() {
        return popularityScore;
    }

    public String getExternalUrl() {
        return externalUrl;
    }

    private String createGoogleSearch(String searchText) {
        String baseUrl = "https://www.google.com/search?q=";
        String encodedSearchText = java.net.URLEncoder.encode(searchText, java.nio.charset.StandardCharsets.UTF_8);
        return baseUrl + encodedSearchText;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        if (hasSpotifyProfile) {
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
            builder.append("]");
            builder.append(", artistGenres=[");
            for (String genre : artistGenres) {
                builder.append(genre).append(", ");
            }
            if (artistGenres.length > 0) {
                // Remove the trailing comma and space
                builder.delete(builder.length() - 2, builder.length());
            }
            builder.append("]");
            builder.append(", popularityScore=").append(popularityScore);
            builder.append(", externalUrl='").append(externalUrl).append("'");
            builder.append("}");
        } else {
            builder.append("Artist{name='").append(name).append("']");
        }

        return builder.toString();
    }
}
