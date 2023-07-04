package com.caseybrugna.nyc_events.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

/**
 * Represents an event with its details.
 */

@Entity
public class Event {
    @Id
    @Column(name = "event_id")
    private String eventID;

    private String eventName;
    private String dateString;
    private String location;
    private String price;
    private String link;
    private String imageUrl;
    private String artistsString;

    private java.sql.Date date;
    
    @ManyToMany
    @JoinTable(name = "event_artist",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "artist_id"))
    private List<Artist> lineup;
    

    /**
     * Constructs an Event object with the specified details.
     *
     * @param eventName     the name of the event -- AS IS FROM DICE.FM
     * @param date          the date of the event -- AS IS FROM DICE.FM
     * @param location      the location of the event -- AS IS FROM DICE.FM
     * @param price         the price of the event -- AS IS FROM DICE.FM
     * @param link          the link to the event -- AS IS FROM DICE.FM
     * @param imageUrl      the link to the displayed image -- AS IS FROM DICE.FM
     * @param artistsString the list of artists (uncleaned) -- AS IS FROM DICE.FM
     */
    public Event(String eventName, String dateString, String location, String price, String link, String imageUrl, String artistsString) {
        this.eventName = eventName;
        this.dateString = dateString;
        this.location = location;
        this.price = price;
        this.link = link;
        this.imageUrl = imageUrl;
        this.artistsString = artistsString;

        // To be filled by EventService
        this.date = null;
        this.lineup = new ArrayList<>();
        this.eventID = "";
    }

    // Getter methods for each field
    public String getEventID() {
        return eventID;
    }

    public String getEventName() {
        return eventName;
    }

    public String getDateString() {
        return dateString;
    }

    public String getLocation() {
        return location;
    }

    public String getPrice() {
        return price;
    }

    public String getLink() {
        return link;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getArtistsString() {
        return artistsString;
    }

    public java.sql.Date getDate() {
        return date;
    }

    public List<Artist> getLineup() {
        return lineup;
    }

    // Setter methods for the necessary fields
    public void setDate(java.sql.Date date) {
        this.date = date;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public void setLineup(List<Artist> artists) {
        this.lineup = artists;
    }

    /**
     * Returns a string representation of the Event object.
     *
     * @return a string representation of the Event object.
     */
    @Override
    public String toString() {
        return "Event ID: " + eventID + "\n"
                + "Event Name: " + eventName + "\n"
                + "Date: " + date + "\n"
                + "Location: " + location + "\n"
                + "Price: " + price + "\n"
                + "Link: " + link + "\n"
                + "Image URL: " + imageUrl + "\n"
                + "Lineup: " + lineup.size() + " artists";
    }

}
