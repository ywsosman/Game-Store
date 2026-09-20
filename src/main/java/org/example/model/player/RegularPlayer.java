package org.example.model.player;

/**
 * A standard player with a fixed 5% discount on orders.
 */
public class RegularPlayer extends Player {


    public RegularPlayer() {
    }

    public RegularPlayer(int id, String name, String email) {
        super(id, name, email);
    }

    @Override
    public String getPlayerType() {
        return "Regular";
    }

}


