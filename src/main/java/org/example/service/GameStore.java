package org.example.service;

import org.example.enums.Genre;
import org.example.interfaces.Searchable;
import org.example.model.game.Game;
import org.example.model.order.Order;
import org.example.model.player.Player;
import org.example.model.tournament.Tournament;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Central manager for the Game Store application.
 * Holds all games, players, orders, and tournaments.
 * Implements Searchable to provide game catalog search functionality.
 */
public class GameStore implements Searchable<Game> {

    private List<Game> games;
    private List<Player> players;
    private List<Order> orders;
    private List<Tournament> tournaments;

    private int nextGameId;
    private int nextPlayerId;
    private int nextOrderId;
    private int nextTournamentId;

    public GameStore() {
        this.games = new ArrayList<>();
        this.players = new ArrayList<>();
        this.orders = new ArrayList<>();
        this.tournaments = new ArrayList<>();
        this.nextGameId = 1;
        this.nextPlayerId = 1;
        this.nextOrderId = 1;
        this.nextTournamentId = 1;
    }

    // ==================== GAME MANAGEMENT ====================

    /**
     * Adds a game to the catalog and assigns it an auto-incremented ID.
     *
     * @param game the game to add
     */
    public void addGame(Game game) {
        game.setId(nextGameId++);
        games.add(game);
    }

    /**
     * Finds a game by its ID.
     *
     * @param id the game ID
     * @return the game, or null if not found
     */
    public Game findGameById(int id) {
        for (Game game : games) {
            if (game.getId() == id) {
                return game;
            }
        }
        return null;
    }

    /**
     * Searches for games whose name contains the keyword (case-insensitive).
     */
    @Override
    public List<Game> searchByName(String name) {
        List<Game> results = new ArrayList<>();
        for (Game game : games) {
            if (game.getName().toLowerCase().contains(name.toLowerCase())) {
                results.add(game);
            }
        }
        return results;
    }

    /**
     * Searches for games matching the specified genre.
     */
    @Override
    public List<Game> searchByGenre(Genre genre) {
        List<Game> results = new ArrayList<>();
        for (Game game : games) {
            if (game.getGenre() == genre) {
                results.add(game);
            }
        }
        return results;
    }

    /**
     * Returns a new list of games sorted by price (lowest first).
     */
    public List<Game> sortByPrice() {
        List<Game> sorted = new ArrayList<>(games);
        sorted.sort(Comparator.comparingDouble(Game::getPrice));
        return sorted;
    }

    /**
     * Returns a new list of games sorted by rating (highest first).
     */
    public List<Game> sortByRating() {
        List<Game> sorted = new ArrayList<>(games);
        sorted.sort(Comparator.comparingDouble(Game::getRating).reversed());
        return sorted;
    }

    // ==================== PLAYER MANAGEMENT ====================

    /**
     * Registers a player and assigns an auto-incremented ID.
     *
     * @param player the player to register
     */
    public void addPlayer(Player player) {
        player.setId(nextPlayerId++);
        players.add(player);
    }

    /**
     * Finds a player by their ID.
     *
     * @param id the player ID
     * @return the player, or null if not found
     */
    public Player findPlayerById(int id) {
        for (Player player : players) {
            if (player.getId() == id) {
                return player;
            }
        }
        return null;
    }

    // ==================== ORDER MANAGEMENT ====================

    /**
     * Creates a new order for a player and assigns an auto-incremented ID.
     *
     * @param player the player who owns the order
     * @return the created order
     */
    public Order createOrder(Player player) {
        Order order = new Order(nextOrderId++, player);
        orders.add(order);
        return order;
    }

    /**
     * Finds an order by its ID.
     *
     * @param id the order ID
     * @return the order, or null if not found
     */
    public Order findOrderById(int id) {
        for (Order order : orders) {
            if (order.getId() == id) {
                return order;
            }
        }
        return null;
    }

    // ==================== TOURNAMENT MANAGEMENT ====================

    /**
     * Creates a new tournament and assigns an auto-incremented ID.
     *
     * @param name       the tournament name
     * @param maxPlayers the maximum number of players
     * @return the created tournament
     */
    public Tournament createTournament(String name, int maxPlayers) {
        Tournament tournament = new Tournament(nextTournamentId++, name, maxPlayers);
        tournaments.add(tournament);
        return tournament;
    }

    /**
     * Finds a tournament by its ID.
     *
     * @param id the tournament ID
     * @return the tournament, or null if not found
     */
    public Tournament findTournamentById(int id) {
        for (Tournament tournament : tournaments) {
            if (tournament.getId() == id) {
                return tournament;
            }
        }
        return null;
    }

    // ==================== GETTERS ====================

    public List<Game> getGames() {
        return games;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public List<Tournament> getTournaments() {
        return tournaments;
    }
}
