package org.example.model.player;

import org.example.exception.IneligibleDiscountException;

/**
 * Abstract base class for all player types.
 * Each subclass defines its own player type label and default discount.
 * The discount is configurable at runtime via setDiscount().
 */
public abstract class Player {

    private int id;
    private String name;
    private String email;
    private double discount;

    // Default constructor
    protected Player() {
        this.discount = 0.0;
    }

    // Parameterized constructor
    protected Player(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.discount = 0.0;
    }

    /**
     * Returns a human-readable label for the player type (e.g. "Regular", "VIP").
     */
    public abstract String getPlayerType();

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the discount rate as a decimal between 0.0 and 1.0.
     * For example, 0.20 represents a 20% discount.
     *
     * @return the discount rate
     */
    public double getDiscount() {
        return discount;
    }

    /**
     * Sets the discount rate. Subclasses enforce eligibility.
     *
     * @param discount the discount rate (0.0 to 1.0)
     * @throws IneligibleDiscountException if the player type is not eligible for discounts
     */
    public void setDiscount(double discount) throws IneligibleDiscountException {
        this.discount = discount;
    }

    @Override
    public String toString() {
        if (discount > 0) {
            return String.format("%-4d | %-20s | %-25s | %-8s | %d%% discount",
                    id, name, email, getPlayerType(), (int) (discount * 100));
        }
        return String.format("%-4d | %-20s | %-25s | %s",
                id, name, email, getPlayerType());
    }
}
