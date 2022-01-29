package main.Game;

import lib.Protocol;
import lib.checker.java.InMemoryScrabbleWordChecker;
import lib.checker.java.ScrabbleWordChecker;
import main.Board.Model.BoardState;
import main.Common.Tile;
import main.Exceptions.InvalidInputException;
import main.Exceptions.InvalidMoveException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public final class GameSpecifics {
    /**
     * Empty slot on board
     */
    public static final char EMPTY_SLOT = '-';

    /**
     * The pattern to identify a number
     */
    private static final Pattern NUMBER_PATTERN = Pattern.compile("-?\\d+(\\.\\d+)?");
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

    private static final ScrabbleWordChecker CHECKER = new InMemoryScrabbleWordChecker();
    private GameSpecifics(){}

    public static boolean checkWord(String word)
    {
        ScrabbleWordChecker.WordResponse wordResponse = CHECKER.isValidWord(word);
        System.out.println(word + " checked : " + wordResponse);
        return wordResponse != null;
    }

    public static int getCol(String character) throws InvalidInputException {
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
        int column = getCol(start.substring(0, 1));
        int row = -1;
        try{
            row = Integer.parseInt(start.substring(1)) - 1;
        }
        catch (NumberFormatException e)
        {
            throw new InvalidInputException(Protocol.Error.E003);
        }

        if(row < 0 || row > 14)
        {
            throw new InvalidInputException(Protocol.Error.E004);
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

    public static List<Tile> extractInlineTiles(String s)
    {
        List<Tile> ret = new ArrayList<>();

        for (char c : s.toCharArray()) {
            ret.add(new Tile(c));
        }

        return ret;
    }

    private static String buildAnnounce(String name)
    {
        return Protocol.BasicCommand.ANNOUNCE.toString() + Protocol.UNIT_SEPARATOR + name + Protocol.MESSAGE_SEPARATOR;
    }

    public static int decodeRequestGame(String[] parts) throws InvalidInputException, InvalidMoveException {
        int numP = -1;
        if(Protocol.BasicCommand.REQUESTGAME.name().equals(parts[0]))
        {
            if(parts.length >= 2)
            {
                try {
                    numP = Integer.parseInt(parts[1]);
                }
                catch (NumberFormatException e)
                {
                    throw new InvalidInputException(Protocol.Error.E003);
                }
            }
            else if(parts.length == 1)
            {
                numP = 2;
            }
            else
            {
                throw new InvalidMoveException(Protocol.Error.E002);
            }
        }
        else
        {
            throw new InvalidMoveException(Protocol.Error.E002);
        }
        return numP;
    }

    public static String encodeMessage(String name, String... parameters)
    {
        String cmd = name;
        for (String param : parameters) {
            cmd += Protocol.UNIT_SEPARATOR + param;
        }
        cmd += Protocol.MESSAGE_SEPARATOR;

        return cmd;
    }

    public static boolean anyPossibleMoves(BoardState boardState, List<Character> availableTiles)
    {
        boolean possible = false;

        for(int i = 0; i < 15; i++)
        {
            for(int j = 0; j < 15; j++)
            {
                if(!boardState.isPositionOccupied(i, j) && boardState.isPositionNeighboured(i, j))
                {
                    for(char c : availableTiles)
                    {
                        List<Character> newAvailableTiles = new ArrayList<>(availableTiles);
                        newAvailableTiles.remove((Character) c);

                        if(c == '!')
                        {
                            for(char cc : "abcdefghijklmnopqrstuvwxyz".toCharArray())
                            {
                                if (canLeadToSolution(boardState, newAvailableTiles, i, j, cc))
                                {
                                    System.out.print("-> Placed " + cc + "on [" + i + "][" + j +"] ->");
                                    return true;
                                }
                            }
                        }
                        else
                        {
                            if (canLeadToSolution(boardState, newAvailableTiles, i, j, c))
                            {
                                System.out.print("-> Placed " + c + "on [" + i + "][" + j + "] ->");
                                return true;
                            }
                        }
                    }
                }
            }
        }
        System.out.println("No more possible moves");
        return false;
    }

    private static boolean canLeadToSolution(BoardState boardState, List<Character> availableTiles, int i, int j, char c) {
        boolean possible;
        BoardState clone = boardState.cloneState();

        try {
            clone.placeTile(new Tile(i, j, c));

            int score = clone.getScoreForPlay();
            if(score > 0)
                return true;
            possible = anyPossibleMoves(clone, availableTiles);
            if(possible)
                return true;
        } catch (InvalidMoveException e) {
            // EXCEPTION MEANS WORDS ARE NOT VALID
            System.out.println(".");
            return false;
        }
        return false;
    }

    public static String extractTilesFromCommand(String command)
    {
        String[] parts = command.split(String.valueOf(Protocol.UNIT_SEPARATOR));
        if("WORD".equals(parts[1]))
        {
            return parts[4];
        }
        else if("SWAP".equals(parts[1]))
        {
            return parts[2];
        }

        return "";
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        return NUMBER_PATTERN.matcher(strNum).matches();
    }
}
