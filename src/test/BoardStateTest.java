package test;

import main.Board.Model.BoardState;
import main.Common.Tile;
import main.Exceptions.InvalidMoveException;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class BoardStateTest {
    BoardState boardState;

    @Before
    public void init()
    {
        boardState = new BoardState();
    }
    @Test
    public void cloneTest() throws InvalidMoveException {
        boardState.placeTile(new Tile(1,1,'A'));
        BoardState clone = boardState.cloneState();
        assertEquals(boardState, clone);
        assertEquals(boardState.hashCode(), clone.hashCode()); // Coverage purpose
        clone.placeTile(new Tile(1,2,'B'));
        assertNotEquals(boardState, clone);
        assertNotEquals(boardState.hashCode(), clone.hashCode()); // Coverage purpose
    }

    @Test
    public void placeTileTest() throws InvalidMoveException
    {
        assertFalse(boardState.isPositionOccupied(0,5));
        boardState.placeTile(new Tile(0,5,'A'));
        assertTrue(boardState.isPositionOccupied(0,5));
        assertTrue(boardState.isPositionNeighboured(0,4));
        assertTrue(boardState.isPositionNeighboured(0,6));
        assertTrue(boardState.isPositionNeighboured(1,5));
    }

    @Test
    public void scoreTest() throws InvalidMoveException {
        boardState.placeTile(new Tile(7,7,'A'));
        boardState.placeTile(new Tile(7,8,'B'));
        boardState.placeTile(new Tile(7,9,'C'));
        boardState.placeTile(new Tile(8,7,'A'));
        boardState.placeTile(new Tile(6,7,'B'));
        boardState.placeTile(new Tile(5,7,'C'));
        boardState.placeTile(new Tile(4,7,'D'));
        assertEquals(34, boardState.getScoreForPlay());
        HashMap<Integer, String> horizontalWords = boardState.getHorizontalWords();
        HashMap<Integer, String> verticalWords = boardState.getVerticalWords();
        assertEquals(1, horizontalWords.size());
        assertEquals("ABC",horizontalWords.get(7 * 15 + 7));
        assertEquals(1, verticalWords.size());
        assertEquals("DCBAA",verticalWords.get(4 * 15 + 7));


        horizontalWords.put(5*15 + 6, "LCD");
        verticalWords.put(4*15 + 7, "TEST");
        boardState.setHorizontalWords(horizontalWords); // Coverage
        boardState.setVerticalWords(verticalWords); // Coverage

        boardState.placeTile(new Tile(5,6,'L'));
        boardState.placeTile(new Tile(5,8,'D'));

        assertEquals(16, boardState.getScoreForPlay());
        assertEquals('L', boardState.getViewData()[5][6]);
    }
}
