package main.Game;

import main.Board.Controller.BoardController;
import main.Common.FileInputReader;
import main.Exceptions.InitialWordNotOnCenterException;
import main.Exceptions.InvalidInputException;
import main.Exceptions.InvalidMoveException;
import main.Exceptions.NotEnoughPlayersException;
import main.TilePool.Controller.TilePoolController;
import main.Player.Controller.PlayerController;

/**
 * The java.Game
 */
public class Game implements Runnable{

    /**
     * Instantiates a new game
     */
    public Game()
    {
    }

    @Override
    public void run()
    {
        try {
            BoardController boardController = new BoardController();
            PlayerController player1Controller = new PlayerController(boardController.addPlayer());
            PlayerController player2Controller = new PlayerController(boardController.addPlayer());
            boardController.startGame();
            TilePoolController tilePoolController = new TilePoolController();

            player1Controller.receiveTiles(tilePoolController.getTilesFromPool(7));
            player2Controller.receiveTiles(tilePoolController.getTilesFromPool(7));

            boardController.handleTilesFromPlayer(FileInputReader.getInput(1));

        } catch (NotEnoughPlayersException | InvalidInputException | InvalidMoveException | InitialWordNotOnCenterException e) {
            e.printStackTrace();
        }
    }
}
