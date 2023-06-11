package com.caseybrugna.nyc_events;

import java.util.List;

/**
     * Main method to scrape events and print their details.
     *
     * @param args command-line arguments (not used)
     */
public class Main {
    public static void main(String[] args) {
        try {
            List<Event> events = DiceScraper.scrapeEvents();

            for (Event event : events) {
                System.out.println(event);
            }

        } catch (RuntimeException e) {
            System.err.println("An error occurred while scraping the events: " + e.getMessage());
        }
    }
}
