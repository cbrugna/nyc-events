package com.caseybrugna.nyc_events;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    public void insertEvents(List<Event> events) {
        String query = "INSERT INTO Events (EventID, EventName, Date, Location, Price, Link, ImageUrl) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            for (Event event : events) {


                statement.setString(1, event.getEventID());
                statement.setString(2, event.getEventName());
                statement.setDate(3, event.getDate());
                statement.setString(4, event.getLocation());
                statement.setString(5, event.getPrice());
                statement.setString(6, event.getLink());
                statement.setString(7, event.getImageUrl());
                statement.addBatch();
            }

            statement.executeBatch();
            System.out.println("Events inserted into the database.");
        } catch (SQLException e) {
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
