package org.example.ui;

import org.example.data.DataManager;
import org.example.enums.Genre;
import org.example.enums.OrderStatus;
import org.example.exception.GameStoreException;
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
import java.util.Scanner;

/**
 * Interactive terminal user interface for the Game Store application.
 * Provides menu navigation for catalog management, player registration & upgrades,
 * order processing, tournament simulation, and XML persistence.
 */
public class ConsoleMenu {

    private final GameStore store;
    private final Scanner scanner;
    private final String dataDir;

    public ConsoleMenu(GameStore store, String dataDir) {
        this.store = store;
        this.scanner = new Scanner(System.in);
        this.dataDir = dataDir;
    }

    /**
     * Starts the interactive main menu loop.
     */
    public void start() {
        boolean running = true;
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║            🎮 WELCOME TO GAME STORE MANAGEMENT CLI             ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");

        while (running) {
            printMainMenu();
            int choice = readInt("Select an option [0-5]: ");

            switch (choice) {
                case 1 -> handleCatalogMenu();
                case 2 -> handlePlayerMenu();
                case 3 -> handleOrderMenu();
                case 4 -> handleTournamentMenu();
                case 5 -> handlePersistenceMenu();
                case 0 -> {
                    System.out.println("\nAuto-saving store data to XML before exit...");
                    autoSave();
                    System.out.println("Thank you for using Game Store Management CLI! Goodbye! 👋");
                    running = false;
                }
                default -> System.out.println("⚠️ Invalid option. Please enter a number between 0 and 5.");
            }
        }
    }

    // ==================== MAIN MENU ====================

    private void printMainMenu() {
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║                          MAIN MENU                             ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        System.out.println("  1. 🎮 Game Catalog & Inventory");
        System.out.println("  2. 👤 Player Management & VIP Upgrades");
        System.out.println("  3. 🛒 Shopping Orders & Checkout");
        System.out.println("  4. 🏆 Tournaments & Match Simulation");
        System.out.println("  5. 💾 Save / Reload Store Data (XML)");
        System.out.println("  0. 🚪 Exit Application");
    }

    // ==================== CATALOG MENU ====================

    private void handleCatalogMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n── 🎮 GAME CATALOG MENU ─────────────────────────────────────────");
            System.out.println("  1. View All Games");
            System.out.println("  2. Search Games by Title");
            System.out.println("  3. Search Games by Genre");
            System.out.println("  4. Sort Catalog by Price (Low → High)");
            System.out.println("  5. Sort Catalog by Rating (High → Low)");
            System.out.println("  6. Add New Game (PC / Console / Mobile)");
            System.out.println("  0. ↩ Back to Main Menu");

            int choice = readInt("Select an option [0-6]: ");
            switch (choice) {
                case 1 -> printGameList(store.getGames(), "ALL CATALOG GAMES");
                case 2 -> {
                    String title = readNonEmptyString("Enter game title keyword: ");
                    printGameList(store.searchByName(title), "SEARCH RESULTS FOR '" + title + "'");
                }
                case 3 -> {
                    Genre genre = selectGenre();
                    printGameList(store.searchByGenre(genre), "GAMES IN GENRE '" + genre + "'");
                }
                case 4 -> printGameList(store.sortByPrice(), "CATALOG SORTED BY PRICE (LOW → HIGH)");
                case 5 -> printGameList(store.sortByRating(), "CATALOG SORTED BY RATING (HIGH → LOW)");
                case 6 -> addNewGame();
                case 0 -> back = true;
                default -> System.out.println("⚠️ Invalid option.");
            }
        }
    }

    private void addNewGame() {
        System.out.println("\n── 🎮 ADD NEW GAME ──────────────────────────────────────────────");
        String name = readNonEmptyString("Enter Game Title: ");
        double price = readDouble("Enter Price ($): ");
        Genre genre = selectGenre();
        double rating = readDouble("Enter Rating (0.0 to 5.0): ");

        System.out.println("\nSelect Platform Type:");
        System.out.println("  1. PC Game");
        System.out.println("  2. Console Game");
        System.out.println("  3. Mobile Game");
        int typeChoice = readInt("Select platform [1-3]: ");

        try {
            Game game;
            if (typeChoice == 1) {
                String os = readNonEmptyString("Enter Operating System (e.g. Windows, macOS, Linux): ");
                game = new PCGame(0, name, price, genre, rating, os);
            } else if (typeChoice == 2) {
                String platform = readNonEmptyString("Enter Console Platform (e.g. PlayStation 5, Xbox Series X, Nintendo Switch): ");
                game = new ConsoleGame(0, name, price, genre, rating, platform);
            } else if (typeChoice == 3) {
                boolean isFree = readBoolean("Is this game Free-to-Play? (y/n): ");
                game = new MobileGame(0, name, isFree ? 0.0 : price, genre, rating, isFree);
            } else {
                System.out.println("⚠️ Invalid platform choice. Creation cancelled.");
                return;
            }

            store.addGame(game);
            System.out.println("✓ Successfully added game '" + name + "' (ID #" + game.getId() + ") to catalog!");
        } catch (GameStoreException e) {
            System.out.println("⚠️ Error adding game: " + e.getMessage());
        }
    }

    // ==================== PLAYER MENU ====================

    private void handlePlayerMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n── 👤 PLAYER MANAGEMENT MENU ────────────────────────────────────");
            System.out.println("  1. View All Registered Players");
            System.out.println("  2. Register New Player (Regular / VIP)");
            System.out.println("  3. Upgrade Regular Player to VIP");
            System.out.println("  4. Reconfigure VIP Discount Rate");
            System.out.println("  0. ↩ Back to Main Menu");

            int choice = readInt("Select an option [0-4]: ");
            switch (choice) {
                case 1 -> printPlayerList(store.getPlayers(), "REGISTERED PLAYERS");
                case 2 -> registerNewPlayer();
                case 3 -> upgradePlayerToVIP();
                case 4 -> reconfigureVIPDiscount();
                case 0 -> back = true;
                default -> System.out.println("⚠️ Invalid option.");
            }
        }
    }

    private void registerNewPlayer() {
        System.out.println("\n── 👤 REGISTER NEW PLAYER ─────────────────────────────────────");
        String name = readNonEmptyString("Enter Player Name: ");
        String email = readNonEmptyString("Enter Email Address: ");

        System.out.println("\nSelect Player Tier:");
        System.out.println("  1. Regular Player (Standard account, 0% discount)");
        System.out.println("  2. VIP Player     (Premium account, 20% default discount)");
        int tierChoice = readInt("Select tier [1-2]: ");

        Player newPlayer;
        if (tierChoice == 2) {
            double customDiscount = readDouble("Enter VIP discount rate (e.g. 0.20 for 20%, 0.30 for 30%): ");
            newPlayer = new VIPPlayer(0, name, email, customDiscount);
        } else {
            newPlayer = new RegularPlayer(0, name, email);
        }

        store.addPlayer(newPlayer);
        System.out.println("✓ Successfully registered " + newPlayer.getPlayerType() + " Player '" + name + "' (ID #" + newPlayer.getId() + ")!");
    }

    private void upgradePlayerToVIP() {
        System.out.println("\n── 👤 UPGRADE PLAYER TO VIP ────────────────────────────────────");
        printPlayerList(store.getPlayers(), "SELECT PLAYER TO UPGRADE");
        int playerId = readInt("Enter Player ID to upgrade: ");

        Player existing = store.findPlayerById(playerId);
        if (existing == null) {
            System.out.println("⚠️ Player with ID #" + playerId + " not found.");
            return;
        }

        if (existing instanceof VIPPlayer) {
            System.out.println("ℹ️ Player '" + existing.getName() + "' is already a VIP Player.");
            return;
        }

        double discount = readDouble("Enter VIP discount rate for " + existing.getName() + " (e.g. 0.20 for 20%): ");
        VIPPlayer upgraded = new VIPPlayer(existing.getId(), existing.getName(), existing.getEmail(), discount);

        // Replace in store list
        int index = store.getPlayers().indexOf(existing);
        store.getPlayers().set(index, upgraded);

        System.out.println("✓ Successfully upgraded '" + existing.getName() + "' to VIP Player with " + (int)(discount * 100) + "% discount!");
    }

    private void reconfigureVIPDiscount() {
        System.out.println("\n── 👤 RECONFIGURE VIP DISCOUNT ─────────────────────────────────");
        printPlayerList(store.getPlayers(), "SELECT VIP PLAYER");
        int playerId = readInt("Enter VIP Player ID: ");

        Player player = store.findPlayerById(playerId);
        if (player == null) {
            System.out.println("⚠️ Player with ID #" + playerId + " not found.");
            return;
        }

        try {
            double newDiscount = readDouble("Enter new discount rate (e.g. 0.25 for 25%): ");
            player.setDiscount(newDiscount);
            System.out.println("✓ Updated discount for " + player.getName() + " (" + player.getPlayerType() + ") to " + (int)(newDiscount * 100) + "%!");
        } catch (GameStoreException e) {
            System.out.println("⚠️ Exception: " + e.getMessage());
        }
    }

    // ==================== ORDER MENU ====================

    private void handleOrderMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n── 🛒 SHOPPING ORDERS MENU ─────────────────────────────────────");
            System.out.println("  1. View All Orders");
            System.out.println("  2. Create New Shopping Order");
            System.out.println("  3. Checkout Pending Order");
            System.out.println("  4. Cancel Order");
            System.out.println("  0. ↩ Back to Main Menu");

            int choice = readInt("Select an option [0-4]: ");
            switch (choice) {
                case 1 -> printOrderList();
                case 2 -> createNewOrder();
                case 3 -> checkoutOrder();
                case 4 -> cancelOrder();
                case 0 -> back = true;
                default -> System.out.println("⚠️ Invalid option.");
            }
        }
    }

    private void createNewOrder() {
        System.out.println("\n── 🛒 CREATE NEW ORDER ──────────────────────────────────────────");
        if (store.getPlayers().isEmpty()) {
            System.out.println("⚠️ No players registered. Please register a player first.");
            return;
        }
        if (store.getGames().isEmpty()) {
            System.out.println("⚠️ Game catalog is empty. Please add games to catalog first.");
            return;
        }

        printPlayerList(store.getPlayers(), "SELECT CUSTOMER");
        int playerId = readInt("Enter Player ID: ");

        Player player = store.findPlayerById(playerId);
        if (player == null) {
            System.out.println("⚠️ Player not found.");
            return;
        }

        Order order = store.createOrder(player);
        System.out.println("\n[Order #" + order.getId() + " Created for " + player.getName() + " (" + player.getPlayerType() + ")]");

        boolean addingGames = true;
        while (addingGames) {
            printGameList(store.getGames(), "AVAILABLE CATALOG GAMES");
            int gameId = readInt("Enter Game ID to add to cart (0 to finish adding): ");

            if (gameId == 0) {
                addingGames = false;
            } else {
                Game game = store.findGameById(gameId);
                if (game != null) {
                    order.addGame(game);
                    System.out.println("  ✓ Added '" + game.getName() + "' ($" + String.format("%.2f", game.getPrice()) + ") to cart!");
                } else {
                    System.out.println("  ⚠️ Game ID #" + gameId + " not found.");
                }
            }
        }

        System.out.println("\n── ORDER BREAKDOWN ─────────────────────────────────────────────");
        System.out.println(order);

        boolean checkoutNow = readBoolean("\nWould you like to checkout this order now? (y/n): ");
        if (checkoutNow) {
            try {
                order.checkout();
                System.out.println("✓ Success: Order #" + order.getId() + " checked out! Status: " + order.getStatus().getDisplayName());
            } catch (GameStoreException e) {
                System.out.println("⚠️ Checkout Exception: " + e.getMessage());
            }
        } else {
            System.out.println("ℹ️ Order #" + order.getId() + " saved as PENDING.");
        }
    }

    private void checkoutOrder() {
        System.out.println("\n── 🛒 CHECKOUT PENDING ORDER ───────────────────────────────────");
        printOrderList();
        int orderId = readInt("Enter Order ID to checkout: ");

        Order order = store.findOrderById(orderId);
        if (order == null) {
            System.out.println("⚠️ Order #" + orderId + " not found.");
            return;
        }

        try {
            order.checkout();
            System.out.println("✓ Order #" + order.getId() + " checked out! Status: " + order.getStatus().getDisplayName());
        } catch (GameStoreException e) {
            System.out.println("⚠️ Exception: " + e.getMessage());
        }
    }

    private void cancelOrder() {
        System.out.println("\n── 🛒 CANCEL ORDER ─────────────────────────────────────────────");
        printOrderList();
        int orderId = readInt("Enter Order ID to cancel: ");

        Order order = store.findOrderById(orderId);
        if (order == null) {
            System.out.println("⚠️ Order #" + orderId + " not found.");
            return;
        }

        order.cancel();
        System.out.println("✓ Order #" + order.getId() + " cancelled. Status: " + order.getStatus().getDisplayName());
    }

    // ==================== TOURNAMENT MENU ====================

    private void handleTournamentMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n── 🏆 TOURNAMENT SYSTEM MENU ───────────────────────────────────");
            System.out.println("  1. View All Tournaments");
            System.out.println("  2. Create New Tournament");
            System.out.println("  3. Register Player for Tournament");
            System.out.println("  4. Start & Simulate Tournament");
            System.out.println("  5. View Tournament Match Log");
            System.out.println("  0. ↩ Back to Main Menu");

            int choice = readInt("Select an option [0-5]: ");
            switch (choice) {
                case 1 -> printTournamentList();
                case 2 -> createTournament();
                case 3 -> registerPlayerToTournament();
                case 4 -> startTournament();
                case 5 -> viewMatchLog();
                case 0 -> back = true;
                default -> System.out.println("⚠️ Invalid option.");
            }
        }
    }

    private void createTournament() {
        System.out.println("\n── 🏆 CREATE NEW TOURNAMENT ───────────────────────────────────");
        String name = readNonEmptyString("Enter Tournament Name: ");
        int maxPlayers = readInt("Enter Max Players Capacity (min 2): ");

        if (maxPlayers < 2) {
            System.out.println("⚠️ Tournament must hold at least 2 players.");
            return;
        }

        Tournament t = store.createTournament(name, maxPlayers);
        System.out.println("✓ Created Tournament '" + t.getName() + "' (ID #" + t.getId() + ", Max Capacity: " + maxPlayers + ")!");
    }

    private void registerPlayerToTournament() {
        System.out.println("\n── 🏆 REGISTER PLAYER TO TOURNAMENT ───────────────────────────");
        if (store.getTournaments().isEmpty()) {
            System.out.println("⚠️ No tournaments created yet.");
            return;
        }

        printTournamentList();
        int tId = readInt("Enter Tournament ID: ");
        Tournament t = store.findTournamentById(tId);

        if (t == null) {
            System.out.println("⚠️ Tournament not found.");
            return;
        }

        printPlayerList(store.getPlayers(), "AVAILABLE PLAYERS");
        int pId = readInt("Enter Player ID to register: ");
        Player p = store.findPlayerById(pId);

        if (p == null) {
            System.out.println("⚠️ Player not found.");
            return;
        }

        try {
            t.registerPlayer(p);
            System.out.println("✓ Registered player '" + p.getName() + "' into tournament '" + t.getName() + "'!");
        } catch (GameStoreException e) {
            System.out.println("⚠️ Exception: " + e.getMessage());
        }
    }

    private void startTournament() {
        System.out.println("\n── 🏆 START & SIMULATE TOURNAMENT ──────────────────────────────");
        printTournamentList();
        int tId = readInt("Enter Tournament ID to start: ");
        Tournament t = store.findTournamentById(tId);

        if (t == null) {
            System.out.println("⚠️ Tournament not found.");
            return;
        }

        try {
            t.startTournament();
            System.out.println("\n" + t.getMatchLog());
            System.out.println("🎉 Tournament simulation complete! Winner: " + t.getWinner().getName() + "!");
        } catch (Exception e) {
            System.out.println("⚠️ Exception: " + e.getMessage());
        }
    }

    private void viewMatchLog() {
        System.out.println("\n── 🏆 VIEW MATCH LOG ───────────────────────────────────────────");
        printTournamentList();
        int tId = readInt("Enter Tournament ID: ");
        Tournament t = store.findTournamentById(tId);

        if (t == null) {
            System.out.println("⚠️ Tournament not found.");
            return;
        }

        if (!t.isStarted()) {
            System.out.println("ℹ️ Tournament '" + t.getName() + "' has not been started yet.");
        } else {
            System.out.println("\n" + t.getMatchLog());
        }
    }

    // ==================== PERSISTENCE MENU ====================

    private void handlePersistenceMenu() {
        System.out.println("\n── 💾 XML DATA PERSISTENCE ─────────────────────────────────────");
        System.out.println("  1. Save Current State to XML ('" + dataDir + "')");
        System.out.println("  2. Reload State from XML ('" + dataDir + "')");
        System.out.println("  0. ↩ Back to Main Menu");

        int choice = readInt("Select an option [0-2]: ");
        if (choice == 1) {
            try {
                DataManager.saveData(store, dataDir);
                System.out.println("✓ Store state saved successfully to '" + dataDir + "' directory!");
            } catch (Exception e) {
                System.out.println("⚠️ Save Error: " + e.getMessage());
            }
        } else if (choice == 2) {
            try {
                DataManager.loadData(store, dataDir);
                System.out.println("✓ Store state reloaded successfully from '" + dataDir + "' directory!");
            } catch (Exception e) {
                System.out.println("⚠️ Load Error: " + e.getMessage());
            }
        }
    }

    private void autoSave() {
        try {
            DataManager.saveData(store, dataDir);
            System.out.println("✓ Auto-save complete (" + store.getGames().size() + " games, "
                    + store.getPlayers().size() + " players, "
                    + store.getOrders().size() + " orders, "
                    + store.getTournaments().size() + " tournaments saved to XML).");
        } catch (Exception e) {
            System.out.println("⚠️ Auto-save error: " + e.getMessage());
        }
    }

    // ==================== PRINT HELPERS ====================

    private void printGameList(List<Game> games, String header) {
        System.out.println("\n── " + header + " ──────────────────────────────────────────");
        if (games.isEmpty()) {
            System.out.println("  (No games found)");
            return;
        }
        for (Game g : games) {
            System.out.println("  " + g);
        }
    }

    private void printPlayerList(List<Player> players, String header) {
        System.out.println("\n── " + header + " ──────────────────────────────────────────");
        if (players.isEmpty()) {
            System.out.println("  (No players found)");
            return;
        }
        for (Player p : players) {
            System.out.println("  " + p);
        }
    }

    private void printOrderList() {
        System.out.println("\n── ALL ORDERS ──────────────────────────────────────────────────");
        List<Order> orders = store.getOrders();
        if (orders.isEmpty()) {
            System.out.println("  (No orders created)");
            return;
        }
        for (Order o : orders) {
            System.out.println(o);
            System.out.println("────────────────────────────────────────────────────────────────");
        }
    }

    private void printTournamentList() {
        System.out.println("\n── ALL TOURNAMENTS ─────────────────────────────────────────────");
        List<Tournament> tournaments = store.getTournaments();
        if (tournaments.isEmpty()) {
            System.out.println("  (No tournaments created)");
            return;
        }
        for (Tournament t : tournaments) {
            System.out.println(t);
        }
    }

    // ==================== SAFE INPUT READERS ====================

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("  ⚠️ Invalid input. Please enter a valid integer.");
            }
        }
    }

    private double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("  ⚠️ Invalid input. Please enter a valid decimal number.");
            }
        }
    }

    private String readNonEmptyString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("  ⚠️ Input cannot be blank.");
        }
    }

    private boolean readBoolean(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("y") || input.equals("yes") || input.equals("true")) {
                return true;
            } else if (input.equals("n") || input.equals("no") || input.equals("false")) {
                return false;
            }
            System.out.println("  ⚠️ Please enter 'y' or 'n'.");
        }
    }

    private Genre selectGenre() {
        System.out.println("Available Genres:");
        Genre[] genres = Genre.values();
        for (int i = 0; i < genres.length; i++) {
            System.out.println("  " + (i + 1) + ". " + genres[i]);
        }
        while (true) {
            int choice = readInt("Select genre [1-" + genres.length + "]: ");
            if (choice >= 1 && choice <= genres.length) {
                return genres[choice - 1];
            }
            System.out.println("  ⚠️ Invalid selection.");
        }
    }
}
