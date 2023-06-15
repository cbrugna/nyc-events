package com.caseybrugna.nyc_events;

import java.util.List;

import org.openqa.selenium.devtools.v112.database.Database;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvBuilder;

/**
 * The main class that coordinates the scraping of events and artist data.
 */
public class Main {
    /**
     * The main method of the application.
     * It first scrapes event data from the Dice website using the DiceScraper
     * class.
     * Then, it creates a SpotifyAPIClient object and uses it to retrieve artist
     * data for each artist in each event's lineup.
     * If an artist exists on Spotify, an Artist object is created for them and
     * added to the event.
     * If an error occurs during the creation of an Artist object, it is logged and
     * the program continues with the next artist.
     * If an error occurs during the scraping of events, it is logged and the
     * program terminates.
     *
     * @param args The command line arguments. These are not used in this method.
     */
    public static void main(String[] args) {
        List<Event> events = DiceScraper.scrapeEvents();
        try {

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
            System.err.println("An error occurred while scraping the events in the main: " + e.getMessage());
        }

        String url = "jdbc:mysql://localhost:3306/events_db?serverTimezone=UTC";

        Dotenv dotenv = Dotenv.configure()
                .directory("src/main/resources")
                .load();
        String user = dotenv.get("DB_USER");
        String password = dotenv.get("DB_PASSWORD");

        DatabaseDAO databaseDAO = new DatabaseDAO(url, user, password);
        databaseDAO.connect();

        // Perform database operations using the databaseDAO object
        databaseDAO.insertEvents(events);

        databaseDAO.disconnect();
    }

}
