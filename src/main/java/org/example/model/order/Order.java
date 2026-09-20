package org.example.model.order;

import org.example.enums.OrderStatus;
import org.example.exception.EmptyOrderException;
import org.example.model.game.Game;
import org.example.model.player.Player;
import org.example.model.player.VIPPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an order belonging to a player, containing a list of games.
 * Supports adding/removing games, calculating the total with VIP discount,
 * and checking out.
 */
public class Order {

    private int id;
    private Player player;
    private List<Game> games;
    private OrderStatus status;

    public Order() {
        this.games = new ArrayList<>();
        this.status = OrderStatus.PENDING;
    }

    public Order(int id, Player player) {
        this.id = id;
        this.player = player;
        this.games = new ArrayList<>();
        this.status = OrderStatus.PENDING;
    }

    // --- Game management ---

    /**
     * Adds a game to this order.
     *
     * @param game the game to add
     */
    public void addGame(Game game) {
        games.add(game);
    }

    /**
     * Removes a game from this order by its ID.
     *
     * @param gameId the ID of the game to remove
     * @return true if the game was found and removed, false otherwise
     */
    public boolean removeGame(int gameId) {
        return games.removeIf(game -> game.getId() == gameId);
    }

    // --- Price calculation ---

    /**
     * Calculates the subtotal (sum of all game prices).
     *
     * @return the subtotal
     */
    public double getSubtotal() {
        double subtotal = 0;
        for (Game game : games) {
            subtotal += game.getPrice();
        }
        return subtotal;
    }

    /**
     * Calculates the final total after applying the player's discount.
     * Only VIP players receive a discount. Regular players pay full price.
     *
     * @return the final total price
     */
    public double calculateTotal() {
        double subtotal = getSubtotal();

        if (player instanceof VIPPlayer) {
            VIPPlayer vip = (VIPPlayer) player;
            double discountAmount = subtotal * vip.getDiscount();
            return subtotal - discountAmount;
        }

        return subtotal;
    }

    /**
     * Confirms the order. Throws EmptyOrderException if the order has no games.
     *
     * @throws EmptyOrderException if the order contains no games
     */
    public void checkout() {
        if (games.isEmpty()) {
            throw new EmptyOrderException(id);
        }
        this.status = OrderStatus.CONFIRMED;
    }

    /**
     * Cancels the order.
     */
    public void cancel() {
        this.status = OrderStatus.CANCELLED;
    }

    // --- Getters and Setters ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Order #%d | Player: %s (%s) | Status: %s\n",
                id, player.getName(), player.getPlayerType(), status.getDisplayName()));
        sb.append("  Games:\n");

        if (games.isEmpty()) {
            sb.append("    (no games added)\n");
        } else {
            for (Game game : games) {
                sb.append(String.format("    - %-25s $%.2f\n", game.getName(), game.getPrice()));
            }
        }

        double subtotal = getSubtotal();
        sb.append(String.format("  Subtotal:        $%.2f\n", subtotal));

        if (player instanceof VIPPlayer) {
            VIPPlayer vip = (VIPPlayer) player;
            double discountAmount = subtotal * vip.getDiscount();
            sb.append(String.format("  VIP discount (%d%%): -$%.2f\n",
                    (int) (vip.getDiscount() * 100), discountAmount));
        }

        sb.append("  ─────────────────────\n");
        sb.append(String.format("  Total:           $%.2f", calculateTotal()));

        return sb.toString();
    }
}
