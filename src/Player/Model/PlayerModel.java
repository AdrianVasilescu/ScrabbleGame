package Player.Model;

import Common.Tile;

import java.util.ArrayList;
import java.util.List;

/**
 * The Player Model - handles player side business
 */
public class PlayerModel {
    String playerId;
    List<Tile> tileStack;

    public PlayerModel(String playerId)
    {
        this.playerId = playerId;
        this.tileStack = new ArrayList<>();
    }

    public String getPlayerId()
    {
        return playerId;
    }

    public void setPlayerId(String playerId)
    {
        this.playerId = playerId;
    }

    public char[] getTileStackViewData()
    {
        char[] viewData = new char[tileStack.size()];

        for(int i = 0; i < tileStack.size(); i++)
        {
            viewData[i] = tileStack.get(i).getLetter();
        }

        return viewData;
    }

    public void addTiles(List<Tile> newTiles) {
        this.tileStack.addAll(newTiles);
    }
}
