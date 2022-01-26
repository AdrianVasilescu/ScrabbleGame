package main.Player.Controller;

import main.Common.PlayerInteractor;
import main.Common.Tile;
import main.Player.Model.PlayerModel;
import main.Player.View.PlayerView;

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

    private PlayerInteractor playerInteractor;

    /**
     * Instantiates a new Player Controller
     * @param playerInteractor
     */
    public PlayerController(PlayerInteractor playerInteractor)
    {
        this.playerView = new PlayerView();
        this.playerModel = new PlayerModel();
        this.playerInteractor = playerInteractor;
    }

    public void receiveTiles(List<Tile> newTiles)
    {
        playerModel.addTiles(newTiles);
        mapModelDataToViewData();
    }

    /**
     * Returns the player input
     * @return
     */
    public String getInput() {
        return playerInteractor.getInput();
    }

    /**
     * Maps the data from the board model to the board view
     */
    private void mapModelDataToViewData()
    {
        playerView.updateView(playerModel.getPlayerId(), playerModel.getTileStackViewData());
    }

    public void printMessage(String s) {
        playerInteractor.printMessage(s);
    }

    public void printMessageFromServer(String s) {
        printMessage("SERVER:" + s);
    }
}
