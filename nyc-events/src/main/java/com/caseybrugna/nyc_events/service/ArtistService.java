package com.caseybrugna.nyc_events.service;

import com.caseybrugna.nyc_events.service.ArtistService;

import com.caseybrugna.nyc_events.model.Artist;
import com.caseybrugna.nyc_events.model.Event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.caseybrugna.nyc_events.repository.ArtistRepository;

@Service
public class ArtistService {
    private static final Logger LOGGER = Logger.getLogger(ArtistService.class.getName());

    private final SpotifyAPIClient spotifyApiClient;
    private final ArtistRepository artistRepository;

    @Autowired
    public ArtistService(SpotifyAPIClient spotifyApiClient, ArtistRepository artistRepository) {
        this.spotifyApiClient = spotifyApiClient;
        this.artistRepository = artistRepository;
    }

    public void fillArtist(Artist artist, Event event) {
        String artistName = artist.getArtistName();
        String artistID = spotifyApiClient.getArtistID(artistName);
        artist.addEvent(event);

        if (artistID != null) {
            Map<String, String> artistTrackMap = spotifyApiClient.getTrackMap(artistID);
            int artistPopularityScore = spotifyApiClient.getPopularityScore(artistID);
            String artistUrl = spotifyApiClient.getArtistSpotifyUrl(artistID);
            String[] artistGenres = spotifyApiClient.getArtistGenres(artistID);

            artist.setArtistID(artistID);
            artist.setArtistTrackMap(artistTrackMap);
            artist.setArtistPopularityScore(artistPopularityScore);
            artist.setArtistUrl(artistUrl);
            artist.setArtistGenres(Arrays.asList(artistGenres));
        } else {
            String searchUrl = getArtistSearchUrl(artistName);
            artist.setArtistUrl(searchUrl);
        }

        artistRepository.save(artist);
    }

    private String getArtistSearchUrl(String artistName) {
        String baseUrl = "https://www.google.com/search?q=";
        String encodedSearchText;
        try {
            encodedSearchText = java.net.URLEncoder.encode(artistName, "UTF-8");
        } catch (java.io.UnsupportedEncodingException e) {
            LOGGER.log(Level.SEVERE, "Error encoding artist name: " + artistName, e);
            return null;
        }
        return baseUrl + encodedSearchText;
    }
}
