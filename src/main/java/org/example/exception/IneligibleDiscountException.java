package org.example.exception;

import org.example.model.player.Player;

/**
 * Thrown when attempting to assign a discount to a player who is not eligible (e.g. RegularPlayer).
 */
public class IneligibleDiscountException extends GameStoreException {

    public IneligibleDiscountException(String playerName, String playerType) {
        super("Player '" + playerName + "' (" + playerType + ") is not eligible for discounts. Discounts are restricted to VIP players.");
    }

    public IneligibleDiscountException(Player player) {
        this(player.getName(), player.getPlayerType());
    }
}
