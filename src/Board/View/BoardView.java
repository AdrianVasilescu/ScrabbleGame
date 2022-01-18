package Board.View;

/**
 * The Board view - handles exposing the board
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
     * Prints the board
     */
    public void printBoard()
    {
        System.out.println("BOARD:\n");
        for(int i = 0; i < board.length; i++)
        {
            for(int j = 0; j < board[i].length; j++)
            {
                System.out.print("|" + board[i][j]) ;
            }
            System.out.print("|\n");
        }
    }
}
