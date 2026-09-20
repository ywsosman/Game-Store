package org.example.model.game;

import org.example.enums.Genre;

/**
 * Represents a game designed for PC platforms.
 */
public class PCGame extends Game {

    private String operatingSystem;

    public PCGame() {
    }

    public PCGame(int id, String name, double price, Genre genre, double rating, String operatingSystem) {
        super(id, name, price, genre, rating);
        this.operatingSystem = operatingSystem;
    }

    @Override
    public String getPlatform() {
        return "PC (" + operatingSystem + ")";
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(String operatingSystem) {
        this.operatingSystem = operatingSystem;
    }
}
