package main.Player.Model;

import main.Common.Tile;

import java.util.ArrayList;
import java.util.List;

/**
 * The java.Player Model - handles player side business
 */
public class PlayerModel {
    /**
     * The player ID
     */
    private String playerId;

    /**
     * The list of available tiles
     */
    private List<Character> tileStack;

    /**
     * Instantiates a new player
     */
    public PlayerModel()
    {
        this.tileStack = new ArrayList<>();
    }

    /**
     * Gets player id
     * @return the player id
     */
    public String getPlayerId()
    {
        return playerId;
    }

    /**
     * Sets the player id
     * @param playerId
     */
    public void setPlayerId(String playerId)
    {
        this.playerId = playerId;
    }

    /**
     * Adds new tiles to stack
     * @param newTiles
     */
    public void addTiles(List<Tile> newTiles)
    {
        for(Tile t : newTiles)
        {
            this.tileStack.add(t.getLetter());
        }
    }

    /**
     * Gets the tile stack view data
     * @return the tile stack view data
     */
    public char[] getTileStackViewData()
    {
        char[] viewData = new char[tileStack.size()];

        for(int i = 0; i < tileStack.size(); i++)
        {
            viewData[i] = tileStack.get(i);
        }

        return viewData;
    }

    public void removeTiles(List<Tile> concernedTiles) {
        for(Tile t : concernedTiles)
        {
            this.tileStack.remove(t.getLetter());
        }
    }
}
