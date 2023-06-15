package com.caseybrugna.nyc_events;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.sql.Date;

/**
 * Represents an event with its details.
 */
public class Event {
    private String eventID;
    private String eventName;
    private java.sql.Date date;
    private String location;
    private String price;
    private String link;
    private String imageUrl;
    private String artistsString;
    private List<String> lineup;
    private List<Artist> artists;

    /**
     * Constructs an Event object with the specified details.
     *
     * @param eventID       the ID of the event (generated)
     * @param eventName     the name of the event
     * @param date          the date of the event
     * @param location      the location of the event
     * @param price         the price of the event
     * @param link          the link to the event
     * @param imageUrl      the link to the displayed image
     * @param artistsString the list of artists (uncleaned)
     * @param lineup        the list of artists (cleaned)
     * @param artists       the list of artist objects
     */
    public Event(String eventName, String date, String location, String price, String link, String imageUrl,
            String artistsString) {
        this.eventName = eventName;
        this.date = parseDate(date);
        this.location = location;
        this.price = price;
        this.link = link;
        this.imageUrl = imageUrl;
        this.artistsString = artistsString;
        parseArtistsString();
        artists = new ArrayList<>();

        this.eventID = generateEventID();

    }

    public String generateEventID() {
    // Concatenate relevant event details
    String eventDetails = eventName + date.toString() + location;

    // Generate a unique ID using the hashCode of the event details
    int hashCode = eventDetails.hashCode();
    String uniqueID = Integer.toHexString(hashCode);

    uniqueID = uniqueID.replaceAll("[^a-zA-Z0-9]", "");

    return uniqueID;
    }

    /**
     * Returns the ID of the event.
     *
     * @return the event name
     */
    public String getEventID() {
        return eventID;
    }

    /**
     * Returns the name of the event.
     *
     * @return the event name
     */
    public String getEventName() {
        return eventName;
    }

    /**
     * Returns the date of the event.
     *
     * @return the event date
     */
    public java.sql.Date getDate() {
        return date;
    }

    /**
     * Parses the provided date String into a LocalDate object.
     * The input date String should be in the format "E, dd MMM",
     * where "E" represents the day of the week, "dd" represents the
     * day of the month, and "MMM" represents the month of the year.
     * The returned LocalDate object will have the current year.
     *
     * @param date the date String to be parsed
     * @return a LocalDate object representing the parsed date
     */
    private java.sql.Date parseDate(String date) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, MMM d[dd]", Locale.ENGLISH);
            TemporalAccessor temporalAccessor = formatter.parse(date);
            LocalDate localDate = LocalDate.of(LocalDate.now().getYear(),
                    temporalAccessor.get(ChronoField.MONTH_OF_YEAR),
                    temporalAccessor.get(ChronoField.DAY_OF_MONTH));
            return java.sql.Date.valueOf(localDate);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Returns the location of the event.
     *
     * @return the event location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Returns the price of the event.
     *
     * @return the event price
     */
    public String getPrice() {
        return price;
    }

    /**
     * Returns the link to the event.
     *
     * @return the event link
     */
    public String getLink() {
        return link;
    }

    /**
     * Returns the imageUrl to the event.
     *
     * @return the imageUrl
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * Returns the artistsString of the event.
     *
     * @return artistsString the artist string from the link
     */
    public String getArtistsString() {
        return artistsString;
    }

    /**
     * Parses the artistsString and populates the lineup list with the parsed
     * artists. Removes "and X more" to last artist.
     */
    private void parseArtistsString() {
        lineup = new ArrayList<>();

        if (this.artistsString != null && !this.artistsString.isEmpty()) {
            String[] names = this.artistsString.split(",");
            for (int i = 0; i < names.length; i++) {
                String name = names[i].trim();
                if (i == names.length - 1) {
                    // Remove "and X more" from the last artist's name
                    name = name.replaceAll(" and \\d+ more$", "");
                }
                lineup.add(name);
            }
        }
    }

    /**
     * Returns the lineup list of artist objects
     *
     * @return the lineup list of artists
     */
    public List<String> getLineup() {
        return lineup;
    }

    public void addArtist(Artist artist) {
        artists.add(artist);
    }

    /**
     * Returns a string representation of the Event object.
     * Includes the event name, date, location, price, link, imageUrl, and lineup.
     * If the lineup is empty, it will be represented as "NULL".
     *
     * @return a string representation of the Event object.
     */
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, MMM d[dd]");
        String formattedDate = date != null ? formatter.format(date.toLocalDate()) : "N/A";
        return "Event: " + eventName + "\n"
                + "Date: " + formattedDate + "\n"
                + "Location: " + location + "\n"
                + "Price: " + price + "\n"
                + "Link: " + link + "\n"
                + "Image URL: " + imageUrl + "\n";
    }
}
