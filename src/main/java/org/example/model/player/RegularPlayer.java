package org.example.model.player;

import org.example.exception.IneligibleDiscountException;

/**
 * A standard player with no discount eligibility.
 * Attempting to set a discount > 0 throws IneligibleDiscountException.
 */
public class RegularPlayer extends Player {

    public RegularPlayer() {
    }

    public RegularPlayer(int id, String name, String email) {
        super(id, name, email);
    }

    @Override
    public void setDiscount(double discount) throws IneligibleDiscountException {
        if (discount > 0) {
            throw new IneligibleDiscountException(this);
        }
        super.setDiscount(0.0);
    }

    @Override
    public String getPlayerType() {
        return "Regular";
    }
}
