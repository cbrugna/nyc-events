package com.caseybrugna.nyc_events;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;

import com.wrapper.spotify.model_objects.specification.ExternalUrl;

import javassist.bytecode.stackmap.BasicBlock.Catch;

public class DatabaseDAO {
    private Connection connection;
    private String url;
    private String username;
    private String password;

    public DatabaseDAO(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public void deleteOldEvents() {
        String query = "DELETE FROM Events WHERE Date < ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            Date currentDate = getCurrentDate();
            statement.setDate(1, currentDate);

            int rowsDeleted = statement.executeUpdate();
            System.out.println("Deleted " + rowsDeleted + " old events from the database.");
        } catch (SQLException e) {
            System.err.println("Problem with deletion.");
            e.printStackTrace();
        }
    }

    private Date getCurrentDate() {
        LocalDate currentDate = LocalDate.now();
        return Date.valueOf(currentDate);
    }

    public void insertArtists(List<Artist> artists) {
        String query = "INSERT IGNORE INTO Artists (ArtistID, Name, HasSpotifyProfile, PopularityScore, ExternalUrl, Genres) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            for (Artist artist : artists) {
                statement.setString(1, artist.getArtistID());
                statement.setString(2, artist.getName());
                statement.setBoolean(3, artist.getHasArtistProfile());
                statement.setInt(4, artist.getPopularityScore());
                statement.setString(5, artist.getExternalUrl());
                statement.setString(6, artist.getArtistGenresAsString());

                statement.addBatch();

            }

            statement.executeBatch();
            System.out.println("Artists inserted into the database.");

        } catch (SQLException e) {
            System.err.println("Problem with inserting artists.");
            e.printStackTrace();
        }
    }

    public void insertEvents(List<Event> events) {
        String query = "INSERT IGNORE INTO Events (EventID, EventName, Date, Location, Price, Link, ImageUrl, Lineup) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)" +
                "ON DUPLICATE KEY UPDATE EventName = VALUES(EventName), Date = VALUES(Date), Location = VALUES(Location), "
                +
                "Price = CASE WHEN Price <> VALUES(Price) THEN VALUES(Price) ELSE Price END, " +
                "Link = VALUES(Link), ImageUrl = VALUES(ImageUrl)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            for (Event event : events) {

                statement.setString(1, event.getEventID());
                statement.setString(2, event.getEventName());
                statement.setDate(3, event.getDate());
                statement.setString(4, event.getLocation());
                statement.setString(5, event.getPrice());
                statement.setString(6, event.getLink());
                statement.setString(7, event.getImageUrl());
                statement.setString(8, event.getLineupAsString());

                statement.addBatch();
            }

            statement.executeBatch();
            System.out.println("Events inserted into the database.");
        } catch (SQLException e) {
            System.err.println("Problem with inserting events.");
            e.printStackTrace();
        }
    }

    public void insertTracks(List<Artist> artists) {
        String query = "INSERT IGNORE INTO Tracks (TrackID, TrackName, ArtistID) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            for (Artist artist : artists) {
                String[] trackIDs = artist.getTopTrackIDs();
                String[] trackTitles = artist.getTopTrackTitles();
                String artistID = artist.getArtistID();

                if (trackIDs != null && trackTitles != null && artistID != null) {
                    for (int i = 0; i < trackIDs.length; i++) {
                        String trackID = trackIDs[i];
                        String trackTitle = trackTitles[i];

                        statement.setString(1, trackID);
                        statement.setString(2, trackTitle);
                        statement.setString(3, artistID);

                        statement.addBatch();
                    }
                }

            }

            statement.executeBatch();
            System.out.println("Tracks inserted into the database.");
        } catch (SQLException e) {
            System.err.println("Problem with inserting tracks.");
            // Handle any additional exceptions that occur during the catch block
            SQLException nextException = e.getNextException();
            if (nextException != null) {
                System.err.println("Additional exception occurred:");
                nextException.printStackTrace();
            }
        }
    }

    public void insertEventArtists(List<Event> events) {
        String query = "INSERT IGNORE INTO EventArtists (EventID, ArtistID1, ArtistID2, ArtistID3) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            for (Event event : events) {

                statement.setString(1, event.getEventID());

                statement.setString(2, event.getArtistID(1));
                statement.setString(3, event.getArtistID(2));
                statement.setString(4, event.getArtistID(3));

                statement.addBatch();
            }

            statement.executeBatch();
            System.out.println("Events inserted into the database.");
        } catch (SQLException e) {
            System.err.println("Problem with inserting events.");
            e.printStackTrace();
        }
    }

    public void connect() {
        try {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to the database.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Disconnected from the database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
