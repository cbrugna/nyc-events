package com.caseybrugna.nyc_events;

import java.util.List;
import java.util.ArrayList;

public class Artist {
    private String name;
    private SpotifyAPIClient spotifyApiClient;
    private String artistID;
    private List<String> topTrackIDs;

    public Artist(String name, SpotifyAPIClient spotifyApiClient) {
        this.name = name;
        this.spotifyApiClient = spotifyApiClient;
        this.artistID = spotifyApiClient.getArtistID(name);
        this.topTrackIDs = fetchTopTrackIDs();
    }

    /** 
    private List<String> fetchTopTrackIDs() {
        List<String> topTrackIDs = new ArrayList<>();

        try {
            
            

        }
    }
    */


    public String getName() {
        return name;
    }

    public String getArtistID() {
        return artistID;
    }

    @Override
    public String toString() {
        return "Artist{" +
                "name='" + name + '\'' +
                "artistID=" + artistID + 
                '}';
    }
}

public class Artist {
    private String name;
    private String artistID;
    private List<String> topTrackIDs;

    public Artist(String name, SpotifyAPIClient spotifyApiClient) {
        this.name = name;
        this.artistID = spotifyApiClient.getArtistID(name);
        this.topTrackIDs = fetchTopTrackIDs();
    }

    private List<String> fetchTopTrackIDs() {
        List<String> topTrackIDs = new ArrayList<>();


    }


    public String getName() {
        return name;
    }

    public String getArtistID() {
        return artistID;
    }

    @Override
    public String toString() {
        return "Artist{" +
                "name='" + name + '\'' +
                "artistID=" + artistID + 
                '}';
    }
}
