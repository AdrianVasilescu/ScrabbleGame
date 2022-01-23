package main.Board.Model;

import main.Common.Tile;
import main.Exceptions.InitialWordNotOnCenterException;
import main.Exceptions.InvalidMoveException;
import main.Exceptions.NotEnoughPlayersException;

import java.util.*;

/**
 * The java.Board Model - handles board business logic
 */
public class BoardModel {

    /**
     * The players count ids.
     */
    int playersCount = 0;

    /**
     * The player id whose turn is next.
     */
    int currentPlayer;

    private BoardState board;

    /**
     * Instantiates a board model
     */
    public BoardModel()
    {
        board = new BoardState();
        System.out.println("java.Board ready!");
    }

    public void startGame() throws NotEnoughPlayersException {
        if(this.playersCount < 2)
            throw new NotEnoughPlayersException();
        this.currentPlayer = 1;
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
     *
     * @return
     */
    public String newPlayerJoinedTheGame()
    {
        return String.valueOf(++playersCount);
    }

    /**
     * Places the tiles on the board
     * @param tiles
     * @throws InvalidMoveException if the tiles placement is not ok
     * @throws InitialWordNotOnCenterException if the first word placed on board does not cover the center tile
     */
    public void handleTiles(List<Tile> tiles) throws InvalidMoveException, InitialWordNotOnCenterException {
        validateTilesPositions(tiles);
        BoardState tempBoard = board.cloneState();

        for(Tile tile : tiles)
        {
            tempBoard.placeTile(tile.getRow(),tile.getColumn(), tile);
        }

        tempBoard.getScoreForPlay();
        this.board = tempBoard;
    }

    /**
     * Validates placing the new tiles on their positions
     * @param tiles
     * @throws InvalidMoveException if the tiles placement is not ok
     * @throws InitialWordNotOnCenterException if the first word placed on board does not cover the center tile
     */
    private void validateTilesPositions(List<Tile> tiles) throws InvalidMoveException, InitialWordNotOnCenterException {
        boolean centerOccupied = board.isPositionOccupied(7,7);
        boolean occupiesCenter = false;
        boolean neighboured = false;

        for(Tile tile : tiles)
        {
            if(board.isPositionOccupied(tile.getRow(), tile.getColumn()))
                throw new InvalidMoveException();
            neighboured |= board.isPositionNeighboured(tile.getRow(), tile.getColumn());
            if(!centerOccupied)
            {
                occupiesCenter |= tile.getRow() == 7 && tile.getColumn() == 7;
            }
        }
        if(!centerOccupied && !occupiesCenter)
        {
            throw new InitialWordNotOnCenterException();
        }
        else if(!neighboured && !occupiesCenter)
        {
            throw new InvalidMoveException();
        }
    }
}
