package com.caseybrugna.nyc_events;

import java.util.List;

import org.springframework.boot.autoconfigure.SpringBootApplication;

//import com.caseybrugna.nyc_events.dao.DatabaseDAO;
import com.caseybrugna.nyc_events.model.Artist;
import com.caseybrugna.nyc_events.model.Event;
import com.caseybrugna.nyc_events.service.ArtistService;
import com.caseybrugna.nyc_events.service.DiceScraper;
import com.caseybrugna.nyc_events.service.EventService;
import com.caseybrugna.nyc_events.service.SpotifyAPIClient;

import java.util.ArrayList;

import io.github.cdimascio.dotenv.Dotenv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The main class that coordinates the scraping of events and artist data.
 */
@SpringBootApplication  
public class Main { 
    public static void main(String[] args) {
        List<Event> events = DiceScraper.scrapeEvents();
        EventService eventService = new EventService(events);
        eventService.fillEvents();

        for (Event event : events) {
            EventService.fillEventLineup(event.getArtistsString());
        }
        


        for (Event event : events) {
            System.out.println(event);
            System.out.println("***************");
            for (Artist artist : event.getLineup()) {
                System.out.println(artist);
                System.out.println("***************");
            }
            System.out.println("***************");
        }
    }

}
