package org.example.exception;

/**
 * Thrown when attempting to register a player in a tournament that has
 * already reached its maximum capacity.
 */
public class TournamentFullException extends GameStoreException {

    public TournamentFullException(String tournamentName, int maxPlayers) {
        super("Tournament '" + tournamentName + "' is full (max " + maxPlayers + " players).");
    }
}
