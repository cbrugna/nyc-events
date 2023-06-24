package com.caseybrugna.nyc_events.model;

import java.util.Map;
import java.util.HashMap;

/**
 * Represents an artist with their details.
 */
public class Artist {
    private String artistID;
    private String artistName;
    private Map<String, String> artistTrackMap;
    private int artistPopularityScore;
    private String artistUrl;
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
        this.artistTrackMap = new HashMap<>();
        this.artistPopularityScore = -1;
        this.artistUrl = "";
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

    public Map<String, String> getArtistTrackMap() {
        return artistTrackMap;
    }

    public void setArtistTrackMap(Map<String, String> artistTrackMap) {
        this.artistTrackMap = artistTrackMap;
    }

    public int getArtistPopularityScore() {
        return artistPopularityScore;
    }

    public void setArtistPopularityScore(int artistPopularityScore) {
        this.artistPopularityScore = artistPopularityScore;
    }

    public String getArtistUrl() {
        return artistUrl;
    }

    public void setArtistUrl(String artistUrl) {
        this.artistUrl = artistUrl;
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
                + "Popularity Score: " + artistPopularityScore + "\n"
                + "External URLs: " + artistUrl + "\n"
                + "Artist Genres: " + artistGenres.length;
    }
}
