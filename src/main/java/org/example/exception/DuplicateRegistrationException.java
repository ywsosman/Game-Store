package org.example.exception;

/**
 * Thrown when attempting to register a player who is already
 * registered in the same tournament.
 */
public class DuplicateRegistrationException extends GameStoreException {

    public DuplicateRegistrationException(String playerName, String tournamentName) {
        super("Player '" + playerName + "' is already registered in tournament '" + tournamentName + "'.");
    }
}
