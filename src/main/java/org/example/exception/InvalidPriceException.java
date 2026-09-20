package org.example.exception;

/**
 * Thrown when attempting to set a negative price on a game.
 */
public class InvalidPriceException extends GameStoreException {

    public InvalidPriceException(double price) {
        super("Invalid price: " + price + ". Price must be >= 0.");
    }
}
