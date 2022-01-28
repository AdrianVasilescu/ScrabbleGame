package main.Board.View;

/**
 * The java.Board view - handles exposing the board
 */
public class BoardView {
    /**
     * The board
     */
    char[][] board;

    /**
     * Updates the view
     */
    public void updateView(char[][] newBoard)
    {
        this.board = newBoard;
    }

    /**
     * Get the board view data
     */
    public char[][] getCurrentView()
    {
        return board;
    }

    /**
     * Prints the board
     */
    public char[][] getBoard()
    {
        return board;
    }
}
