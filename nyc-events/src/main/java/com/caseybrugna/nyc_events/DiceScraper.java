package com.caseybrugna.nyc_events;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A web scraper that extracts event data from a website and stores it in Event
 * objects.
 */
public class DiceScraper {

    /**
     * Scrapes event data from a website and returns a list of Event objects.
     *
     * @return a list of Event objects containing the scraped event data
     * @throws RuntimeException if an error occurs while fetching the website data
     *                          or extracting event details
     */
    public static List<Event> scrapeEvents() {
        String url = "https://dice.fm/browse/new-york/music/dj";
        List<Event> events = new ArrayList<>();

        try {
            Document document = Jsoup.connect(url).get();
            Elements eventElements = document.select("div.EventCard__Event-sc-95ckmb-1.iTiBRM");

            for (Element eventElement : eventElements) {
                try {
                    Event event = extractEvent(eventElement);
                    events.add(event);
                } catch (Exception e) {
                    // Log the error and continue with the next event
                    System.err.println("An error occurred while extracting event details: " + e.getMessage());
                }
            }

            System.out.println("Scraping complete. Total events: " + events.size());
        } catch (IOException e) {
            throw new RuntimeException("An error occurred while fetching the website data: " + e.getMessage(), e);
        }

        return events;
    }

    /**
     * Extracts event details from an event element and returns an Event object.
     *
     * @param eventElement the event element to extract details from
     * @return an Event object containing the extracted event details
     */
    private static Event extractEvent(Element eventElement) {
        String eventName = extractText(eventElement, "div.styles__Title-mwubo3-6");
        String date = extractText(eventElement, "div.styles__Date-mwubo3-8");
        String location = extractText(eventElement, "div.styles__Venue-mwubo3-7");
        String price = extractText(eventElement, "div.styles__Price-mwubo3-9");
        String link = "https://dice.fm/" + eventElement.select("a.styles__EventCardLink-mwubo3-5").attr("href");
        String imageUrl = eventElement.select("img.styles__Image-mwubo3-3").attr("src");
        String artistsString = extractArtistsString(link);

        return new Event(eventName, date, location, price, link, imageUrl, artistsString);
    }

    /**
     * Extracts text from the specified CSS selector within the given element.
     *
     * @param element     the element to extract text from
     * @param cssSelector the CSS selector to locate the target element
     * @return the extracted text, or an empty string if the element is not found
     */
    private static String extractText(Element element, String cssSelector) {
        Element targetElement = element.selectFirst(cssSelector);
        return targetElement != null ? targetElement.text() : "";
    }

    /**
     * Extracts the lineup of artists from an event page given its URL.
     *
     * @param eventLink the URL of the event page
     * @return a string representing the lineup of artists, or null if no lineup is
     *         found or an error occurs
     */
    private static String extractArtistsString(String eventLink) {
        try {
            Document eventDocument = Jsoup.connect(eventLink).get();
            Element artistElement = eventDocument.selectFirst("div.EventDetailsLineup__ArtistTitle-gmffoe-10.ciOfhd");
            if (artistElement != null) {
                String artistsString = artistElement.text();
                if (!artistsString.matches(".*and\\s+\\d+\\s+more$")) {
                    return artistsString;
                }
            }
        } catch (IOException e) {
            System.err.println("An error occurred while extracting the lineup: " + e.getMessage());
        }

        return null;
    }
}
