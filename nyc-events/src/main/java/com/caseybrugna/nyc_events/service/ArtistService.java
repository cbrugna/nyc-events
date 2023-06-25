package com.caseybrugna.nyc_events.service;

import com.caseybrugna.nyc_events.model.Artist;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//import com.caseybrugna.nyc_events.model.Event;

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
        this.artists = new ArrayList<>();
    }

    public static void fillArtist(Artist artist) {
        spotifyApiClient = new SpotifyAPIClient();
        String artistName = artist.getArtistName();
        String artistID = spotifyApiClient.getArtistID(artistName);

        if (artistID != null) {
            Map<String, String> artistTrackMap = spotifyApiClient.getTrackMap(artistID);
            int artistPopularityScore = spotifyApiClient.getPopularityScore(artistID);
            String artistUrl = spotifyApiClient.getArtistSpotifyUrl(artistID);
            String[] artistGenres = spotifyApiClient.getArtistGenres(artistID);

            artist.setArtistTrackMap(artistTrackMap);
            artist.setArtistPopularityScore(artistPopularityScore);
            artist.setArtistUrl(artistUrl);
            artist.setArtistGenres(artistGenres);
        } else {
            String searchUrl = getArtistSearchUrl(artistName);
            artist.setArtistUrl(searchUrl);
        }
    }

    private static String getArtistSearchUrl(String artistName) {
        String baseUrl = "https://www.google.com/search?q=";
        String encodedSearchText = "";
        try {
            encodedSearchText = URLEncoder.encode(artistName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOGGER.log(Level.INFO, "Error encoding artist name: " + artistName, e);
            return null;
        }
        return baseUrl + encodedSearchText;
    } 

}
