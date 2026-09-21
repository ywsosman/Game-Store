package org.example.model.player;

/**
 * A standard player with no discount by default.
 * Discount can be configured at runtime via setDiscount().
 */
public class RegularPlayer extends Player {

    public RegularPlayer() {
    }

    public RegularPlayer(int id, String name, String email) {
        super(id, name, email);
        // discount defaults to 0.0 from Player
    }

    @Override
    public String getPlayerType() {
        return "Regular";
    }
}
