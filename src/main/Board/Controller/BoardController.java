package main.Board.Controller;

import main.Board.View.BoardView;
import main.Board.Model.BoardModel;
import main.Common.Tile;
import main.Exceptions.InitialWordNotOnCenterException;
import main.Exceptions.InvalidInputException;
import main.Exceptions.InvalidMoveException;

import java.util.List;

/**
 * The java.Board Controller - handles interactions
 */
public class BoardController {
    /**
     * The board model
     */
    private BoardModel boardModel;

    /**
     * The board view
     */
    private BoardView boardView;

    /**
     * Instantiates a board controller
     */
    public BoardController()
    {
        this.boardModel = new BoardModel();
        this.boardView = new BoardView();

    }

    /**
     * Prints the board
     */
    public String printBoard(){
        return boardView.printBoard();
    }

    /**
     * Handles input tiles from a player
     * @param tiles
     * @throws InvalidInputException if the input is not valid
     * @throws InvalidMoveException if the move cannot be done on the current state of the board
     * @throws InitialWordNotOnCenterException if the first word placed on board does not cover the center tile
     */
    public void handleTilesFromPlayer(List<Tile> tiles) throws InvalidMoveException, InvalidInputException, InitialWordNotOnCenterException {
        validateInput(tiles);
        boardModel.handleTiles(tiles);
        mapModelDataToViewData();
        boardView.printBoard();
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
                throw new InvalidInputException();
            }
        }
    }

    /**
     * Maps the data from the board model to the board view
     */
    public void mapModelDataToViewData()
    {
        char[][] boardViewData = boardModel.getBoardViewData();

        boardView.updateView(boardViewData);
    }
}
