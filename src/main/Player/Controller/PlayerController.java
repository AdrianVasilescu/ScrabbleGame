package main.Player.Controller;

import main.Common.PlayerInteractor;
import main.Common.Tile;
import main.Player.Model.PlayerModel;

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
     * The player UI
     */
    private PlayerInteractor playerInteractor;

    /**
     * Instantiates a new Player Controller
     * @param playerInteractor
     */
    public PlayerController(PlayerInteractor playerInteractor)
    {
        this.playerModel = new PlayerModel();
        this.playerInteractor = playerInteractor;
    }

    public void receiveTiles(List<Tile> newTiles)
    {
        playerModel.addTiles(newTiles);
        mapModelDataToViewData();
    }

    public void removeTiles(List<Tile> concernedTiles) {
        playerModel.removeTiles(concernedTiles);
        mapModelDataToViewData();
    }

    /**
     * Returns the player input
     * @return
     */
    public String getInput() throws InterruptedException {
        return playerInteractor.getInput();
    }

    /**
     * Maps the data from the board model to the board view
     */
    private void mapModelDataToViewData()
    {
        playerInteractor.updateTiles(playerModel.getTileStackViewData());
    }

    /**
     * Prints a message
     * @param s the message
     */
    public void printMessage(String s) {
        playerInteractor.printMessage(s);
    }

    /**
     * Prints a message prompted as from the server
     * @param s the message
     */
    public void printMessageFromServer(String s) {
        printMessage("SERVER:" + s);
    }
}
