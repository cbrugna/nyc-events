package com.caseybrugna.nyc_events;

public class Artist {
    private String name;

    /**
     * Creates a new Artist with the specified name.
     *
     * @param name the name of the artist
     */
    public Artist(String name) {
        this.name = name;
    }

    /**
     * Retrieves the name of the artist.
     *
     * @return the name of the artist
     */
    public String getName() {
        return name;
    }

    /**
     * Returns a string representation of the artist.
     * 
     * @return a string representation of the artist
     */
    @Override
    public String toString() {
        return "Artist{" +
                "name='" + name + '\'' +
                '}';
    }
}
