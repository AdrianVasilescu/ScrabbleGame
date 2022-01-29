package main.Game;

import main.Common.Tile;

import java.io.IOException;
import java.util.List;

public class PlayerSession {
    private PlayerConnector playerConnector;
    private String name;
    private Thread playerThread;
    private String tiles;
    private int score;
    private boolean disconnected = false;

    public PlayerSession(PlayerConnector playerConnector) {
        this.playerConnector = playerConnector;
        this.score = 0;
        this.tiles = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNextMessage() throws IOException {
        return playerConnector.getNextMessage();
    }

    public void sendMessage(String message) {
        playerConnector.sendMessage(message);
    }

    public Thread getPlayerThread() {
        return playerThread;
    }

    public void setPlayerThread(Thread playerThread) {
        this.playerThread = playerThread;
    }

    public void endSession() {
        this.score = -1;
        this.playerThread.interrupt();
        try {
            this.playerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void addScore(int score)
    {
        this.score += score;
    }

    public int getScore() {
        return score;
    }

    public int usedTiles(List<Tile> playedTiles, char[][] board) {
        int count = 0;
        String tileClone = tiles;
        for(Tile t : playedTiles)
        {
            if(board[t.getRow()][t.getColumn()] != t.getLetter()) {
                String tileString = String.valueOf(t.getLetter());
                if (Character.isLowerCase(t.getLetter())) {
                    tileString = "!";
                }

                if (tileClone.contains(tileString)) {
                    tileClone = tileClone.replaceFirst(tileString, "");
                    count++;
                }
                else
                {
                    return -1;
                }
            }
        }
        return count;
    }

    public boolean hasTiles(String swappedTiles) {
        for(char c : swappedTiles.toCharArray())
        {
            String tileString = String.valueOf(c);
            if (Character.isLowerCase(c)) {
                tileString = "!";
            }

            if (!tiles.contains(tileString)) {
                return false;
            }
        }
        return true;
    }

    public void removeTiles(String requestedTiles) {
        for(char c : requestedTiles.toCharArray())
        {
            String tile = String.valueOf(c);
            if(Character.isLowerCase(c))
            {
                tile = "!";
            }
            this.tiles = tiles.replaceFirst(tile, "");
        }
    }

    public void addTiles(String newTiles)
    {
        this.tiles += newTiles;
    }

    public boolean isDisconnected() {
        return disconnected;
    }

    public void disconnect()
    {
        this.disconnected = true;
    }
}
