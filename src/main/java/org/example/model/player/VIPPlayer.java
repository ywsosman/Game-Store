package org.example.model.player;

/**
 * A premium player with a default 20% discount.
 * Discount can be configured at runtime via setDiscount().
 */
public class VIPPlayer extends Player {

    private static final double DEFAULT_VIP_DISCOUNT = 0.20;

    public VIPPlayer() {
        setDiscount(DEFAULT_VIP_DISCOUNT);
    }

    public VIPPlayer(int id, String name, String email) {
        super(id, name, email);
        setDiscount(DEFAULT_VIP_DISCOUNT);
    }

    public VIPPlayer(int id, String name, String email, double discount) {
        super(id, name, email);
        setDiscount(discount);
    }

    @Override
    public String getPlayerType() {
        return "VIP";
    }
}
