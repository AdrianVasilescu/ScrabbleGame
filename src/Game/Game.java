package Game;

import Board.Controller.BoardController;
import Player.Controller.PlayerController;
import Player.Model.PlayerModel;

/**
 * The Game
 */
public class Game implements Runnable{
    /**
     * The board controller - handles board interactions
     */
    private BoardController boardController;

    /**
     * The player controller - handles player I/O
     */
    private PlayerController playerController;

    /**
     * Instantiates a new game
     * @param boardController the board controller
     * @param playerController the player controller
     */
    public Game(BoardController boardController, PlayerController playerController)
    {
        this.boardController = boardController;
        this.playerController = playerController;
    }

    @Override
    public void run()
    {
        PlayerController player1Controller = new PlayerController("1");
        PlayerController player2Controller = new PlayerController("2");

        player1Controller.receiveTiles(boardController.getTilesFromStack(7));
        player2Controller.receiveTiles(boardController.getTilesFromStack(7));

        player1Controller.getInputFromPlayer();
        player2Controller.getInputFromPlayer();
    }
}
