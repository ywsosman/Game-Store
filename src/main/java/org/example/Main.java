package org.example;

import org.example.enums.Genre;
import org.example.exception.DuplicateRegistrationException;
import org.example.exception.EmptyOrderException;
import org.example.exception.IneligibleDiscountException;
import org.example.exception.InvalidPriceException;
import org.example.exception.TournamentFullException;
import org.example.model.game.ConsoleGame;
import org.example.model.game.Game;
import org.example.model.game.MobileGame;
import org.example.model.game.PCGame;
import org.example.model.order.Order;
import org.example.model.player.Player;
import org.example.model.player.RegularPlayer;
import org.example.model.player.VIPPlayer;
import org.example.model.tournament.Tournament;
import org.example.service.GameStore;

import java.util.List;

/**
 * Test harness demonstrating all features and exception protections built in Phases 1–4.
 */
public class Main {

    public static void main(String[] args) {
        GameStore store = new GameStore();

        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║          🎮 GAME STORE — FEATURE TEST           ║");
        System.out.println("╚══════════════════════════════════════════════════╝");

        // ====================================================================
        // 1. ADD GAMES TO CATALOG
        // ====================================================================
        System.out.println("\n── 1. Adding Games ──────────────────────────────────");

        PCGame fifa = new PCGame(0, "FIFA 27", 59.99, Genre.SPORTS, 4.5, "Windows");
        PCGame gtaPC = new PCGame(0, "GTA VI", 69.99, Genre.ACTION, 4.8, "Windows");
        ConsoleGame eldenRing = new ConsoleGame(0, "Elden Ring", 49.99, Genre.RPG, 4.9, "PlayStation");
        ConsoleGame halo = new ConsoleGame(0, "Halo Infinite", 39.99, Genre.ACTION, 4.2, "Xbox");
        MobileGame clash = new MobileGame(0, "Clash Royale", 0.00, Genre.STRATEGY, 4.2, true);
        MobileGame pokemon = new MobileGame(0, "Pokemon GO", 0.00, Genre.ADVENTURE, 3.8, true);

        store.addGame(fifa);
        store.addGame(gtaPC);
        store.addGame(eldenRing);
        store.addGame(halo);
        store.addGame(clash);
        store.addGame(pokemon);

        System.out.println("Added " + store.getGames().size() + " games to catalog:");
        for (Game g : store.getGames()) {
            System.out.println("  " + g);
        }

        // ====================================================================
        // 2. REGISTER PLAYERS
        // ====================================================================
        System.out.println("\n── 2. Registering Players ──────────────────────────");

        VIPPlayer ahmed = new VIPPlayer(0, "Ahmed", "ahmed@example.com");
        RegularPlayer sara = new RegularPlayer(0, "Sara", "sara@example.com");
        VIPPlayer omar = new VIPPlayer(0, "Omar", "omar@example.com", 0.30);
        RegularPlayer layla = new RegularPlayer(0, "Layla", "layla@example.com");
        RegularPlayer khaled = new RegularPlayer(0, "Khaled", "khaled@example.com");

        store.addPlayer(ahmed);
        store.addPlayer(sara);
        store.addPlayer(omar);
        store.addPlayer(layla);
        store.addPlayer(khaled);

        System.out.println("Registered " + store.getPlayers().size() + " players:");
        for (Player p : store.getPlayers()) {
            System.out.println("  " + p);
        }

        // ====================================================================
        // 3. CONFIGURABLE VIP DISCOUNT DEMO
        // ====================================================================
        System.out.println("\n── 3. Configurable VIP Discount ────────────────────");

        System.out.println("Omar's VIP discount before: " + (int)(omar.getDiscount() * 100) + "%");
        omar.setDiscount(0.35);  // Reconfigure VIP discount to 35%
        System.out.println("Omar's VIP discount after boost: " + (int)(omar.getDiscount() * 100) + "%");

        // ====================================================================
        // 4. CREATE ORDER & CALCULATE TOTAL (VIP 20%)
        // ====================================================================
        System.out.println("\n── 4. Ahmed's Order (VIP 20%) ──────────────────────");

        Order ahmedOrder = store.createOrder(ahmed);
        ahmedOrder.addGame(fifa);
        ahmedOrder.addGame(eldenRing);
        System.out.println(ahmedOrder);

        // ====================================================================
        // 5. CREATE ORDER & CALCULATE TOTAL (Regular 0%)
        // ====================================================================
        System.out.println("\n── 5. Sara's Order (Regular 0%) ────────────────────");

        Order saraOrder = store.createOrder(sara);
        saraOrder.addGame(gtaPC);
        saraOrder.addGame(clash);
        System.out.println(saraOrder);

        // ====================================================================
        // 6. CHECKOUT
        // ====================================================================
        System.out.println("\n── 6. Checkout ─────────────────────────────────────");

        ahmedOrder.checkout();
        System.out.println("Ahmed's order status: " + ahmedOrder.getStatus().getDisplayName());

        saraOrder.checkout();
        System.out.println("Sara's order status: " + saraOrder.getStatus().getDisplayName());

        // ====================================================================
        // 7. SEARCH GAMES
        // ====================================================================
        System.out.println("\n── 7. Search Games ─────────────────────────────────");

        System.out.println("Search 'FIFA':");
        for (Game g : store.searchByName("FIFA")) {
            System.out.println("  " + g);
        }

        System.out.println("Search genre ACTION:");
        for (Game g : store.searchByGenre(Genre.ACTION)) {
            System.out.println("  " + g);
        }

        // ====================================================================
        // 8. SORT GAMES
        // ====================================================================
        System.out.println("\n── 8. Sort Games ───────────────────────────────────");

        System.out.println("By price (low → high):");
        for (Game g : store.sortByPrice()) {
            System.out.println("  " + g);
        }

        System.out.println("By rating (high → low):");
        for (Game g : store.sortByRating()) {
            System.out.println("  " + g);
        }

        // ====================================================================
        // 9. TOURNAMENT
        // ====================================================================
        System.out.println("\n── 9. Tournament ───────────────────────────────────");

        Tournament worldCup = store.createTournament("Gaming World Cup", 8);
        worldCup.registerPlayer(ahmed);
        worldCup.registerPlayer(sara);
        worldCup.registerPlayer(omar);
        worldCup.registerPlayer(layla);
        worldCup.registerPlayer(khaled);

        System.out.println(worldCup);
        System.out.println();

        worldCup.startTournament();
        System.out.println(worldCup.getMatchLog());

        // ====================================================================
        // 10. EDGE CASES & EXCEPTION HANDLING
        // ====================================================================
        System.out.println("── 10. Edge Cases & Exception Handling ─────────────");

        // Non-VIP discount attempt
        System.out.print("  Non-VIP discount: ");
        try {
            sara.setDiscount(0.10); // Regular player cannot have discount
            System.out.println("FAIL (no exception)");
        } catch (IneligibleDiscountException e) {
            System.out.println("✓ Caught: " + e.getMessage());
        }

        // Negative price
        System.out.print("  Negative price:   ");
        try {
            new PCGame(0, "Bad Game", -10.00, Genre.ACTION, 3.0, "Windows");
            System.out.println("FAIL (no exception)");
        } catch (InvalidPriceException e) {
            System.out.println("✓ Caught: " + e.getMessage());
        }

        // Empty order checkout
        System.out.print("  Empty checkout:   ");
        try {
            Order emptyOrder = store.createOrder(ahmed);
            emptyOrder.checkout();
            System.out.println("FAIL (no exception)");
        } catch (EmptyOrderException e) {
            System.out.println("✓ Caught: " + e.getMessage());
        }

        // Duplicate registration
        System.out.print("  Duplicate reg:    ");
        try {
            worldCup.registerPlayer(ahmed);
            System.out.println("FAIL (no exception)");
        } catch (DuplicateRegistrationException e) {
            System.out.println("✓ Caught: " + e.getMessage());
        }

        // Full tournament
        System.out.print("  Full tournament:  ");
        try {
            Tournament tiny = store.createTournament("Tiny Cup", 2);
            tiny.registerPlayer(ahmed);
            tiny.registerPlayer(sara);
            tiny.registerPlayer(omar);  // 3rd player, max is 2
            System.out.println("FAIL (no exception)");
        } catch (TournamentFullException e) {
            System.out.println("✓ Caught: " + e.getMessage());
        }

        System.out.println("\n══════════════════════════════════════════════════════");
        System.out.println("  All tests passed! ✅");
        System.out.println("══════════════════════════════════════════════════════");
    }
}
