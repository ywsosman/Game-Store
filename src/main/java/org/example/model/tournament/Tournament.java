package org.example.model.tournament;

import org.example.exception.DuplicateRegistrationException;
import org.example.exception.TournamentFullException;
import org.example.model.player.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Represents a tournament where players can register and compete.
 * Supports capacity limits, duplicate checks, and elimination-style
 * match simulation with random outcomes.
 */
public class Tournament {

    private int id;
    private String name;
    private int maxPlayers;
    private List<Player> participants;
    private boolean started;
    private Player winner;
    private List<String> matchLog;

    public Tournament() {
        this.participants = new ArrayList<>();
        this.started = false;
        this.matchLog = new ArrayList<>();
    }

    public Tournament(int id, String name, int maxPlayers) {
        this.id = id;
        this.name = name;
        this.maxPlayers = maxPlayers;
        this.participants = new ArrayList<>();
        this.started = false;
        this.matchLog = new ArrayList<>();
    }

    // --- Registration ---

    /**
     * Registers a player in this tournament.
     *
     * @param player the player to register
     * @throws TournamentFullException        if the tournament has reached max capacity
     * @throws DuplicateRegistrationException if the player is already registered
     */
    public void registerPlayer(Player player) {
        if (participants.size() >= maxPlayers) {
            throw new TournamentFullException(name, maxPlayers);
        }

        for (Player p : participants) {
            if (p.getId() == player.getId()) {
                throw new DuplicateRegistrationException(player.getName(), name);
            }
        }

        participants.add(player);
    }

    // --- Tournament simulation ---

    /**
     * Starts the tournament and simulates elimination rounds.
     * Requires at least 2 registered players.
     * Each round pairs players randomly; the winner of each match advances.
     * If there's an odd number of players, one gets a bye (auto-advances).
     *
     * @throws IllegalStateException if fewer than 2 players are registered
     * @throws IllegalStateException if the tournament has already been started
     */
    public void startTournament() {
        if (started) {
            throw new IllegalStateException("Tournament '" + name + "' has already been started.");
        }
        if (participants.size() < 2) {
            throw new IllegalStateException("Tournament '" + name + "' needs at least 2 players to start. Currently: " + participants.size());
        }

        started = true;
        matchLog.clear();
        Random random = new Random();

        // Copy participants so we don't modify the original list
        List<Player> remaining = new ArrayList<>(participants);
        int roundNumber = 1;

        matchLog.add("🏆 Tournament: " + name);
        matchLog.add("   Participants: " + remaining.size());
        matchLog.add("");

        while (remaining.size() > 1) {
            matchLog.add("── Round " + roundNumber + " ──");
            Collections.shuffle(remaining, random);

            List<Player> nextRound = new ArrayList<>();

            for (int i = 0; i < remaining.size(); i += 2) {
                if (i + 1 < remaining.size()) {
                    // Normal match: two players
                    Player player1 = remaining.get(i);
                    Player player2 = remaining.get(i + 1);

                    // Random winner
                    Player matchWinner = random.nextBoolean() ? player1 : player2;
                    Player matchLoser = (matchWinner == player1) ? player2 : player1;

                    matchLog.add(String.format("   %s vs %s  →  Winner: %s",
                            player1.getName(), player2.getName(), matchWinner.getName()));

                    nextRound.add(matchWinner);
                } else {
                    // Odd player out: gets a bye
                    Player byePlayer = remaining.get(i);
                    matchLog.add(String.format("   %s gets a bye (auto-advances)", byePlayer.getName()));
                    nextRound.add(byePlayer);
                }
            }

            remaining = nextRound;
            roundNumber++;
            matchLog.add("");
        }

        winner = remaining.get(0);
        matchLog.add("🥇 Winner: " + winner.getName() + "!");
    }

    /**
     * Returns the full match log as a formatted string.
     *
     * @return the match log
     */
    public String getMatchLog() {
        StringBuilder sb = new StringBuilder();
        for (String line : matchLog) {
            sb.append(line).append("\n");
        }
        return sb.toString();
    }

    // --- Getters and Setters ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public List<Player> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Player> participants) {
        this.participants = participants;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Tournament #%d: %s | Max: %d | Registered: %d/%d | %s",
                id, name, maxPlayers, participants.size(), maxPlayers,
                started ? (winner != null ? "Winner: " + winner.getName() : "In Progress") : "Not Started"));

        if (!participants.isEmpty()) {
            sb.append("\n  Participants:");
            for (Player p : participants) {
                sb.append(String.format("\n    - %s (%s)", p.getName(), p.getPlayerType()));
            }
        }

        return sb.toString();
    }
}
