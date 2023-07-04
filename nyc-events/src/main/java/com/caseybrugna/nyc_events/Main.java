package com.caseybrugna.nyc_events;

import java.util.List;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.caseybrugna.nyc_events.model.Artist;
import com.caseybrugna.nyc_events.model.Event;
import com.caseybrugna.nyc_events.service.DiceScraper;
import com.caseybrugna.nyc_events.service.EventService;
import com.caseybrugna.nyc_events.service.SpotifyAPIClient;
import com.caseybrugna.nyc_events.service.ArtistService;
import io.github.cdimascio.dotenv.Dotenv;

/**
 * The main class that coordinates the scraping of events and artist data.
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@ComponentScan(basePackages = {"com.caseybrugna.nyc_events", "com.caseybrugna.nyc_events.config"})
public class Main {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure()
            .directory("src/main/resources")
            .load();

        ApplicationContext context = SpringApplication.run(Main.class, args);

        DiceScraper diceScraper = context.getBean(DiceScraper.class);
        EventService eventService = context.getBean(EventService.class);

        List<Event> events = diceScraper.scrapeEvents();
        eventService.fillEvents();

        for (Event event : events) {
            eventService.fillEventLineup(event.getArtistsString());
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

