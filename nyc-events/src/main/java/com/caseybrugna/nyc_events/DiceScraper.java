package com.caseybrugna.nyc_events;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Scrapes event data from a website and stores it in an ArrayList of Event
 * objects.
 */
public class DiceScraper {

    /**
     * Scrapes event data from the website and returns a list of Event objects.
     *
     * @return a list of Event objects containing the scraped event data
     * @throws IOException if an error occurs while fetching the website data
     */
    public static List<Event> scrapeEvents() {
        // URL of the website to scrape
        String url = "https://dice.fm/browse/new-york/music/dj";

        // List to store the Event objects
        List<Event> events = new ArrayList<>();

        try {
            // Fetch the website HTML
            Document document = Jsoup.connect(url).get();

            // Select the HTML elements containing the event data
            Elements eventElements = document.select("div.EventCard__Event-sc-95ckmb-1.iTiBRM");

            // Iterate over the event elements and extract the event details
            for (Element eventElement : eventElements) {
                String eventName = eventElement.select("div.styles__Title-mwubo3-6").text();
                String date = eventElement.select("div.styles__Date-mwubo3-8").text();
                String location = eventElement.select("div.styles__Venue-mwubo3-7").text();
                String price = eventElement.select("div.styles__Price-mwubo3-9").text();
                String link = "https://dice.fm/"
                        + (eventElement.select("a.styles__EventCardLink-mwubo3-5").attr("href"));
                String imageUrl = eventElement.select("img.styles__Image-mwubo3-3").attr("src");

                // Load the event page to extract the lineup of artists
                Document eventDocument = Jsoup.connect(link).get();

                // Extract lineup from the event page
                String artistsString = null;
                try {
                    Element artistElement = eventDocument
                            .selectFirst("div.EventDetailsLineup__ArtistTitle-gmffoe-10.ciOfhd");
                    if (artistElement != null) {
                        artistsString = artistElement.text();
                        // Lineup div exists, but does not contain names
                        if (!artistsString.matches(".*[a-zA-Z].*")) {
                            artistsString = null;
                        }
                    }
                    // No lineup div exsists
                    else {
                        artistsString = null;
                    }

                } catch (Exception e) {
                    System.out.println("An error occurred while extracting the lineup: " + e.getMessage());
                }

                System.out.println("ARTISTS STRING: " + artistsString);

                // Create a new Event object with the extracted details and add it to the list
                Event event = new Event(eventName, date, location, price, link, imageUrl, artistsString);
                events.add(event);
            }
            System.out.println("Scraping complete. Total events: " + events.size());
        } catch (IOException e) {
            throw new RuntimeException("An error occurred while scraping the website: " + e.getMessage(), e);
        }

        return events;
    }

    public static void main(String[] args) {
        try {
            // Scrape the events and store them in a list
            List<Event> events = scrapeEvents();

            // Print the event details
            
            for (Event event : events) {
                System.out.println(event);
            }
             

        } catch (RuntimeException e) {
            System.out.println("An error occurred while scraping the events: " + e.getMessage());
        }
    }
}

/**
 * Example Event Card from DICE.FM *
 * 
 * 
 * 
 * <div class="EventCard__Event-sc-95ckmb-1 iTiBRM">
 * <a href=
 * "/event/ewvry-will-clarke-open-air-2023-gray-area-9th-jun-ruins-at-knockdown-center-new-york-tickets"
 * class="styles__EventCardLink-mwubo3-5 drYrZL">
 * <div class="styles__ImageWrapper-mwubo3-2 gLMyUD">
 * <!-- Image element -->
 * </div>
 * <div class="styles__EventDetails-mwubo3-4 etQVRr">
 * <div class="styles__Title-mwubo3-6 bAhusj">Event Title</div>
 * <div class="styles__Date-mwubo3-8 liAOLh">Event Date</div>
 * <div class="styles__Venue-mwubo3-7 dtVuFX">Event Venue</div>
 * <div class="styles__Price-mwubo3-9 ihGaVh">Event Price</div>
 * </div>
 * </a>
 * <div class="styles__Actions-mwubo3-1 kXrUfh">
 * <!-- Buttons or actions related to the event -->
 * </div>
 * </div>
 *
 * 
 * 
 */
