package main.Player.Controller;

import main.Common.PlayerInputService;
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
    private PlayerModel playerModel;

    /**
     * Current java.Player view
     */
    private PlayerView playerView;

    private PlayerInputService playerInputService;

    /**
     * Instantiates a new java.Player Controller
     */
    public PlayerController()
    {
        this.playerView = new PlayerView();
        this.playerModel = new PlayerModel();
        this.playerInputService = new PlayerInputService();
    }

    public void receiveTiles(List<Tile> newTiles)
    {
        playerModel.addTiles(newTiles);
        mapModelDataToViewData();
    }

    /**
     * Returns the player input asked in prompt
     * @param prompt
     * @return
     */
    public String getInput(String prompt) {
        return playerInputService.getInput(prompt);
    }

    /**
     * Maps the data from the board model to the board view
     */
    private void mapModelDataToViewData()
    {
        playerView.updateView(playerModel.getPlayerId(), playerModel.getTileStackViewData());
    }
}
