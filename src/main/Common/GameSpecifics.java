package main.Common;

import lib.Protocol;
import main.Exceptions.InvalidInputException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class GameSpecifics {
    /**
     * The predefined multipliers of the board
     */
    public static final int[][] MULTIPLIERS = {
            {3, 1, 1, 2, 1, 1, 1, 3, 1, 1, 1, 2, 1, 1, 3},
            {1, 2, 1, 1, 1, 3, 1, 1, 1, 3, 1, 1, 1, 2, 1},
            {1, 1, 2, 1, 1, 1, 2, 1, 2, 1, 1, 1, 2, 1, 1},
            {2, 1, 1, 2, 1, 1, 1, 2, 1, 1, 1, 2, 1, 1, 2},
            {1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1},
            {1, 3, 1, 1, 1, 3, 1, 1, 1, 3, 1, 1, 1, 3, 1},
            {1, 1, 2, 1, 1, 1, 2, 1, 2, 1, 1, 1, 2, 1, 1},
            {3, 1, 1, 2, 1, 1, 1, 2, 1, 1, 1, 2, 1, 1, 3},
            {1, 1, 2, 1, 1, 1, 2, 1, 2, 1, 1, 1, 2, 1, 1},
            {1, 3, 1, 1, 1, 3, 1, 1, 1, 3, 1, 1, 1, 3, 1},
            {1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1},
            {2, 1, 1, 2, 1, 1, 1, 2, 1, 1, 1, 2, 1, 1, 2},
            {1, 1, 2, 1, 1, 1, 2, 1, 2, 1, 1, 1, 2, 1, 1},
            {1, 2, 1, 1, 1, 3, 1, 1, 1, 3, 1, 1, 1, 2, 1},
            {3, 1, 1, 2, 1, 1, 1, 3, 1, 1, 1, 2, 1, 1, 3}};
    /**
     * The predefined multipliers types
     */
    public static final char[][] MULTIPLIER_TYPES = {
            {'W', 'L', 'L', 'L', 'L', 'L', 'L', 'W', 'L', 'L', 'L', 'L', 'L', 'L', 'W'},
            {'L', 'W', 'L', 'L', 'L', 'L', 'L', 'L', 'L', 'L', 'L', 'L', 'L', 'W', 'L'},
            {'L', 'L', 'W', 'L', 'L', 'L', 'L', 'L', 'L', 'L', 'L', 'L', 'W', 'L', 'L'},
            {'L', 'L', 'L', 'W', 'L', 'L', 'L', 'L', 'L', 'L', 'L', 'W', 'L', 'L', 'L'},
            {'L', 'L', 'L', 'L', 'W', 'L', 'L', 'L', 'L', 'L', 'W', 'L', 'L', 'L', 'L'},
            {'L', 'L', 'L', 'L', 'L', 'L', 'L', 'L', 'L', 'L', 'L', 'L', 'L', 'L', 'L'},
            {'L', 'L', 'L', 'L', 'L', 'L', 'L', 'L', 'L', 'L', 'L', 'L', 'L', 'L', 'L'},
            {'L', 'L', 'L', 'L', 'L', 'L', 'L', 'W', 'L', 'L', 'L', 'L', 'L', 'L', 'L'},
            {'L', 'L', 'L', 'L', 'L', 'L', 'L', 'L', 'L', 'L', 'L', 'L', 'L', 'L', 'L'},
            {'L', 'L', 'L', 'L', 'L', 'L', 'L', 'L', 'L', 'L', 'L', 'L', 'L', 'L', 'L'},
            {'L', 'L', 'L', 'L', 'W', 'L', 'L', 'L', 'L', 'L', 'W', 'L', 'L', 'L', 'L'},
            {'L', 'L', 'L', 'W', 'L', 'L', 'L', 'L', 'L', 'L', 'L', 'W', 'L', 'L', 'L'},
            {'L', 'L', 'W', 'L', 'L', 'L', 'L', 'L', 'L', 'L', 'L', 'L', 'W', 'L', 'L'},
            {'L', 'W', 'L', 'L', 'L', 'L', 'L', 'L', 'L', 'L', 'L', 'L', 'L', 'W', 'L'},
            {'W', 'L', 'L', 'L', 'L', 'L', 'L', 'W', 'L', 'L', 'L', 'L', 'L', 'L', 'W'}};

    //private static final ScrabbleWordChecker CHECKER;
    private GameSpecifics(){}

    public static boolean checkWord(String word)
    {
        // TODO
        return true;
    }

    public static int getRow(String character) throws InvalidInputException {
        switch (character)
        {
            case "A":
                return 0;
            case "B":
                return 1;
            case "C":
                return 2;
            case "D":
                return 3;
            case "E":
                return 4;
            case "F":
                return 5;
            case "G":
                return 6;
            case "H":
                return 7;
            case "I":
                return 8;
            case "J":
                return 9;
            case "K":
                return 10;
            case "L":
                return 11;
            case "M":
                return 12;
            case "N":
                return 13;
            case "0":
                return 14;
            default:
                throw new InvalidInputException(Protocol.Error.E004);
        }
    }

    public static int getScoreOfCharacter(char c) {
        switch (c)
        {
            case 'A':
            case 'E':
            case 'I':
            case 'L':
            case 'N':
            case 'O':
            case 'R':
            case 'S':
            case 'T':
            case 'U':
                return 1;
            case 'D':
            case 'G':
                return 2;
            case 'B':
            case 'C':
            case 'M':
            case 'P':
                return 3;
            case 'F':
            case 'H':
            case 'V':
            case 'W':
            case 'Y':
                return 4;
            case 'K':
                return 5;
            case 'J':
            case 'X':
                return 8;
            case 'Q':
            case 'Z':
                return 10;
            default:
                if(Character.isAlphabetic(c) && Character.isLowerCase(c))
                    return 0;
        }
        return -1;
    }

    /**
     * Extracts the list of placed tiles from the command
     * @param start
     * @param align
     * @param word
     * @return the list of tiles
     */
    public static List<Tile> extractTiles(String start, String align, String word)
            throws InvalidInputException {
        List<Tile> ret = new ArrayList<>();
        char[] characters = word.toCharArray();
        int row = getRow(start.substring(0, 1));
        int column = -1;
        try{
            column = Integer.parseInt(start.substring(1)) - 1;
        }
        catch (NumberFormatException e)
        {
            throw new InvalidInputException(Protocol.Error.E003);
        }
        for(int i = 0; i < word.length(); i++)
        {
            if("H".equals(align))
            {
                ret.add(new Tile(row, column + i, characters[i]));
            }
            else if("V".equals(align))
            {
                ret.add(new Tile(row + i, column, characters[i]));
            }
            else
            {
                throw new InvalidInputException(Protocol.Error.E003);
            }
        }

        return ret;
    }
}
