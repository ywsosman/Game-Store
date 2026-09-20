package org.example.exception;

/**
 * Base exception for all custom Game Store exceptions.
 * All domain-specific exceptions extend this class, making it easy
 * to catch any store-related error with a single catch block.
 */
public class GameStoreException extends RuntimeException {

    public GameStoreException(String message) {
        super(message);
    }

    public GameStoreException(String message, Throwable cause) {
        super(message, cause);
    }
}
