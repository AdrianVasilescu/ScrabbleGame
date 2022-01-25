package test;

import main.Board.Model.BoardModel;
import main.Common.Tile;
import main.Exceptions.InitialWordNotOnCenterException;
import main.Exceptions.InvalidMoveException;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class BoardModelTest {
    BoardModel boardModel;

    @Before
    public void init()
    {
        boardModel = new BoardModel();
    }

    @Test
    public void testHandleTiles() throws InitialWordNotOnCenterException, InvalidMoveException {
        assertThrows(InitialWordNotOnCenterException.class,
                () -> boardModel.handleTiles(Arrays.asList(new Tile(1, 1, 'A', 1))));
        assertThrows(InvalidMoveException.class,
                () -> boardModel.handleTiles(Arrays.asList(new Tile(7, 7, 'A', 1),
                        new Tile(7, 7, 'A', 1))));
        boardModel.handleTiles(Arrays.asList(new Tile(7, 7, 'A', 1),
                new Tile(7, 8, 'D', 1),
                new Tile(7, 6, 'D', 1)));
        assertEquals('D', boardModel.getBoardViewData()[7][6]);
        assertThrows(InvalidMoveException.class,
                () -> boardModel.handleTiles(Arrays.asList(new Tile(2, 2, 'A', 1),
                        new Tile(2, 3, 'A', 1))));
    }
}
