package com.caseybrugna.nyc_events;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            List<Event> events = DiceScraper.scrapeEvents();
            SpotifyAPIClient spotify = new SpotifyAPIClient();

            for (Event event : events) {
                if (event.getLineup() != null) {
                    
                    for (String artistString : event.getLineup()) {
                        try {
                            Artist artist = new Artist(artistString, spotify);
                            event.addArtist(artist);
                            System.out.println(artist);
                            System.out.println();
                        } catch (RuntimeException e) {
                            System.err.println("An error occurred while creating " + artistString + e.getMessage());
                        }
                        
                    }

                }

            }

        } catch (RuntimeException e) {
            System.err.println("An error occurred while scraping the events: " + e.getMessage());
        }
    }
}
