package Game;

import Board.Controller.BoardController;
import Player.Controller.PlayerController;

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
        System.out.println("Game ran!");
        // TODO
    }
}
