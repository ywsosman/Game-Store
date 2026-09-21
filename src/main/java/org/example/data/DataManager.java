package org.example.data;

import org.example.enums.Genre;
import org.example.enums.OrderStatus;
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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Persistence manager handling serialization and deserialization
 * of store entities (Games, Players, Orders, Tournaments) to/from XML files using DOM XML parsers.
 */
public class DataManager {

    private static final String GAMES_FILE = "games.xml";
    private static final String PLAYERS_FILE = "players.xml";
    private static final String ORDERS_FILE = "orders.xml";
    private static final String TOURNAMENTS_FILE = "tournaments.xml";

    /**
     * Saves all entities from GameStore to XML files in the specified directory.
     *
     * @param store   the GameStore instance
     * @param dirPath the target directory path
     * @throws Exception if XML creation or file writing fails
     */
    public static void saveData(GameStore store, String dirPath) throws Exception {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        saveGames(store.getGames(), new File(dir, GAMES_FILE));
        savePlayers(store.getPlayers(), new File(dir, PLAYERS_FILE));
        saveOrders(store.getOrders(), new File(dir, ORDERS_FILE));
        saveTournaments(store.getTournaments(), new File(dir, TOURNAMENTS_FILE));
    }

    /**
     * Loads all entities from XML files in the specified directory into GameStore.
     *
     * @param store   the GameStore instance
     * @param dirPath the source directory path
     * @throws Exception if reading or parsing XML fails
     */
    public static void loadData(GameStore store, String dirPath) throws Exception {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            return;
        }

        File gamesFile = new File(dir, GAMES_FILE);
        File playersFile = new File(dir, PLAYERS_FILE);
        File ordersFile = new File(dir, ORDERS_FILE);
        File tournamentsFile = new File(dir, TOURNAMENTS_FILE);

        if (!gamesFile.exists() && !playersFile.exists() && !ordersFile.exists() && !tournamentsFile.exists()) {
            return;
        }

        store.clear();

        if (gamesFile.exists()) {
            loadGames(store, gamesFile);
        }
        if (playersFile.exists()) {
            loadPlayers(store, playersFile);
        }
        if (ordersFile.exists()) {
            loadOrders(store, ordersFile);
        }
        if (tournamentsFile.exists()) {
            loadTournaments(store, tournamentsFile);
        }

        store.syncNextIds();
    }

    // ==================== SAVE HELPERS ====================

    private static void saveGames(List<Game> games, File file) throws Exception {
        Document doc = createDocument();
        Element root = doc.createElement("games");
        doc.appendChild(root);

        for (Game game : games) {
            Element gameElem = doc.createElement("game");

            String type = "Generic";
            if (game instanceof PCGame) type = "PC";
            else if (game instanceof ConsoleGame) type = "Console";
            else if (game instanceof MobileGame) type = "Mobile";

            gameElem.setAttribute("type", type);
            appendElement(doc, gameElem, "id", String.valueOf(game.getId()));
            appendElement(doc, gameElem, "name", game.getName());
            appendElement(doc, gameElem, "price", String.valueOf(game.getPrice()));
            appendElement(doc, gameElem, "genre", game.getGenre().name());
            appendElement(doc, gameElem, "rating", String.valueOf(game.getRating()));

            if (game instanceof PCGame) {
                appendElement(doc, gameElem, "os", ((PCGame) game).getOperatingSystem());
            } else if (game instanceof ConsoleGame) {
                appendElement(doc, gameElem, "platform", ((ConsoleGame) game).getPlatform());
            } else if (game instanceof MobileGame) {
                appendElement(doc, gameElem, "isFreeToPlay", String.valueOf(((MobileGame) game).isFreeToPlay()));
            }

            root.appendChild(gameElem);
        }

        writeXml(doc, file);
    }

    private static void savePlayers(List<Player> players, File file) throws Exception {
        Document doc = createDocument();
        Element root = doc.createElement("players");
        doc.appendChild(root);

        for (Player player : players) {
            Element playerElem = doc.createElement("player");
            playerElem.setAttribute("type", player.getPlayerType());

            appendElement(doc, playerElem, "id", String.valueOf(player.getId()));
            appendElement(doc, playerElem, "name", player.getName());
            appendElement(doc, playerElem, "email", player.getEmail());
            appendElement(doc, playerElem, "discount", String.valueOf(player.getDiscount()));

            root.appendChild(playerElem);
        }

        writeXml(doc, file);
    }

    private static void saveOrders(List<Order> orders, File file) throws Exception {
        Document doc = createDocument();
        Element root = doc.createElement("orders");
        doc.appendChild(root);

        for (Order order : orders) {
            Element orderElem = doc.createElement("order");

            appendElement(doc, orderElem, "id", String.valueOf(order.getId()));
            appendElement(doc, orderElem, "playerId", String.valueOf(order.getPlayer().getId()));
            appendElement(doc, orderElem, "status", order.getStatus().name());

            Element gameIdsElem = doc.createElement("gameIds");
            for (Game g : order.getGames()) {
                appendElement(doc, gameIdsElem, "gameId", String.valueOf(g.getId()));
            }
            orderElem.appendChild(gameIdsElem);

            root.appendChild(orderElem);
        }

        writeXml(doc, file);
    }

    private static void saveTournaments(List<Tournament> tournaments, File file) throws Exception {
        Document doc = createDocument();
        Element root = doc.createElement("tournaments");
        doc.appendChild(root);

        for (Tournament t : tournaments) {
            Element tElem = doc.createElement("tournament");

            appendElement(doc, tElem, "id", String.valueOf(t.getId()));
            appendElement(doc, tElem, "name", t.getName());
            appendElement(doc, tElem, "maxPlayers", String.valueOf(t.getMaxPlayers()));
            appendElement(doc, tElem, "started", String.valueOf(t.isStarted()));
            appendElement(doc, tElem, "winnerId", t.getWinner() != null ? String.valueOf(t.getWinner().getId()) : "0");

            Element participantsElem = doc.createElement("participantIds");
            for (Player p : t.getParticipants()) {
                appendElement(doc, participantsElem, "participantId", String.valueOf(p.getId()));
            }
            tElem.appendChild(participantsElem);

            root.appendChild(tElem);
        }

        writeXml(doc, file);
    }

    // ==================== LOAD HELPERS ====================

    private static void loadGames(GameStore store, File file) throws Exception {
        Document doc = parseXml(file);
        NodeList nodes = doc.getElementsByTagName("game");

        for (int i = 0; i < nodes.getLength(); i++) {
            Element elem = (Element) nodes.item(i);
            String type = elem.getAttribute("type");

            int id = Integer.parseInt(getTextContent(elem, "id"));
            String name = getTextContent(elem, "name");
            double price = Double.parseDouble(getTextContent(elem, "price"));
            Genre genre = Genre.valueOf(getTextContent(elem, "genre"));
            double rating = Double.parseDouble(getTextContent(elem, "rating"));

            Game game;
            if ("PC".equalsIgnoreCase(type)) {
                String os = getTextContent(elem, "os");
                game = new PCGame(id, name, price, genre, rating, os);
            } else if ("Console".equalsIgnoreCase(type)) {
                String platform = getTextContent(elem, "platform");
                game = new ConsoleGame(id, name, price, genre, rating, platform);
            } else if ("Mobile".equalsIgnoreCase(type)) {
                boolean isFree = Boolean.parseBoolean(getTextContent(elem, "isFreeToPlay"));
                game = new MobileGame(id, name, price, genre, rating, isFree);
            } else {
                continue;
            }

            store.getGames().add(game);
        }
    }

    private static void loadPlayers(GameStore store, File file) throws Exception {
        Document doc = parseXml(file);
        NodeList nodes = doc.getElementsByTagName("player");

        for (int i = 0; i < nodes.getLength(); i++) {
            Element elem = (Element) nodes.item(i);
            String type = elem.getAttribute("type");

            int id = Integer.parseInt(getTextContent(elem, "id"));
            String name = getTextContent(elem, "name");
            String email = getTextContent(elem, "email");
            double discount = Double.parseDouble(getTextContent(elem, "discount"));

            Player player;
            if ("VIP".equalsIgnoreCase(type)) {
                player = new VIPPlayer(id, name, email, discount);
            } else {
                player = new RegularPlayer(id, name, email);
            }

            store.getPlayers().add(player);
        }
    }

    private static void loadOrders(GameStore store, File file) throws Exception {
        Document doc = parseXml(file);
        NodeList nodes = doc.getElementsByTagName("order");

        for (int i = 0; i < nodes.getLength(); i++) {
            Element elem = (Element) nodes.item(i);

            int id = Integer.parseInt(getTextContent(elem, "id"));
            int playerId = Integer.parseInt(getTextContent(elem, "playerId"));
            OrderStatus status = OrderStatus.valueOf(getTextContent(elem, "status"));

            Player player = store.findPlayerById(playerId);
            if (player == null) continue;

            Order order = new Order(id, player);
            order.setStatus(status);

            NodeList gameIdNodes = elem.getElementsByTagName("gameId");
            for (int j = 0; j < gameIdNodes.getLength(); j++) {
                int gameId = Integer.parseInt(gameIdNodes.item(j).getTextContent());
                Game game = store.findGameById(gameId);
                if (game != null) {
                    order.addGame(game);
                }
            }

            store.getOrders().add(order);
        }
    }

    private static void loadTournaments(GameStore store, File file) throws Exception {
        Document doc = parseXml(file);
        NodeList nodes = doc.getElementsByTagName("tournament");

        for (int i = 0; i < nodes.getLength(); i++) {
            Element elem = (Element) nodes.item(i);

            int id = Integer.parseInt(getTextContent(elem, "id"));
            String name = getTextContent(elem, "name");
            int maxPlayers = Integer.parseInt(getTextContent(elem, "maxPlayers"));
            boolean started = Boolean.parseBoolean(getTextContent(elem, "started"));
            int winnerId = Integer.parseInt(getTextContent(elem, "winnerId"));

            Tournament tournament = new Tournament(id, name, maxPlayers);
            tournament.setStarted(started);

            if (winnerId > 0) {
                Player winner = store.findPlayerById(winnerId);
                tournament.setWinner(winner);
            }

            NodeList pIdNodes = elem.getElementsByTagName("participantId");
            for (int j = 0; j < pIdNodes.getLength(); j++) {
                int pId = Integer.parseInt(pIdNodes.item(j).getTextContent());
                Player player = store.findPlayerById(pId);
                if (player != null) {
                    tournament.getParticipants().add(player);
                }
            }

            store.getTournaments().add(tournament);
        }
    }

    // ==================== UTILITY METHODS ====================

    private static Document createDocument() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.newDocument();
    }

    private static Document parseXml(File file) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(file);
    }

    private static void writeXml(Document doc, File file) throws Exception {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(file);
        transformer.transform(source, result);
    }

    private static void appendElement(Document doc, Element parent, String tagName, String textContent) {
        Element elem = doc.createElement(tagName);
        elem.setTextContent(textContent);
        parent.appendChild(elem);
    }

    private static String getTextContent(Element parent, String tagName) {
        NodeList list = parent.getElementsByTagName(tagName);
        if (list.getLength() > 0) {
            return list.item(0).getTextContent();
        }
        return "";
    }
}
