package com.caseybrugna.nyc_events;

/**
 * A class representing an Artist with their details fetched from the Spotify API.
 */
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

    /**
     * Constructor for the Artist class.
     * Initializes an Artist object with the given name and fetches their details from the Spotify API.
     *
     * @param name The name of the artist.
     * @param spotifyApiClient The SpotifyAPIClient object to use for fetching artist details.
     */
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

    /**
     * Returns the name of the artist.
     *
     * @return The name of the artist.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the Spotify ID of the artist.
     *
     * @return The Spotify ID of the artist.
     */
    public String getArtistID() {
        return artistID;
    }

    /**
     * Returns the IDs of the artist's top tracks on Spotify.
     *
     * @return An array of the artist's top track IDs.
     */
    public String[] getTopTrackIDs() {
        return topTrackIDs;
    }

    /**
     * Returns the titles of the artist's top tracks on Spotify.
     *
     * @return An array of the artist's top track titles.
     */
    public String[] getTopTrackTitles() {
        return topTrackTitles;
    }

    /**
     * Returns the genres associated with the artist on Spotify.
     *
     * @return An array of the artist's genres.
     */
    public String[] getArtistGenres() {
        return artistGenres;
    }

    /**
     * Returns the popularity score of the artist on Spotify.
     *
     * @return The artist's popularity score.
     */
    public int getPopularityScore() {
        return popularityScore;
    }

    /**
     * Returns the external URL of the artist's Spotify profile.
     *
     * @return The artist's Spotify profile URL.
     */
    public String getExternalUrl() {
        return externalUrl;
    }

    /**
     * Creates a Google search URL with the provided search text.
     *
     * @param searchText The text to use for the Google search.
     * @return The created Google search URL.
     */
    private String createGoogleSearch(String searchText) {
        String baseUrl = "https://www.google.com/search?q=";
        String encodedSearchText = java.net.URLEncoder.encode(searchText, java.nio.charset.StandardCharsets.UTF_8);
        return baseUrl + encodedSearchText;
    }

    /**
     * Returns a stringrepresentation of the Artist object, including all of its details.

     * @return A string representation of the Artist object.
     */
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
            builder.append("Artist{name='").append(name).append("'}");
        }

        return builder.toString();
    }
}
