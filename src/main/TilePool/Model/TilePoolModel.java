package main.TilePool.Model;

import main.Exceptions.NotEnoughTilesException;

import java.util.*;

/**
 * The tile pool model
 */
public class TilePoolModel {
    /**
     * The available tiles
     */
    List<Character> tileStack;

    public TilePoolModel()
    {
        initializeTileStack();
        System.out.println("Tile pool ready!");
    }

    /**
     * Returns a number of random tiles from the tiles stack
     * @param numTiles
     * @return
     */
    public String getTilesFromPool(int numTiles)
    {
        String tiles = "";
        Random rand = new Random();

        while (numTiles > 0 && !tileStack.isEmpty())
        {
            tiles += tileStack.remove(rand.nextInt(tileStack.size()));
            numTiles--;
        }

        return tiles;
    }

    /**
     * Swap tiles
     * @param tiles
     * @return the swapped tiles
     * @throws NotEnoughTilesException if there are not enough tiles in the stack
     */
    public String swapTiles(char[] tiles) throws NotEnoughTilesException {
        if(tileStack.size() < tiles.length)
        {
            throw new NotEnoughTilesException();
        }
        String ret = this.getTilesFromPool(tiles.length);
        for(char c : tiles)
        {
            tileStack.add(c);
        }

        return ret;
    }

    /**
     * Returns the available tiles
     * @return
     */
    public char[] getTilesViewData() {
        char[] tiles = new char[tileStack.size()];
        for(int i = 0; i < tileStack.size(); i++)
        {
            tiles[i] = tileStack.get(i);
        }

        return tiles;
    }

    /**
     * Initializes the tile stack
     */
    private void initializeTileStack() {
        tileStack = new ArrayList<>();
        /*
        tileStack.addAll(Collections.nCopies(9, 'A'));
        tileStack.addAll(Collections.nCopies(2, 'B'));
        tileStack.addAll(Collections.nCopies(2, 'C'));
        tileStack.addAll(Collections.nCopies(4, 'D'));
        tileStack.addAll(Collections.nCopies(12,'E'));
        tileStack.addAll(Collections.nCopies(2,'F'));
        tileStack.addAll(Collections.nCopies(2, 'G'));
        tileStack.addAll(Collections.nCopies(2, 'H'));
        tileStack.addAll(Collections.nCopies(8, 'I'));
        tileStack.addAll(Collections.nCopies(2, 'J'));
        tileStack.addAll(Collections.nCopies(2, 'K'));
        tileStack.addAll(Collections.nCopies(4, 'L'));
        tileStack.addAll(Collections.nCopies(2, 'M'));
        tileStack.addAll(Collections.nCopies(6, 'N'));
        tileStack.addAll(Collections.nCopies(8, 'O'));
        tileStack.addAll(Collections.nCopies(2, 'P'));
        tileStack.addAll(Collections.nCopies(1, 'Q'));
        tileStack.addAll(Collections.nCopies(6, 'R'));
        tileStack.addAll(Collections.nCopies(4, 'S'));
        tileStack.addAll(Collections.nCopies(6, 'T'));
        tileStack.addAll(Collections.nCopies(4, 'U'));
        tileStack.addAll(Collections.nCopies(2, 'V'));
        tileStack.addAll(Collections.nCopies(2, 'W'));
        tileStack.addAll(Collections.nCopies(1, 'X'));
        tileStack.addAll(Collections.nCopies(2, 'Y'));
        tileStack.addAll(Collections.nCopies(1, 'Z'));
        tileStack.addAll(Collections.nCopies(2, '!'));
        */

        //FOR SHORT GAMES - TESTING
        tileStack.addAll(Collections.nCopies(3, 'B'));
        tileStack.addAll(Collections.nCopies(4, 'A'));
        tileStack.addAll(Collections.nCopies(4, 'D'));
        tileStack.addAll(Collections.nCopies(3, 'E'));
        tileStack.addAll(Collections.nCopies(2, 'S'));
        tileStack.addAll(Collections.nCopies(1, '!'));

    }

    public boolean isEmpty() {
        return this.tileStack.isEmpty();
    }
}
