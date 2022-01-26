package main.Board.Model;

import lib.Protocol;
import main.Common.Tile;
import main.Exceptions.InitialWordNotOnCenterException;
import main.Exceptions.InvalidMoveException;

import java.util.*;

/**
 * The java.Board Model - handles board business logic
 */
public class BoardModel {

    private BoardState board;

    /**
     * Instantiates a board model
     */
    public BoardModel()
    {
        board = new BoardState();
        System.out.println("Board ready!");
    }

    /**
     * returns the current board
     * @return the board
     */
    public char[][] getBoardViewData()
    {
        return board.getViewData();
    }

    /**
     * Places the tiles on the board
     * @param tiles
     * @throws InvalidMoveException if the tiles placement is not ok
     * @throws InitialWordNotOnCenterException if the first word placed on board does not cover the center tile
     */
    public void handleTiles(List<Tile> tiles) throws InvalidMoveException, InitialWordNotOnCenterException {
        boolean centerOccupied = board.isPositionOccupied(7,7);
        boolean occupiesCenter = false;
        boolean neighboured = false;
        BoardState tempBoard = board.cloneState();

        for(Tile tile : tiles)
        {
            neighboured |= board.isPositionNeighboured(tile.getRow(), tile.getColumn());
            if(!centerOccupied)
            {
                occupiesCenter |= tile.getRow() == 7 && tile.getColumn() == 7;
            }
            tempBoard.placeTile(tile);
        }

        if(!centerOccupied && !occupiesCenter)
        {
            throw new InitialWordNotOnCenterException();
        }
        else if(!neighboured && !occupiesCenter)
        {
            throw new InvalidMoveException(Protocol.Error.E005);
        }

        tempBoard.getScoreForPlay();
        this.board = tempBoard;
    }
}
