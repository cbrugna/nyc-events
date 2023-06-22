package com.caseybrugna.nyc_events.model;

import java.util.Map;
import java.util.HashMap;

/**
 * Represents an artist with their details.
 */
public class Artist {
    private String artistID;
    private String artistName;
    private Map<String, String> trackMap;
    private boolean hasArtistProfile;
    private int popularityScore;
    private String externalUrls;
    private String[] artistGenres;

    /**
     * Constructs an Artist object with the specified details.
     *
     * @param artistID   the ID of the artist
     * @param artistName the name of the artist
     */
    public Artist(String artistName) {
        this.artistID = "";
        this.artistName = artistName;
        this.trackMap = new HashMap<>();
        this.hasArtistProfile = false;
        this.popularityScore = -1;
        this.externalUrls = "";
        this.artistGenres = new String[0];
    }

    public String getArtistID() {
        return artistID;
    }

    public void setArtistID(String artistID) {
        this.artistID = artistID;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public boolean hasArtistProfile() {
        return hasArtistProfile;
    }

    public void setHasArtistProfile(boolean hasArtistProfile) {
        this.hasArtistProfile = hasArtistProfile;
    }

    public int getPopularityScore() {
        return popularityScore;
    }

    public void setPopularityScore(int popularityScore) {
        this.popularityScore = popularityScore;
    }

    public String getExternalUrls() {
        return externalUrls;
    }

    public void setExternalUrls(String externalUrls) {
        this.externalUrls = externalUrls;
    }

    public String[] getArtistGenres() {
        return artistGenres;
    }

    public void setArtistGenres(String[] artistGenres) {
        this.artistGenres = artistGenres;
    }

    @Override
    public String toString() {
        return "Artist ID: " + artistID + "\n"
                + "Artist Name: " + artistName + "\n"
                + "Has Artist Profile: " + hasArtistProfile + "\n"
                + "Popularity Score: " + popularityScore + "\n"
                + "External URLs: " + externalUrls + "\n"
                + "Artist Genres: " + artistGenres.length;
    }
}
