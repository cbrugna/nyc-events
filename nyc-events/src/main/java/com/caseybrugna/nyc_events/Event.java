package com.caseybrugna.nyc_events;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an event with its details.
 */
public class Event {
    private String eventName;
    private String date;
    private String location;
    private String price;
    private String link;
    private String imageUrl;
    private String artistsString;
    private List<String> lineup;

    /**
     * Constructs an Event object with the specified details.
     *
     * @param eventName     the name of the event
     * @param date          the date of the event
     * @param location      the location of the event
     * @param price         the price of the event
     * @param link          the link to the event
     * @param imageUrl      the link to the displayed image
     * @param artistsString the list of artists (uncleaned)
     */
    public Event(String eventName, String date, String location, String price, String link, String imageUrl,
            String artistsString) {
        this.eventName = eventName;
        this.date = date;
        this.location = location;
        this.price = price;
        this.link = link;
        this.imageUrl = imageUrl;
        this.artistsString = artistsString;
        parseArtistsString();
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
    public String getDate() {
        return date;
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

        if (artistsString != null && !artistsString.isEmpty()) {
            String[] artists = artistsString.split(",");
            for (int i = 0; i < artists.length; i++) {
                String artist = artists[i].trim();
                if (i == artists.length - 1) {
                    // Remove "and X more" from the last artist's name
                    artist = artist.replaceAll(" and \\d+ more$", "");
                }
                lineup.add(artist);
            }
        }
    }

    /**
     * Returns the lineup list containing the parsed artists.
     *
     * @return the lineup list of artists
     */
    public List<String> getLineup() {
        return lineup;
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
        StringBuilder sb = new StringBuilder();
        sb.append("Event{");
        sb.append("eventName='").append(eventName).append('\'');
        sb.append(", date='").append(date).append('\'');
        sb.append(", location='").append(location).append('\'');
        sb.append(", price='").append(price).append('\'');
        sb.append(", link='").append(link).append('\'');
        sb.append(", imageUrl='").append(imageUrl).append('\'');

        if (lineup.isEmpty()) {
            sb.append(", lineup=").append("n/a");
        } else {
            sb.append(", lineup=").append(lineup);
        }

        sb.append('}');
        return sb.toString();
    }
}
