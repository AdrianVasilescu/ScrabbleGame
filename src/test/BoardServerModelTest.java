package test;

import main.Board.Model.BoardServerModel;
import main.Common.Tile;
import main.Exceptions.InitialWordNotOnCenterException;
import main.Exceptions.InvalidMoveException;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class BoardServerModelTest {
    BoardServerModel boardServerModel;

    @Before
    public void init()
    {
        boardServerModel = new BoardServerModel();
    }

    @Test
    public void testHandleTiles() throws InitialWordNotOnCenterException, InvalidMoveException {
        assertThrows(InitialWordNotOnCenterException.class,
                () -> boardServerModel.handleTiles(Arrays.asList(new Tile(1, 1, 'A'))));
        assertThrows(InvalidMoveException.class,
                () -> boardServerModel.handleTiles(Arrays.asList(new Tile(7, 7, 'A'),
                        new Tile(7, 7, 'A'))));
        boardServerModel.handleTiles(Arrays.asList(new Tile(7, 7, 'A'),
                new Tile(7, 8, 'D'),
                new Tile(7, 6, 'D')));
        assertEquals('D', boardServerModel.getBoardViewData()[7][6]);
        assertThrows(InvalidMoveException.class,
                () -> boardServerModel.handleTiles(Arrays.asList(new Tile(2, 2, 'A'),
                        new Tile(2, 3, 'A'))));
    }
}
