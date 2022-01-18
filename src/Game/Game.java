package Game;

import Board.Controller.BoardController;
import Common.FileInputReader;
import Common.Tile;
import Exceptions.InvalidInputException;
import Exceptions.InvalidMoveException;
import Exceptions.NotEnoughPlayersException;
import Player.Controller.PlayerController;
import TilePool.Controller.TilePoolController;

import java.util.Arrays;
import java.util.List;

/**
 * The Game
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
        } catch (NotEnoughPlayersException | InvalidInputException | InvalidMoveException e) {
            e.printStackTrace();
        }
    }
}
