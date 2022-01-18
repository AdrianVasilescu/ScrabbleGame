package Player.Model;

import Common.Tile;

import java.util.ArrayList;
import java.util.List;

/**
 * The Player Model - handles player side business
 */
public class PlayerModel {
    /**
     * The player ID
     */
    private String playerId;

    /**
     * The list of available tiles
     */
    private List<Tile> tileStack;

    /**
     * Instantiates a new player
     * @param playerId
     */
    public PlayerModel(String playerId)
    {
        this.playerId = playerId;
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
    public void addTiles(List<Tile> newTiles) {
        this.tileStack.addAll(newTiles);
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
            viewData[i] = tileStack.get(i).getLetter();
        }

        return viewData;
    }
}
