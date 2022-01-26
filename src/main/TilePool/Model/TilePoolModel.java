package main.TilePool.Model;

import main.Common.Tile;
import main.Exceptions.NotEnoughTilesException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * The tile pool model
 */
public class TilePoolModel {
    /**
     * The available tiles
     */
    List<Tile> tileStack;

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
    public List<Tile> getTilesFromPool(int numTiles)
    {
        List<Tile> tiles = new ArrayList<>();
        Random rand = new Random();

        while (numTiles > 0 && !tileStack.isEmpty())
        {
            tiles.add(tileStack.remove(rand.nextInt(tileStack.size() - 1)));
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
    public List<Tile> swapTiles(List<Tile> tiles) throws NotEnoughTilesException {
        if(tileStack.size() < tiles.size())
        {
            throw new NotEnoughTilesException();
        }
        List<Tile> ret = this.getTilesFromPool(tiles.size());
        this.tileStack.addAll(tiles);

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
            tiles[i] = tileStack.get(i).getLetter();
        }

        return tiles;
    }

    /**
     * Initializes the tile stack
     */
    private void initializeTileStack() {
        tileStack = new ArrayList<>();
        tileStack.addAll(Collections.nCopies(9, new Tile('A')));
        tileStack.addAll(Collections.nCopies(2, new Tile('B')));
        tileStack.addAll(Collections.nCopies(2, new Tile('C')));
        tileStack.addAll(Collections.nCopies(4, new Tile('D')));
        tileStack.addAll(Collections.nCopies(12, new Tile('E')));
        tileStack.addAll(Collections.nCopies(2, new Tile('F')));
        tileStack.addAll(Collections.nCopies(2, new Tile('G')));
        tileStack.addAll(Collections.nCopies(2, new Tile('H')));
        tileStack.addAll(Collections.nCopies(8, new Tile('I')));
        tileStack.addAll(Collections.nCopies(2, new Tile('J')));
        tileStack.addAll(Collections.nCopies(2, new Tile('K')));
        tileStack.addAll(Collections.nCopies(4, new Tile('L')));
        tileStack.addAll(Collections.nCopies(2, new Tile('M')));
        tileStack.addAll(Collections.nCopies(6, new Tile('N')));
        tileStack.addAll(Collections.nCopies(8, new Tile('O')));
        tileStack.addAll(Collections.nCopies(2, new Tile('P')));
        tileStack.addAll(Collections.nCopies(1, new Tile('Q')));
        tileStack.addAll(Collections.nCopies(6, new Tile('R')));
        tileStack.addAll(Collections.nCopies(4, new Tile('S')));
        tileStack.addAll(Collections.nCopies(6, new Tile('T')));
        tileStack.addAll(Collections.nCopies(4, new Tile('U')));
        tileStack.addAll(Collections.nCopies(2, new Tile('V')));
        tileStack.addAll(Collections.nCopies(2, new Tile('W')));
        tileStack.addAll(Collections.nCopies(1, new Tile('X')));
        tileStack.addAll(Collections.nCopies(2, new Tile('Y')));
        tileStack.addAll(Collections.nCopies(1, new Tile('Z')));
        tileStack.addAll(Collections.nCopies(2, new Tile('!')));
    }
}
