package main.Board.Controller;

import lib.Protocol;
import main.Board.Model.BoardServerModel;
import main.Board.Model.BoardState;
import main.Board.View.BoardView;
import main.Common.Tile;
import main.Exceptions.InitialWordNotOnCenterException;
import main.Exceptions.InvalidInputException;
import main.Exceptions.InvalidMoveException;

import java.util.List;

/**
 * The java.Board Controller - handles interactions
 */
public class    BoardController {
    /**
     * The board model
     */
    private BoardServerModel boardServerModel;

    /**
     * The board view
     */
    private BoardView boardView;

    /**
     * Instantiates a board controller
     */
    public BoardController()
    {
        this.boardServerModel = new BoardServerModel();
        this.boardView = new BoardView();
        this.mapModelDataToViewData();
    }

    /**
     * Prints the board
     */
    public char[][] getBoard(){
        return boardView.getBoard();
    }

    /**
     * Handles input tiles from a player
     * @param tiles the tiles
     * @return the score
     * @throws InvalidInputException if the input is not valid
     * @throws InvalidMoveException if the move cannot be done on the current state of the board
     * @throws InitialWordNotOnCenterException if the first word placed on board does not cover the center tile
     */
    public int handleMove(List<Tile> tiles)
            throws InvalidMoveException, InvalidInputException, InitialWordNotOnCenterException {
        validateInput(tiles);
        int score = boardServerModel.handleTiles(tiles);
        mapModelDataToViewData();

        return score;
    }

    /**
     * Validates the input syntax and value range
     * @param tiles
     * @throws InvalidInputException whether the input is valid or not
     */
    private void validateInput(List<Tile> tiles) throws InvalidInputException {
        for(Tile tile : tiles)
        {
            if(!Character.isAlphabetic(tile.getLetter()) ||
                    (tile.getRow() < 0 || tile.getRow() > 14) ||
                    (tile.getColumn() < 0 || tile.getColumn() > 14)) {
                throw new InvalidInputException(Protocol.Error.E005);
            }
        }
    }

    /**
     * Maps the data from the board model to the board view
     */
    public void mapModelDataToViewData()
    {
        char[][] boardViewData = boardServerModel.getBoardViewData();

        boardView.updateView(boardViewData);
    }

    /**
     * Produces a snapshot of the current board state
     * @return the snapshot
     */
    public BoardState getBoardStateSnapshot() {
        return boardServerModel.getBoardStateSnapshot();
    }
}
