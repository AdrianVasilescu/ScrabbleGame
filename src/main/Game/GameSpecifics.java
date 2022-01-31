package main.Game;

import lib.Protocol;
import lib.checker.java.InMemoryScrabbleWordChecker;
import lib.checker.java.ScrabbleWordChecker;
import main.Board.Model.BoardState;
import main.Common.Tile;
import main.Exceptions.InvalidInputException;
import main.Exceptions.InvalidMoveException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Game utility class
 */
public final class GameSpecifics {
    /**
     * Empty slot on board
     */
    public static final char EMPTY_SLOT = '-';
    /**
     * The local command input parts delimiter
     */
    public static final char LOCAL_DELIMITER = '-';
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

    /**
     * The word checker
     */
    private static final ScrabbleWordChecker CHECKER = new InMemoryScrabbleWordChecker();

    private GameSpecifics(){
        // This is an utility class -> private constructor
    }

    /**
     * Checks if a word is valid
     * @param word the word
     * @return whether it is valid or not
     */
    public static boolean checkWord(String word)
    {
        ScrabbleWordChecker.WordResponse wordResponse = CHECKER.isValidWord(word);
        System.out.println(word + " checked : " + wordResponse);
        return wordResponse != null;
    }

    /**
     * Gets the column associated with a letter from the view
     * @param character the character
     * @return the column
     * @throws InvalidInputException whether the column specified is out of bounds
     */
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
            case "O":
                return 14;
            default:
                throw new InvalidInputException(Protocol.Error.E004);
        }
    }

    /**
     * Gets the hardcoded given score of any character
     * @param c the character
     * @return the score
     */
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
     * @param start the start position
     * @param align H(orizontal)/V(ertical)
     * @param word the word
     * @param board the current board state (needed to know which letters of the word are new)
     * @return the list of tiles played
     */
    public static List<Tile> extractTiles(String start, String align, String word, char[][] board)
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
                if(!(column + i < 15 && board[row][column + i] == characters[i]))
                {
                    ret.add(new Tile(row, column + i, characters[i]));
                }
            }
            else if("V".equals(align))
            {
                if(!(row + i < 15 && board[row + i][column] == characters[i]))
                {
                    ret.add(new Tile(row + i, column, characters[i]));
                }
            }
            else
            {
                throw new InvalidInputException(Protocol.Error.E003);
            }
        }
        return ret;
    }

    /**
     * Extracts the tiles for an inline word
     * @param word the word
     * @return a list of tiles
     */
    public static List<Tile> extractInlineTiles(String word)
    {
        List<Tile> ret = new ArrayList<>();

        for (char c : word.toCharArray()) {
            ret.add(new Tile(c));
        }

        return ret;
    }

    /**
     * Tries to decode a REQUESTGAME message
     * @param parts the command sections
     * @return the number of players requested for the game
     * @throws InvalidInputException in case something is wrong about the input
     * @throws InvalidMoveException in case something is wrong about the command
     */
    public static int decodeRequestGame(String[] parts) throws InvalidInputException, InvalidMoveException {
        int numP;

        if(parts.length > 0) {
            if (Protocol.BasicCommand.REQUESTGAME.name().equals(parts[0])) {
                if (parts.length >= 2) {
                    try {
                        numP = Integer.parseInt(parts[1]);
                    } catch (NumberFormatException e) {
                        throw new InvalidInputException(Protocol.Error.E003);
                    }
                    if(numP < 2 || numP > 4)
                        throw new InvalidInputException(Protocol.Error.E003);
                } else {
                    numP = 2;
                }
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

    /**
     * Encodes a message according to the protocol
     * @param name the command name
     * @param parameters the command parameters
     * @return the encoded command
     */
    public static String encodeMessage(String name, String... parameters)
    {
        String cmd = name;
        for (String param : parameters) {
            cmd += Protocol.UNIT_SEPARATOR + param;
        }
        cmd += Protocol.MESSAGE_SEPARATOR;

        return cmd;
    }

    /**
     * Checks if on the current board and with the available tiles there are any possible moves left
     * @param boardState the board state
     * @param availableTiles the available tiles
     * @return
     */
    public static boolean anyPossibleMoves(BoardState boardState, List<Character> availableTiles)
    {
        Map<BoardState, List<Character>> nextPossibleStates = new HashMap<>();
        // First try to place each tile on each available positions
        for(int i = 0; i < 15; i++)
        {
            for(int j = 0; j < 15; j++)
            {
                if(!boardState.isPositionOccupied(i, j) && boardState.isPositionNeighboured(i, j))
                {
                    // Each combination of an available tile and an empty spot generates a possible future state
                    for(char c : availableTiles)
                    {
                        List<Character> newAvailableTiles = new ArrayList<>(availableTiles);
                        newAvailableTiles.remove((Character) c);

                        if(c == '!')
                        {
                            for(char cc : "abcdefghijklmnopqrstuvwxyz".toCharArray())
                            {
                                BoardState clone = boardState.cloneState();
                                if (canLeadToSolution(clone, i, j, cc))
                                {
                                    System.out.println("Possible play:\n" + clone);
                                    return true;
                                }
                                nextPossibleStates.put(clone, newAvailableTiles);
                            }
                        }
                        else
                        {
                            BoardState clone = boardState.cloneState();
                            if (canLeadToSolution(clone, i, j, c))
                            {
                                System.out.println("Possible play:\n" + clone);
                                return true;
                            }
                            nextPossibleStates.put(clone, newAvailableTiles);
                        }
                    }
                }
            }
        }
        // Than iteratively do the same for all the remaining tiles for all the possible generated states
        for(Map.Entry<BoardState, List<Character>> e : nextPossibleStates.entrySet())
        {
            if(anyPossibleMoves(e.getKey(), e.getValue()))
                return true;
        }
        return false;
    }

    /**
     * Checks if a specific move leads to a final solution
     * @param boardState the board state
     * @param i the row
     * @param j the column
     * @param c the letter to place
     * @return whether a solution was found
     */
    private static boolean canLeadToSolution(BoardState boardState, int i, int j, char c)
    {
        try {
            boardState.placeTile(new Tile(i, j, c));

            int score = boardState.getScoreForPlay();
            if(score > 0)
                return true;
        } catch (InvalidMoveException e) {
            // MEANS WORDS ARE NOT VALID
            boardState.setHorizontalWords(new HashMap<>());
            boardState.setVerticalWords(new HashMap<>());
            return false;
        }
        return false;
    }

    /**
     * Checks whether a string is numerical
     * @param strNum the string
     * @return whether it is numerical
     */
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        return NUMBER_PATTERN.matcher(strNum).matches();
    }
}
