package main.Board.View;

/**
 * The java.Board view - handles exposing the board
 */
public class BoardView {
    String sp = "__";
    String boardHeader = sp  + sp + "A" + sp +
            sp + "B" + sp +
            sp + "C" + sp +
            sp + "D" + sp +
            sp + "E" + sp +
            sp + "_F" + sp +
            sp + "G" + sp +
            sp + "H" + sp +
            sp + "I" + sp +
            sp + "J" + sp +
            sp + "_K" + sp +
            sp + "L" + sp +
            sp + "M" + sp +
            sp + "N" + sp +
            sp + "O" + sp;
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
    public String printBoard()
    {
        String ret = boardHeader + "\n";
        for(int i = 0; i < board.length; i++)
        {
            ret += String.format("%02d",(i + 1)) + "|";
            for(int j = 0; j < board[i].length; j++)
            {
                ret += ("[    " + board[i][j] + "    ]") ;
            }
            ret += "\n\n";
        }

        return ret;
    }
}
