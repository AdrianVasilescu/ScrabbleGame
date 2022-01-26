package main.Game;

public class PlayerSession {
    private PlayerConnector playerConnector;
    private String name;
    private Thread playerThread;
    private String tiles;
    private int score;

    public PlayerSession(PlayerConnector playerConnector) {
        this.playerConnector = playerConnector;
        this.score = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNextMessage() {
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

    public boolean hasTiles(String requestedTiles) {
        for(char c : requestedTiles.toCharArray())
        {
            String tile = String.valueOf(c);
            if(Character.isLowerCase(c))
            {
                tile = "!";
            }

            if(!tiles.contains(tile))
            {
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
}
