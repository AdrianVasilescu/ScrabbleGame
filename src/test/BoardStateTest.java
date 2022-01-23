package test;

import main.Board.Model.BoardState;
import main.Common.Tile;
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
    public void cloneTest()
    {
        boardState.placeTile(1,1,new Tile('A',2));
        BoardState clone = boardState.cloneState();
        assertEquals(boardState, clone);
        assertEquals(boardState.hashCode(), clone.hashCode()); // Coverage purpose
        clone.placeTile(1,2,new Tile('B', 3));
        assertNotEquals(boardState, clone);
        assertNotEquals(boardState.hashCode(), clone.hashCode()); // Coverage purpose
    }

    @Test
    public void placeTileTest()
    {
        assertFalse(boardState.isPositionOccupied(0,5));
        boardState.placeTile(0,5,new Tile('A',2));
        assertTrue(boardState.isPositionOccupied(0,5));
        assertTrue(boardState.isPositionNeighboured(0,4));
        assertTrue(boardState.isPositionNeighboured(0,6));
        assertTrue(boardState.isPositionNeighboured(1,5));
    }

    @Test
    public void scoreTest()
    {
        boardState.placeTile(7,7,new Tile('A', 4));
        boardState.placeTile(7,8,new Tile('B', 2));
        boardState.placeTile(7,9,new Tile('C', 3));
        boardState.placeTile(8,7,new Tile('A', 1));
        boardState.placeTile(6,7,new Tile('B', 1));
        boardState.placeTile(5,7,new Tile('C', 1));
        boardState.placeTile(4,7,new Tile('D', 1));
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

        boardState.placeTile(5,6,new Tile('L', 1));
        boardState.placeTile(5,8,new Tile('D', 1));

        assertEquals(16, boardState.getScoreForPlay());
        assertEquals('L', boardState.getViewData()[5][6]);
    }
}
