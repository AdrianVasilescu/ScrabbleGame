package main.Common;

import java.util.Objects;

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
     * Instantiates an unplaced tile
     * @param letter the letter on the tile
     */
    public Tile(char letter) {
        this.letter = letter;
    }

    /**
     * Instantiates a placed tile
     * @param letter the letter on the tile
     * @param row
     * @param column
     */
    public Tile(int row, int column, char letter) {
        this.row = row;
        this.column = column;
        this.letter = letter;
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
        if(this.letter == '!')
        {
            this.letter = letter;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tile)) return false;
        Tile tile = (Tile) o;
        return row == tile.row && column == tile.column && letter == tile.letter;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column, letter);
    }

    @Override
    public String toString()
    {
        return "[" + row + "]" + "[" + column + "] =" + letter + "";
    }
}
