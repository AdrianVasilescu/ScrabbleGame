package Board.Controller;

import Board.Model.BoardModel;
import Board.View.BoardView;
import Common.Tile;

/**
 * The Board Controller - handles interactions
 */
public class BoardController {
    /**
     * The board model
     */
    private BoardModel boardModel;

    /**
     * The board view
     */
    private BoardView boardView;

    /**
     * Instantiates a board controller
     */
    public BoardController()
    {
        this.boardModel = new BoardModel();
        this.boardView = new BoardView();
        mapModelDataToViewData();
        boardView.printBoard();
    }

    private void mapModelDataToViewData()
    {
        char[][] boardViewData = boardModel.getBoardViewData();
        char[] tilesViewData = boardModel.getTilesViewData();
        boardView.updateView(boardViewData, tilesViewData);
    }
}
