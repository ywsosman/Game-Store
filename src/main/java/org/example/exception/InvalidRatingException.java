package org.example.exception;

/**
 * Thrown when attempting to set a rating outside the valid 0.0–5.0 range.
 */
public class InvalidRatingException extends GameStoreException {

    public InvalidRatingException(double rating) {
        super("Invalid rating: " + rating + ". Rating must be between 0.0 and 5.0.");
    }
}
