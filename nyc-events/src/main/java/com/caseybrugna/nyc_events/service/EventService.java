package com.caseybrugna.nyc_events.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.caseybrugna.nyc_events.model.Event;
import com.caseybrugna.nyc_events.model.Artist;

public class EventService {
    private static final Logger LOGGER = Logger.getLogger(ArtistService.class.getName());

    private List<Event> events = new ArrayList<>();

    public EventService(List<Event> events) {
        this.events = events;
        // fill events
    }

    // To be filled:
    // private java.sql.Date date;
    // private List<Artist> lineup;
    // private String eventID;

    public void fillEvents() {
        for (Event event : events) {
            event.setDate(formatDate(event.getDateString()));
            event.setLineup(fillEventLinup(event.getArtistsString()));
            event.setEventID(generateEventID(event.getEventName(), event.getDate(), event.getLocation()));

            // Loop through each event
                // event.fillLineup
        }
    }

    public java.sql.Date formatDate(String dateString) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, MMM d[dd]", Locale.ENGLISH);
            TemporalAccessor temporalAccessor = formatter.parse(dateString);
            LocalDate localDate = LocalDate.of(LocalDate.now().getYear(),
                    temporalAccessor.get(ChronoField.MONTH_OF_YEAR),
                    temporalAccessor.get(ChronoField.DAY_OF_MONTH));
            return java.sql.Date.valueOf(localDate);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error occurred while parsing date: " + dateString, e);
            return null;
        }
    }

    public String generateEventID(String eventName, java.sql.Date date, String location) {
        String eventDetails = eventName + date.toString() + location;
        return Integer.toHexString(eventDetails.hashCode()).replaceAll("[^a-zA-Z0-9]", "");
    }

    public List<Artist> fillEventLinup(String artistsString) {
        if (artistsString == null || artistsString.isEmpty()) {
            return null;
        }
        

        List<Artist> lineup = new ArrayList<>();
        String[] artistNames = artistsString.split(",");

        for (int i = 0; i < artistNames.length; i++) {
            String artistName = artistNames[i].trim();

            if (i == artistNames.length - 1) {
                // Removing "and X more" from the last artist
                artistName = artistName.replaceAll(" and \\d+ more$", "");
            }

            Artist newArtist = new Artist(artistName);
            newArtist = ArtistService.fillArtist(newArtist);
            lineup.add(newArtist);

        }

        return lineup;
    }



}
