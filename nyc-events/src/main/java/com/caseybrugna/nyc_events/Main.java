package com.caseybrugna.nyc_events;

import java.util.List;

import javax.persistence.Entity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.caseybrugna.nyc_events.model.Artist;
import com.caseybrugna.nyc_events.model.Event;
import com.caseybrugna.nyc_events.repository.ArtistRepository;
import com.caseybrugna.nyc_events.repository.EventRepository;
import com.caseybrugna.nyc_events.service.DiceScraper;
import com.caseybrugna.nyc_events.service.EventService;
import com.caseybrugna.nyc_events.service.SpotifyAPIClient;
import com.caseybrugna.nyc_events.service.ArtistService;
import io.github.cdimascio.dotenv.Dotenv;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * The main class that coordinates the scraping of events and artist data.
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@ComponentScan(basePackages = {"com.caseybrugna.nyc_events", "com.caseybrugna.nyc_events.config"})
//@EnableJpaRepositories
@EntityScan("com.caseybrugna.nyc_events.model")
public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure()
            .directory("src/main/resources")
            .load();

        
        log.info("This is an INFO");
        log.warn("This is a WARN");

        ApplicationContext context = SpringApplication.run(Main.class, args);

        EventRepository eventRepository = context.getBean(EventRepository.class);
        ArtistRepository artistRepository = context.getBean(ArtistRepository.class);

        DiceScraper diceScraper = context.getBean(DiceScraper.class);
        List<Event> events = diceScraper.scrapeEvents();
        SpotifyAPIClient spotifyApiClient = context.getBean(SpotifyAPIClient.class);
        ArtistService artistService = new ArtistService(spotifyApiClient, artistRepository);
        EventService eventService = new EventService(events, artistService, eventRepository);
        eventService.fillEvents();

        for (Event event : events) {
            eventService.fillEventLineup(event.getArtistsString(), event);
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

