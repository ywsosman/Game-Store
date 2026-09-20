package org.example.model.game;

import org.example.enums.Genre;
import org.example.exception.InvalidPriceException;
import org.example.exception.InvalidRatingException;

/**
 * Abstract base class for all game types in the store.
 * Each subclass must define its platform identity.
 */
public abstract class Game {

    private int id;
    private String name;
    private double price;
    private Genre genre;
    private double rating;

    // Default constructor
    protected Game() {
    }

    // Parameterized constructor with validation
    protected Game(int id, String name, double price, Genre genre, double rating) {
        this.id = id;
        this.name = name;
        setPrice(price);
        this.genre = genre;
        setRating(rating);
    }

    /**
     * Returns the platform name for this game type (e.g. "PC", "PlayStation", "Mobile").
     */
    public abstract String getPlatform();

    // --- Getters and Setters ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    /**
     * Sets the price after validating it is non-negative.
     *
     * @param price the price to set
     * @throws InvalidPriceException if price is negative
     */
    public void setPrice(double price) {
        if (price < 0) {
            throw new InvalidPriceException(price);
        }
        this.price = price;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public double getRating() {
        return rating;
    }

    /**
     * Sets the rating after validating it is within 0.0–5.0.
     *
     * @param rating the rating to set
     * @throws InvalidRatingException if rating is outside the valid range
     */
    public void setRating(double rating) {
        if (rating < 0.0 || rating > 5.0) {
            throw new InvalidRatingException(rating);
        }
        this.rating = rating;
    }

    @Override
    public String toString() {
        return String.format("%-4d | %-25s | $%-8.2f | %-12s | ★ %.1f | %s",
                id, name, price, genre.getDisplayName(), rating, getPlatform());
    }
}
