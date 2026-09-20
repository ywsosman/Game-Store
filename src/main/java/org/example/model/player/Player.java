package org.example.model.player;



/**
 * Abstract base class for all player types.
 * Each subclass defines its own player type label.
 */
public abstract class Player {

    private int id;
    private String name;
    private String email;

    // Default constructor
    protected Player() {
    }

    // Parameterized constructor
    protected Player(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
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

    @Override
    public String toString() {
        return String.format("%-4d | %-20s | %-25s | %s",
                id, name, email, getPlayerType());
    }
}
