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
     * The available tiles
     */
    char[] tiles;

    /**
     * Updates the view
     */
    public void updateView(char[][] newBoard, char[] tilesViewData)
    {
        this.board = newBoard;
        this.tiles = tilesViewData;
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
        System.out.println("\nTILES STACK:\n");
        int spacingCount = 0;
        System.out.print("[");
        for(char c : tiles)
        {
            if(spacingCount == 20) {
                System.out.print("\n");
                spacingCount = 0;
            }
            System.out.print("[" + c + "]");
            spacingCount++;
        }
        System.out.println("]");
    }
}
