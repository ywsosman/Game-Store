package org.example.model.game;

import org.example.enums.Genre;

/**
 * Represents a mobile game. May be free-to-play.
 */
public class MobileGame extends Game {

    private boolean freeToPlay;

    public MobileGame() {
    }

    public MobileGame(int id, String name, double price, Genre genre, double rating, boolean freeToPlay) {
        super(id, name, price, genre, rating);
        this.freeToPlay = freeToPlay;
    }

    @Override
    public String getPlatform() {
        return freeToPlay ? "Mobile (Free-to-Play)" : "Mobile";
    }

    public boolean isFreeToPlay() {
        return freeToPlay;
    }

    public void setFreeToPlay(boolean freeToPlay) {
        this.freeToPlay = freeToPlay;
    }
}
