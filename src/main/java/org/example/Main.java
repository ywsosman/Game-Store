package org.example;

import org.example.data.DataManager;
import org.example.service.GameStore;
import org.example.ui.ConsoleMenu;

/**
 * Application entry point for the Game Store Management CLI.
 * Restores store state from XML files on boot and launches the interactive ConsoleMenu.
 */
public class Main {

    private static final String DATA_DIRECTORY = "data";

    public static void main(String[] args) {
        GameStore store = new GameStore();

        System.out.println("[SYSTEM] Initializing Game Store application...");
        System.out.println("[SYSTEM] Checking for existing persistent data in '" + DATA_DIRECTORY + "'...");

        try {
            DataManager.loadData(store, DATA_DIRECTORY);
            if (!store.getGames().isEmpty() || !store.getPlayers().isEmpty()) {
                System.out.println("[SYSTEM] Loaded " + store.getGames().size() + " games, "
                        + store.getPlayers().size() + " players, "
                        + store.getOrders().size() + " orders, and "
                        + store.getTournaments().size() + " tournaments from XML!");
            } else {
                System.out.println("[SYSTEM] No prior XML data found. Initialized clean store.");
            }
        } catch (Exception e) {
            System.out.println("[SYSTEM] Warning: Error loading XML data: " + e.getMessage());
        }

        // Launch interactive terminal interface
        ConsoleMenu menu = new ConsoleMenu(store, DATA_DIRECTORY);
        menu.start();
    }
}
