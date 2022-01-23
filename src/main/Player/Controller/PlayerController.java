package main.Player.Controller;

import main.Common.Tile;
import main.Player.Model.PlayerModel;
import main.Player.View.PlayerView;

import java.util.ArrayList;
import java.util.List;

/**
 * The java.Player controller - handles player inputs
 */
public class PlayerController {

    /**
     * The player model
     */
    PlayerModel playerModel;

    /**
     * Current java.Player view
     */
    PlayerView playerView;

    /**
     * Instantiates a new java.Player Controller
     */
    public PlayerController(String id)
    {
        this.playerView = new PlayerView();
        this.playerModel = new PlayerModel(id);
    }

    public void receiveTiles(List<Tile> newTiles)
    {
        playerModel.addTiles(newTiles);
        mapModelDataToViewData();
    }

    /**
     * Handles input from player
     */
    public List<Tile> getInputFromPlayer()
    {
        playerView.printPlayer();
        ArrayList<Tile> input = new ArrayList<>();

        System.out.println("Waiting input :");
        // TODO

        return input;
    }

    /**
     * Starts playing
     */
    public void startGame()
    {
        // TODO
    }

    /**
     * Maps the data from the board model to the board view
     */
    private void mapModelDataToViewData()
    {
        playerView.updateView(playerModel.getPlayerId(), playerModel.getTileStackViewData());
    }
}
