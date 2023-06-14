package com.caseybrugna.nyc_events;

import java.util.List;

/**
 * The main class that coordinates the scraping of events and artist data.
 */
public class Main {
    /**
     * The main method of the application.
     * It first scrapes event data from the Dice website using the DiceScraper class.
     * Then, it creates a SpotifyAPIClient object and uses it to retrieve artist data for each artist in each event's lineup.
     * If an artist exists on Spotify, an Artist object is created for them and added to the event.
     * If an error occurs during the creation of an Artist object, it is logged and the program continues with the next artist.
     * If an error occurs during the scraping of events, it is logged and the program terminates.
     *
     * @param args The command line arguments. These are not used in this method.
     */
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
