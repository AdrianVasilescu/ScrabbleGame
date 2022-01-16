package Player.Controller;

import Player.Model.PlayerModel;
import Player.View.PlayerView;

import java.util.HashMap;

/**
 * The Player controller - handles player inputs
 */
public class PlayerController {

    /**
     * The players models
     */
    HashMap<String, PlayerModel> players;

    /**
     * Current Player view
     */
    PlayerView playerView;

    /**
     * The id of the player currently playing it's turn
     */
    private String currentTurnPlayerId;

    /**
     * Handles input from a specific player
     */
    public void handleInputFromPlayer(String playerId)
    {
        // TODO
    }
}
