package main.Board.Controller;

import lib.Protocol;
import main.Board.View.BoardView;
import main.Board.Model.BoardServerModel;
import main.Common.Tile;
import main.Exceptions.InitialWordNotOnCenterException;
import main.Exceptions.InvalidInputException;
import main.Exceptions.InvalidMoveException;

import java.util.List;

import static main.Common.GameSpecifics.extractTiles;

/**
 * The java.Board Controller - handles interactions
 */
public class BoardController {
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
    public String printBoard(){
        return boardView.printBoard();
    }

    /**
     * Handles input tiles from a player
     * @param start the start tile
     * @param align vertical or horizontal
     * @param word the word
     * @return the score
     * @throws InvalidInputException if the input is not valid
     * @throws InvalidMoveException if the move cannot be done on the current state of the board
     * @throws InitialWordNotOnCenterException if the first word placed on board does not cover the center tile
     */
    public int handleMove(String start, String align, String word)
            throws InvalidMoveException, InvalidInputException, InitialWordNotOnCenterException {
        List<Tile> tiles = extractTiles(start, align, word);
        validateInput(tiles);
        int score = boardServerModel.handleTiles(tiles);
        mapModelDataToViewData();
        boardView.printBoard();

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
}
