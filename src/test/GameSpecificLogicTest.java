package test;

import lib.Protocol;
import main.Board.Model.BoardState;
import main.Common.Tile;
import main.Exceptions.InvalidInputException;
import main.Exceptions.InvalidMoveException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static main.Game.GameSpecifics.*;

public class GameSpecificLogicTest {

    @Test
    public void testCheckWord()
    {
        assertTrue(checkWord("DOG"));
        assertFalse(checkWord("DOGI"));
    }

    @Test
    public void testGetCol() throws InvalidInputException {
        assertEquals(0, getCol("A"));
        assertEquals(3, getCol("D"));
        assertEquals(8, getCol("I"));
        assertEquals(14, getCol("O"));
        assertThrows(InvalidInputException.class, () -> getCol("P"));
    }

    @Test
    public void testGetSchoreOfCharacter()
    {
        assertEquals(1, getScoreOfCharacter('A'));
        assertEquals(0, getScoreOfCharacter('a'));
        assertEquals(-1, getScoreOfCharacter(Protocol.UNIT_SEPARATOR));
    }

    @Test
    public void testExtractTiles() throws InvalidInputException {
        char[][] board = new char[15][15];
        List<Tile> expected = Arrays.asList(new Tile(0, 0, 'S'),
                new Tile(0, 1, 'N'),
                new Tile(0, 2, 'A'),
                new Tile(0, 3, 'K'),
                new Tile(0, 4, 'E'));

        assertEquals(expected, extractTiles("A1", "H", "SNAKE", board));

        board[0][3] = 'k';
        assertEquals(expected, extractTiles("A1", "H", "SNAKE", board));

        board[0][3] = 'K';
        expected = Arrays.asList(new Tile(0, 0, 'S'),
                new Tile(0, 1, 'N'),
                new Tile(0, 2, 'A'),
                new Tile(0, 4, 'E'));
        assertEquals(expected, extractTiles("A1", "H", "SNAKE", board));

        try
        {
            extractTiles("AB", "H", "SNAKE", board);
        }
        catch (InvalidInputException e)
        {
            assertEquals(Protocol.Error.E003, e.getError());
        }

        try
        {
            extractTiles("A100", "H", "SNAKE", board);
        }
        catch (InvalidInputException e)
        {
            assertEquals(Protocol.Error.E004, e.getError());
        }

        try
        {
            extractTiles("L10", "H", "SNAKE", board);
        }
        catch (InvalidInputException e)
        {
            assertEquals(Protocol.Error.E004, e.getError());
        }

        try
        {
            extractTiles("B10", "McDonalds", "SNAKE", board);
        }
        catch (InvalidInputException e)
        {
            assertEquals(Protocol.Error.E003, e.getError());
        }
    }

    @Test
    public void testInlineTiles()
    {
        List<Tile> expected = Arrays.asList(new Tile('S'),
                new Tile('N'),
                new Tile('A'),
                new Tile('K'),
                new Tile('E'));
        assertEquals(expected, extractInlineTiles("SNAKE"));
    }

    @Test
    public void testDecodeRequestgame() throws InvalidInputException, InvalidMoveException {
        String request = "REQUESTGAME";
        assertThrows(InvalidMoveException.class, () -> decodeRequestGame(new String[]{""}));
        assertThrows(InvalidMoveException.class, () -> decodeRequestGame(new String[0]));
        assertEquals(2, decodeRequestGame(new String[]{request}));
        assertEquals(2, decodeRequestGame(new String[]{request, "2"}));
        assertEquals(3, decodeRequestGame(new String[]{request, "3"}));
        assertThrows(InvalidMoveException.class, () -> decodeRequestGame(new String[]{"test", "2"}));
        assertThrows(InvalidInputException.class, () -> decodeRequestGame(new String[]{request, "7"}));
        assertThrows(InvalidInputException.class, () -> decodeRequestGame(new String[]{request, "T"}));
    }

    @Test
    public void testEncodeMessage()
    {
        assertEquals(String.valueOf(Protocol.MESSAGE_SEPARATOR),
                encodeMessage(""));
        assertEquals("A" + Protocol.MESSAGE_SEPARATOR,
                encodeMessage("A"));
        String expected = "A" + Protocol.UNIT_SEPARATOR + "B" + Protocol.UNIT_SEPARATOR + "C" ;
        assertEquals(expected + Protocol.MESSAGE_SEPARATOR,
                encodeMessage("A", "B", "C"));
        assertEquals(expected + Protocol.UNIT_SEPARATOR + "D" +Protocol.MESSAGE_SEPARATOR,
                encodeMessage("A", "B", "C", "D"));
    }

    @Test
    public void testAnyPossibleMoves() throws InvalidMoveException {
        BoardState boardState = new BoardState();
        boardState.placeTile(new Tile(7,7, 'R'));
        List<Character> tiles = new ArrayList<>(Arrays.asList('T'));
        assertFalse(anyPossibleMoves(boardState, tiles));
        tiles.add('I');
        assertTrue(anyPossibleMoves(boardState, tiles));
        tiles.remove((Character)'I');
        tiles.add('!');
        assertTrue(anyPossibleMoves(boardState, tiles));
    }

    @Test
    public void extraTest() throws InvalidMoveException {
        BoardState boardState = new BoardState();
        boardState.placeTile(new Tile(7,6, 'B'));
        boardState.placeTile(new Tile(7,7, 'A'));
        boardState.placeTile(new Tile(7,8, 'D'));
        List<Character> tiles = new ArrayList<>(Arrays.asList('E', 'S', 'A', 'D', 'D', 'A', 'E'));
        assertTrue(anyPossibleMoves(boardState, tiles));
    }

    @Test
    public void testIsNumeric()
    {
        assertTrue(isNumeric("1"));
        assertTrue(isNumeric("01"));
        assertTrue(isNumeric("01213"));
        assertFalse(isNumeric("A"));
        assertFalse(isNumeric("1A"));
        assertFalse(isNumeric("1A2"));
    }
}
