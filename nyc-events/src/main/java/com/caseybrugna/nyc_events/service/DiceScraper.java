package com.caseybrugna.nyc_events.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.caseybrugna.nyc_events.model.Event;

import org.openqa.selenium.NoSuchElementException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class that provides methods to scrape event data from a website and store
 * it in Event objects.
 */
public class DiceScraper {
    private static final Logger LOGGER = Logger.getLogger(DiceScraper.class.getName());

    /**
     * Scrapes event data from the "https://dice.fm/browse/new-york/music/dj" website.
     * It loads all available events by clicking on the "Load more" button until it's no longer available.
     * Then, it extracts the details of each event and creates an Event object for each one.
     *
     * @return A list of Event objects, each representing an event extracted from the website.
     * @throws RuntimeException If there's an error during website data retrieval or event detail extraction.
     */
    public static List<Event> scrapeEvents() {
        String url = "https://dice.fm/browse/new-york/music/dj";
        WebDriver driver = setupWebDriver();
        driver.get(url);
        dismissCookieConsentPopup(driver);
        //loadAllEvents(driver);
        List<Event> events = extractEventDetails(driver);
        driver.quit();
        return events;
    }

    /**
     * Sets up the WebDriver needed for web scraping operations.
     *
     * @return An instance of WebDriver.
     */
    private static WebDriver setupWebDriver() {
        System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");
        WebDriver driver = new ChromeDriver();
        return driver;
    }

    /**
     * Dismisses the cookie consent popup on the website.
     *
     * @param driver The WebDriver instance used for web scraping operations.
     */
    private static void dismissCookieConsentPopup(WebDriver driver) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement allowCookiesButton = wait.until(ExpectedConditions
                    .elementToBeClickable(By.cssSelector(".ch2-btn.ch2-allow-all-btn.ch2-btn-primary")));
            allowCookiesButton.click();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An error occurred while trying to dismiss the cookies consent popup: " + e.getMessage(), e);
        }
    }

    /**
     * Loads all available events by clicking on the "Load more" button until it's no longer available.
     *
     * @param driver The WebDriver instance used for web scraping operations.
     */
    private static void loadAllEvents(WebDriver driver) {
        try {
            while (true) {
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                WebElement loadMoreDiv = driver
                        .findElement(By.cssSelector("div.styles__LoadMoreRow-sc-1505uh6-1.cxEHUh"));
                WebElement loadMoreButton = loadMoreDiv
                        .findElement(By.cssSelector("button.ButtonBase-sc-1lkfwal-0.Button-b7vefn-0.gIWihf.cIyxHS"));
                wait.until(ExpectedConditions.elementToBeClickable(loadMoreButton));
                loadMoreButton.click();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    LOGGER.log(Level.SEVERE, "An error occurred while loading more events: " + e.getMessage(), e);
                }
            }
        } catch (NoSuchElementException e) {
            // All events have been loaded
        }
    }

    /**
     * Extracts the details of each event from the loaded website data and creates a list of Event objects.
     *
     * @param driver The WebDriver instance used for web scraping operations.
     * @return A list of Event objects, each representing an event extracted from the website.
     */
    private static List<Event> extractEventDetails(WebDriver driver) {
        List<Event> events = new ArrayList<>();
        List<WebElement> eventElements = driver.findElements(By.cssSelector("div.EventCard__Event-sc-95ckmb-1"));
        for (WebElement eventElement : eventElements) {
            try {
                Event event = extractEvent(eventElement);
                events.add(event);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "An error occurred while extracting event details: " + e.getMessage(), e);
            }
        }
        return events;
    }

    /**
     * Extracts the details of an event from a given WebElement.
     * It finds specific details such as event name, date, location, price, link, image URL, and artists.
     * Then, creates and returns an Event object with these details.
     *
     * @param eventElement The WebElement from which to extract event details.
     * @return An Event object with the details extracted from the provided WebElement.
     */
    private static Event extractEvent(WebElement eventElement) {
        try {
            String eventName = extractText(eventElement, "div.styles__Title-mwubo3-6");
            String date = extractText(eventElement, "div.styles__Date-mwubo3-8");
            String location = extractText(eventElement, "div.styles__Venue-mwubo3-7");
            String price = extractText(eventElement, "div.styles__Price-mwubo3-9");
            String link = eventElement.findElement(By.cssSelector("a.styles__EventCardLink-mwubo3-5")).getAttribute("href");
            String imageUrl = eventElement.findElement(By.cssSelector("img.styles__Image-mwubo3-3")).getAttribute("src");
            String artistsString = extractArtistsString(link);
            //String eventName, java.sql.Date date, String location, String price, String link, String imageUrl,
            //String artistsString
            return new Event(eventName, date, location, price, link, imageUrl, artistsString);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An error occurred while extracting event details:", e);
            throw e;
        }
    }

    /**
     * Extracts text from a WebElement using a given CSS selector.
     * If the target element doesn't exist, it returns an empty string.
     *
     * @param element     The WebElement from which to extract text.
     * @param cssSelector The CSS selector to locate the target element within the provided WebElement.
     * @return The extracted text as a String, or an empty string if the target element is not found.
     */
    private static String extractText(WebElement element, String cssSelector) {
        WebElement targetElement = element.findElement(By.cssSelector(cssSelector));
        return targetElement != null ? targetElement.getText() : "";
    }

    /**
     * Extracts the lineup of artists from an event page by connecting to the event's URL.
     * If an artist lineup isn't found or an error occurs during extraction, it returns null.
     *
     * @param eventLink The URL of the event page from which to extract the artist lineup.
     * @return A String representing the artist lineup, or null if no lineup is found or an error occurs during extraction.
     */
    private static String extractArtistsString(String eventLink) {
        try {
            Document eventDocument = Jsoup.connect(eventLink).get();
            Element artistElement = eventDocument.selectFirst("div.EventDetailsLineup__ArtistTitle-gmffoe-10");
            if (artistElement != null) {
                return artistElement.text();
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "An error occurred while extracting the lineup: " + e.getMessage(), e);
        }

        return null;
    }
}
