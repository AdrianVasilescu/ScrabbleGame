package Common;

/**
 * The tile
 */
public class Tile {
    /**
     * Row to be placed on (-1 when still in bag / pool)
     */
    private int row = -1;
    /**
     * Column to be placed on (-1 when still in bag / pool)
     */
    private int column = -1;
    /**
     * The letter on the tile
     */
    private char letter;
    /**
     * The score the tile offers
     */
    private final int score;

    /**
     * Instantiates an unplaced tile
     * @param letter the letter on the tile
     * @param score the score the tile offers
     */
    public Tile(char letter, int score) {
        this.letter = letter;
        this.score = score;
    }

    /**
     * Gets the row
     * @return the row
     */
    public int getRow() {
        return row;
    }

    /**
     * Sets the row
     * @param row
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * Gets the column
     * @return the column
     */
    public int getColumn() {
        return column;
    }

    /**
     * Sets the column
     * @param column
     */
    public void setColumn(int column) {
        this.column = column;
    }

    /**
     * Gets the letter
     * @return the letter
     */
    public char getLetter() {
        return letter;
    }

    /**
     * Sets the letter
     * (this should only be possible when current tile is BLANK and has not been played yet)
     * @param letter
     */
    public void setLetter(char letter)
    {
        if(this.letter == '*')
        {
            this.letter = letter;
        }
    }

    /**
     * Gets the score that this tile offers
     * @return the score
     */
    public int getScore() {
        return score;
    }

    @Override
    public String toString()
    {
        return "[" + row + "]" + "[" + column + "] =" + letter + "";
    }
}
