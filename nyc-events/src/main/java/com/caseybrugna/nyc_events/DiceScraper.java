package com.caseybrugna.nyc_events;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.NoSuchElementException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.time.Duration;

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

        System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");
        WebDriver driver = new ChromeDriver();
        driver.get(url);

        // Dismiss the cookies consent popup
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // waits up to 10 seconds
            WebElement allowCookiesButton = wait.until(ExpectedConditions
                    .elementToBeClickable(By.cssSelector(".ch2-btn.ch2-allow-all-btn.ch2-btn-primary")));
            allowCookiesButton.click();
        } catch (Exception e) {
            System.err
                    .println("An error occurred while trying to dismiss the cookies consent popup: " + e.getMessage());
        }

        // Load more try
        try {
            while (true) {
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // waits up to 10 seconds

                WebElement loadMoreDiv = driver
                        .findElement(By.cssSelector("div.styles__LoadMoreRow-sc-1505uh6-1.cxEHUh"));

                // Now find the button within that div
                WebElement loadMoreButton = loadMoreDiv
                        .findElement(By.cssSelector("button.ButtonBase-sc-1lkfwal-0.Button-b7vefn-0.gIWihf.cIyxHS"));

                wait.until(ExpectedConditions.elementToBeClickable(loadMoreButton));

                loadMoreButton.click();

                try {
                    Thread.sleep(3000); // adjust this delay as necessary
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        } catch (NoSuchElementException e) {
            // The "Load more" button is no longer found in the page, so we have loaded all
            // the content.
        }

        List<WebElement> eventElements = driver.findElements(By.cssSelector("div.EventCard__Event-sc-95ckmb-1"));
        // driver.quit();

        for (WebElement eventElement : eventElements) {
            try {
                Event event = extractEvent(eventElement);
                events.add(event);
            } catch (Exception e) {
                // Log the error and continue with the next event
                System.err.println("An error occurred while extracting event details: " + e.getMessage());
            }
        }

        driver.quit();

        return events;
    }

    /**
     * Extracts event details from an event element and returns an Event object.
     *
     * @param eventElement the event element to extract details from
     * @return an Event object containing the extracted event details
     */
    private static Event extractEvent(WebElement eventElement) {
        String eventName = extractText(eventElement, "div.styles__Title-mwubo3-6");
        String date = extractText(eventElement, "div.styles__Date-mwubo3-8");
        String location = extractText(eventElement, "div.styles__Venue-mwubo3-7");
        String price = extractText(eventElement, "div.styles__Price-mwubo3-9");
        String link = eventElement.findElement(By.cssSelector("a.styles__EventCardLink-mwubo3-5")).getAttribute("href");
        String imageUrl = eventElement.findElement(By.cssSelector("img.styles__Image-mwubo3-3")).getAttribute("src");
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
    private static String extractText(WebElement element, String cssSelector) {
        WebElement targetElement = element.findElement(By.cssSelector(cssSelector));
        return targetElement != null ? targetElement.getText() : "";
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
            Element artistElement = eventDocument.selectFirst("div.EventDetailsLineup__ArtistTitle-gmffoe-10");
            if (artistElement != null) {
                return artistElement.text();
            }
        } catch (IOException e) {
            System.err.println("An error occurred while extracting the lineup: " + e.getMessage());
        }

        return null;
    }
}
