package com.caseybrugna.nyc_events.service;

import com.caseybrugna.nyc_events.model.Artist;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.caseybrugna.nyc_events.model.Event;

/**
 * Service class for managing artist data.
 */
public class ArtistService {
    private static final Logger LOGGER = Logger.getLogger(ArtistService.class.getName());

    private static SpotifyAPIClient spotifyApiClient;
    private List<Artist> artists = new ArrayList<>();

    /**
     * Constructs an ArtistService object.
     */
    public ArtistService() {
        spotifyApiClient = new SpotifyAPIClient();
        this.artists = new ArrayList<>();
    }

    // To be filled:
    // Everything but artistName
    //private String artistID;
    //private Map<String, String> trackMap;
    //private boolean hasArtistProfile;
    //private int popularityScore;
    //private String externalUrls;
    //private String[] artistGenres;

    public static Artist fillArtist(Artist artist) {
        String artistName = artist.getArtistName();


        String artistID = spotifyApiClient.getArtistID(artistName);
        // fill track map
        
        

    }







    
}
