package main.Game;

import main.Common.Tile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The player session class - used on the server to keep track of all connections
 */
public class PlayerSession {
    /**
     * The socket connector
     */
    private SocketConnector socketConnector;
    /**
     * The player name
     */
    private String name = "";
    /**
     * The thread on which this player is being listened to
     */
    private Thread playerThread;
    /**
     * The tiles of the player
     */
    private String tiles;
    /**
     * The score
     */
    private int score;
    /**
     * A flag to know this player has disconnected
     */
    private boolean disconnected = false;

    /**
     * Creates a player session
     * @param socketConnector the socket connector
     */
    public PlayerSession(SocketConnector socketConnector) {
        this.socketConnector = socketConnector;
        this.score = 0;
        this.tiles = "";
    }

    /**
     * Gets the name of the player
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the next message from this player
     * @return the message
     * @throws IOException
     */
    public String getNextMessage() throws IOException {
        return socketConnector.getNextMessage();
    }

    /**
     * Send a message to this player
     * @param message the message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException {
        socketConnector.sendMessage(message);
    }

    /**
     * Assign a player thread to the session
     * @param playerThread the player thread
     */
    public void setPlayerThread(Thread playerThread) {
        this.playerThread = playerThread;
    }

    /**
     * Ends the session
     */
    public void endSession() {
        this.score = 0;
        this.playerThread.interrupt();
        try {
            this.playerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds score
     * @param score
     */
    public void addScore(int score)
    {
        this.score += score;
    }

    /**
     * Gets the total score
     * @return the total score
     */
    public int getScore() {
        return score;
    }

    /**
     * Checks if the player has some tiles
     * @param tiles the tiles
     * @return
     */
    public boolean hasTiles(List<Tile> tiles)
    {
        String tileString = "";
        for(Tile t : tiles)
        {
            tileString += String.valueOf(t.getLetter());
        }

        return hasTiles(tileString);
    }

    /**
     * Checks if a player has the tiles received as string
     * @param swappedTiles the swapped tiles
     * @return
     */
    public boolean hasTiles(String swappedTiles) {
        String tmpTiles = tiles;
        for(char c : swappedTiles.toCharArray())
        {
            String tileString = String.valueOf(c);
            if (Character.isLowerCase(c)) {
                tileString = "!";
            }

            if (!tmpTiles.contains(tileString)) {
                return false;
            }
            tmpTiles = tmpTiles.replaceFirst(tileString, "");
        }
        return true;
    }

    /**
     * Remove tiles
     * @param requestedTiles
     */
    public void removeTiles(List<Tile> requestedTiles) {
        for(Tile t : requestedTiles)
        {
            String tile = String.valueOf(t.getLetter());
            if(Character.isLowerCase(t.getLetter()))
            {
                tile = "!";
            }
            this.tiles = tiles.replaceFirst(tile, "");
        }
    }

    /**
     * Adds new tiles
     * @param newTiles
     */
    public void addTiles(String newTiles)
    {
        this.tiles += newTiles;
    }

    /**
     * Tells whether this player has disconnected
     * @return
     */
    public boolean isDisconnected() {
        return disconnected;
    }

    /**
     * Disconnects the player
     */
    public void disconnect()
    {
        this.disconnected = true;
    }

    /**
     * Gets all the tiles of this player
     * @return
     */
    public List<Character> getTiles() {
        List<Character> ret = new ArrayList<>();
        for(char  c : tiles.toCharArray())
        {
            ret.add(c);
        }
        return ret;
    }
}
