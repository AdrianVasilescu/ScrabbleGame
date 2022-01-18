package Board.Model;

import Common.Tile;
import Exceptions.InvalidMoveException;
import Exceptions.NotEnoughPlayersException;

import java.util.*;

/**
 * The Board Model - handles board business logic
 */
public class BoardModel {
    /**
     * The board
     */
    Tile[][] board;

    /**
     * The players count ids.
     */
    int playersCount = 0;

    /**
     * The player id whose turn is next.
     */
    int currentPlayer;

    /**
     * Instantiates a board model
     */
    public BoardModel()
    {
        initializeBoard();
        System.out.println("Board ready!");
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
        char[][] viewData = new char[15][15];

        for(int i = 0; i < board.length; i++)
        {
            for(int j = 0; j < board[i].length; j++)
            {
                if(board[i][j] == null)
                {
                    viewData[i][j] = '-';
                }
                else
                {
                    viewData[i][j] = board[i][j].getLetter();
                }
            }
        }

        return viewData;
    }

    /**
     * Tells if a position is occupied by another tile
     * @param row
     * @param column
     * @return
     */
    public boolean isPositionOccupied(int row, int column)
    {
        return board[row][column] != null;
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
     * Initialize the board
     */
    private void initializeBoard()
    {
        board = new Tile[15][15];
    }

    /**
     * Places the tiles on the board
     * @param tiles
     */
    public void handleTiles(List<Tile> tiles) throws InvalidMoveException {
        validateTilesPositions(tiles);
        for(Tile tile : tiles)
        {
            board[tile.getRow()][tile.getColumn()] = tile;
        }
    }

    /**
     * Validates placing the new tiles on their positions
     * @param tiles
     * @throws InvalidMoveException if the tiles placement is not ok
     */
    private void validateTilesPositions(List<Tile> tiles) throws InvalidMoveException {
        for(Tile t : tiles)
        {
            if(isPositionOccupied(t.getRow(), t.getColumn()))
                throw new InvalidMoveException();
        }
    }
}
