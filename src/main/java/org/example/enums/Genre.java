package org.example.enums;

/**
 * Represents the genre categories available for games in the store.
 */
public enum Genre {
    ACTION("Action"),
    ADVENTURE("Adventure"),
    RPG("RPG"),
    SPORTS("Sports"),
    STRATEGY("Strategy"),
    PUZZLE("Puzzle"),
    SIMULATION("Simulation"),
    HORROR("Horror");

    private final String displayName;

    Genre(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    /**
     * Finds a Genre by its display name (case-insensitive).
     *
     * @param name the display name to search for
     * @return the matching Genre
     * @throws IllegalArgumentException if no matching genre is found
     */
    public static Genre fromDisplayName(String name) {
        for (Genre genre : values()) {
            if (genre.displayName.equalsIgnoreCase(name) || genre.name().equalsIgnoreCase(name)) {
                return genre;
            }
        }
        throw new IllegalArgumentException("Unknown genre: " + name);
    }
}
