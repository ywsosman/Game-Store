package org.example.model.game;

import org.example.enums.Genre;

/**
 * Represents a game designed for gaming consoles (PlayStation, Xbox, Nintendo, etc.).
 */
public class ConsoleGame extends Game {

    private String consoleBrand;

    public ConsoleGame() {
    }

    public ConsoleGame(int id, String name, double price, Genre genre, double rating, String consoleBrand) {
        super(id, name, price, genre, rating);
        this.consoleBrand = consoleBrand;
    }

    @Override
    public String getPlatform() {
        return consoleBrand;
    }

    public String getConsoleBrand() {
        return consoleBrand;
    }

    public void setConsoleBrand(String consoleBrand) {
        this.consoleBrand = consoleBrand;
    }
}
