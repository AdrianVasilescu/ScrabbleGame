package main.Board.Model;

import lib.Protocol;
import main.Game.GameSpecifics;
import main.Common.Tile;
import main.Exceptions.InvalidMoveException;

import java.util.*;

import static main.Game.GameSpecifics.EMPTY_SLOT;
import static main.Game.GameSpecifics.getScoreOfCharacter;

public class BoardState {
    /**
     * The board
     */
    private Tile[][] board;
    /**
     * The horizontal words scored so far and position of starting letter
     */
    private HashMap<Integer, String> horizontalWords;

    /**
     * The vertical words scored so far and position of starting letter
     */
    private HashMap<Integer, String> verticalWords;

    public BoardState() {
        this.board = new Tile[15][15];
        horizontalWords = new HashMap<>();
        verticalWords = new HashMap<>();
    }


    public HashMap<Integer, String> getHorizontalWords() {
        return horizontalWords;
    }

    public void setHorizontalWords(HashMap<Integer, String> horizontalWords) {
        this.horizontalWords = horizontalWords;
    }

    public HashMap<Integer, String> getVerticalWords() {
        return verticalWords;
    }

    public void setVerticalWords(HashMap<Integer, String> verticalWords) {
        this.verticalWords = verticalWords;
    }

    /**
     * Places a tile
     * @param tile
     * @throws InvalidMoveException if the position is already occupied
     */
    public void placeTile(Tile tile) throws InvalidMoveException
    {
        if(isPositionOccupied(tile.getRow(), tile.getColumn()))
        {
            if(board[tile.getRow()][tile.getColumn()].getLetter() != tile.getLetter())
                throw new InvalidMoveException(Protocol.Error.E005);
        }
        board[tile.getRow()][tile.getColumn()] = tile;
    }

    /**
     * Tells if a position is occupied by another tile
     * @param row
     * @param column
     * @return boolean
     */
    public boolean isPositionOccupied(int row, int column)
    {
        return board[row][column] != null;
    }

    /**
     * Tells if a position is neighboured by another tile
     * @param row
     * @param column
     * @return boolean
     */
    public boolean isPositionNeighboured(int row, int column) {
        return (row > 0 && isPositionOccupied(row - 1, column))
                || (row < 14 && isPositionOccupied(row + 1, column))
                || (column > 0 && isPositionOccupied(row, column - 1))
                || (column < 14 && isPositionOccupied(row, column + 1));
    }

    /**
     * returns the current board
     * @return the board
     */
    public char[][] getViewData() {
        char[][] viewData = new char[15][15];

        for(int i = 0; i < board.length; i++)
        {
            for(int j = 0; j < board[i].length; j++)
            {
                if(board[i][j] == null)
                {
                    viewData[i][j] = EMPTY_SLOT;
                }
                else
                {
                    viewData[i][j] = board[i][j].getLetter();
                }
            }
        }

        return viewData;
    }

    public BoardState cloneState()
    {
        BoardState clone = new BoardState();
        clone.board = cloneBoard();
        clone.verticalWords = new HashMap<>(verticalWords);
        clone.horizontalWords = new HashMap<>(horizontalWords);

        return clone;
    }

    private Tile[][] cloneBoard()
    {
        Tile[][] clone = Arrays.copyOf(board, 15);
        for(int i = 0; i < 15; i++)
        {
            clone[i] = Arrays.copyOf(board[i], 15);
        }

        return clone;
    }

    public int getScoreForPlay() throws InvalidMoveException {
        boolean[][] visited = new boolean[15][15];

        if(isPositionOccupied(7, 6)
            || isPositionOccupied(7, 8))
        {
            int rootColumn = findLeftRoot(7, 7);
            return checkWordHorizontally(7,rootColumn, visited);
        } else if(isPositionOccupied(6, 7)
                || isPositionOccupied(8, 7))
        {
            int rootRow = findUpperRoot(7, 7);
            return checkWordVertically(rootRow,7, visited);
        }

        return 0;
    }

    /**
     * Finds the column of the first letter of an horizontal word
     * @param row
     * @param column
     * @return the column
     */
    private int findLeftRoot(int row, int column)
    {
        while(column - 1 >= 0 && isPositionOccupied(row, column - 1))
        {
            column--;
        }
        return column;
    }

    /**
     * Find the row of the first letter of a vertical word
     * @param row
     * @param column
     * @return the row
     */
    private int findUpperRoot(int row, int column)
    {
        while(row - 1 >= 0 && isPositionOccupied(row - 1,column))
        {
            row--;
        }
        return row;
    }

    /**
     * Checks for a word horizontally
     * @param rootRow
     * @param rootColumn
     * @param visited
     * @return the score for this word
     * @throws InvalidMoveException in case the word is not valid
     */
    private int checkWordHorizontally(int rootRow, int rootColumn, boolean[][] visited) throws InvalidMoveException {
        String word = "";
        int score = 0;
        int wordMultiplier = 1;
        List<Integer> columnsToCheck = new ArrayList<>();
        int column = rootColumn;
        int row = rootRow;

        while(column <= 14 && isPositionOccupied(row,column))
        {
            word += board[row][column].getLetter();
            switch(GameSpecifics.MULTIPLIER_TYPES[row][column]) {
                case 'L':
                    score += getScoreOfCharacter(board[row][column].getLetter()) * GameSpecifics.MULTIPLIERS[row][column];
                    break;
                case 'W':
                    score += getScoreOfCharacter(board[row][column].getLetter());
                    wordMultiplier *= GameSpecifics.MULTIPLIERS[row][column];
            }
            visited[row][column] = true;
            if((row > 0 && isPositionOccupied(row-1, column) && !visited[row-1][column])
                    ||
                    (row < 14 && isPositionOccupied(row+1, column) && !visited[row+1][column]))
            {
                columnsToCheck.add(column);
            }
            column++;
        }
        score *= wordMultiplier;
        System.out.println("Found word:" + word + " Score: " + score);
        if(!validateHorizontalWord(rootRow, rootColumn, word))
        {
            score = 0;
        }

        for(int columnToCheck : columnsToCheck)
        {
            int rowToCheck = findUpperRoot(row, columnToCheck);
            score += checkWordVertically(rowToCheck, columnToCheck, visited);
        }

        return score;
    }

    /**
     * Validates the horizontal word
     * @param row
     * @param column
     * @param word
     * @return whether the word is valid or not
     * @throws InvalidMoveException in case the word is not valid
     */
    private boolean validateHorizontalWord(int row, int column, String word)
            throws InvalidMoveException {
        int position = row * 15 + column;
        if(horizontalWords.containsKey(position) && horizontalWords.get(position).equals(word))
        {
            return false;
        }
        horizontalWords.put(position, word);
        if(!GameSpecifics.checkWord(word))
        {
            throw new InvalidMoveException(Protocol.Error.E006);
        }
        return true;
    }

    /**
     * Checks for a word vertically
     * @param rootRow
     * @param rootColumn
     * @param visited
     * @return the score for this word
     * @throws InvalidMoveException in case the word is not valid
     */
    private int checkWordVertically(int rootRow, int rootColumn, boolean[][] visited) throws InvalidMoveException {
        String word = "";
        int score = 0;
        int wordMultiplier = 1;
        List<Integer> rowsToCheck = new ArrayList<>();
        int column = rootColumn;
        int row = rootRow;

        while(row <= 14 && isPositionOccupied(row,column))
        {
            word += board[row][column].getLetter();
            switch(GameSpecifics.MULTIPLIER_TYPES[row][column]) {
                case 'L':
                    score += getScoreOfCharacter(board[row][column].getLetter()) * GameSpecifics.MULTIPLIERS[row][column];
                    break;
                case 'W':
                    score += getScoreOfCharacter(board[row][column].getLetter());
                    wordMultiplier *= GameSpecifics.MULTIPLIERS[row][column];
            }
            visited[row][column] = true;
            if((column > 0 && isPositionOccupied(row,column-1) && !visited[row][column-1])
                    ||
                    (column < 14 && isPositionOccupied(row,column+1) && !visited[row][column+1]))
            {
                rowsToCheck.add(row);
            }
            row++;
        }
        score *= wordMultiplier;
        System.out.println("Found word:" + word + " Score: " + score);
        if(!validateVerticalWord(rootRow, rootColumn, word))
        {
            score = 0;
        }

        for(int rowToCheck : rowsToCheck)
        {
            int columnToCheck = findLeftRoot(rowToCheck, column);
            score += checkWordHorizontally(rowToCheck, columnToCheck, visited);
        }

        return score;
    }

    /**
     * Validates the horizontal word and gets it's score
     * @param row
     * @param column
     * @param word
     * @return the word's score
     * @throws InvalidMoveException in case the word is not valid
     */
    private boolean validateVerticalWord(int row, int column, String word) throws InvalidMoveException {
        int position = row * 15 + column;
        if(verticalWords.containsKey(position) && verticalWords.get(position).equals(word))
        {
            return false;
        }
        verticalWords.put(position, word);
        if(!GameSpecifics.checkWord(word))
        {
            throw new InvalidMoveException(Protocol.Error.E006);
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BoardState)) return false;
        BoardState that = (BoardState) o;
        return Arrays.deepEquals(board, that.board) && Objects.equals(horizontalWords, that.horizontalWords) && Objects.equals(verticalWords, that.verticalWords);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(horizontalWords, verticalWords);
        result = 31 * result + Arrays.deepHashCode(board);
        return result;
    }
}
