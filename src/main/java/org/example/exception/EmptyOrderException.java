package org.example.exception;

/**
 * Thrown when attempting to checkout an order that contains no games.
 */
public class EmptyOrderException extends GameStoreException {

    public EmptyOrderException(int orderId) {
        super("Cannot checkout order #" + orderId + ": the order is empty.");
    }
}
