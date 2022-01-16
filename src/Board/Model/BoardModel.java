package Board.Model;

import Common.Tile;

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
     * The available tiles
     */
    List<Tile> tileStack;

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
        initializeTileStack();
        System.out.println("Board ready!");
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
     * Returns the available tiles
     * @return
     */
    public char[] getTilesViewData() {
        char[] tiles = new char[tileStack.size()];
        for(int i = 0; i < tileStack.size(); i++)
        {
            tiles[i] = tileStack.get(i).getLetter();
        }

        return tiles;
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
     * Returns a number of random tiles from the tiles stack
     * @param numTiles
     * @return
     */
    public List<Tile> getTilesFromStack(int numTiles)
    {
        List<Tile> tiles = new ArrayList<>();
        Random rand = new Random();

        while (numTiles > 0)
        {
            tiles.add(tileStack.remove(rand.nextInt(tileStack.size() - 1)));
            numTiles--;
        }

        return tiles;
    }

    /**
     *
     * @return
     */
    public int newPlayerJoinedTheGame()
    {
        return ++playersCount;
    }

    /**
     * Initialize the board
     */
    private void initializeBoard()
    {
        board = new Tile[15][15];
        this.tileStack = new ArrayList<>();
        this.currentPlayer = 1;
    }

    /**
     * Initializes the tile stack
     */
    private void initializeTileStack() {
        tileStack.addAll(Collections.nCopies(9, new Tile('A', 1)));
        tileStack.addAll(Collections.nCopies(2, new Tile('B', 3)));
        tileStack.addAll(Collections.nCopies(2, new Tile('C', 3)));
        tileStack.addAll(Collections.nCopies(4, new Tile('D', 2)));
        tileStack.addAll(Collections.nCopies(12, new Tile('E', 1)));
        tileStack.addAll(Collections.nCopies(2, new Tile('F', 4)));
        tileStack.addAll(Collections.nCopies(2, new Tile('G', 2)));
        tileStack.addAll(Collections.nCopies(2, new Tile('H', 4)));
        tileStack.addAll(Collections.nCopies(8, new Tile('I', 1)));
        tileStack.addAll(Collections.nCopies(2, new Tile('J', 8)));
        tileStack.addAll(Collections.nCopies(2, new Tile('K', 5)));
        tileStack.addAll(Collections.nCopies(4, new Tile('L', 1)));
        tileStack.addAll(Collections.nCopies(2, new Tile('M', 3)));
        tileStack.addAll(Collections.nCopies(6, new Tile('N', 1)));
        tileStack.addAll(Collections.nCopies(8, new Tile('O', 1)));
        tileStack.addAll(Collections.nCopies(2, new Tile('P', 3)));
        tileStack.addAll(Collections.nCopies(1, new Tile('Q', 10)));
        tileStack.addAll(Collections.nCopies(6, new Tile('R', 1)));
        tileStack.addAll(Collections.nCopies(4, new Tile('S', 1)));
        tileStack.addAll(Collections.nCopies(6, new Tile('T', 1)));
        tileStack.addAll(Collections.nCopies(4, new Tile('U', 1)));
        tileStack.addAll(Collections.nCopies(2, new Tile('V', 4)));
        tileStack.addAll(Collections.nCopies(2, new Tile('W', 4)));
        tileStack.addAll(Collections.nCopies(1, new Tile('X', 8)));
        tileStack.addAll(Collections.nCopies(2, new Tile('Y', 4)));
        tileStack.addAll(Collections.nCopies(1, new Tile('Z', 10)));
        tileStack.addAll(Collections.nCopies(2, new Tile('*', 0)));
    }
}
