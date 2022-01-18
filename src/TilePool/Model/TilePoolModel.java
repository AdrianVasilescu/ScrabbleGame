package TilePool.Model;

import Common.Tile;
import Exceptions.NotEnoughTilesException;

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
        tileStack.addAll(Collections.nCopies(9, new Tile('A', 1)));
        tileStack.addAll(Collections.nCopies(2, new Tile('B', 3)));
        tileStack.addAll(Collections.nCopies(2, new Tile('C', 3)));
        tileStack.addAll(Collections.nCopies(4, new Tile('D', 2)));
        tileStack.addAll(Collections.nCopies(12, new Tile('E', 1)));
        tileStack.addAll(Collections.nCopies(2, new Tile('F', 4)));
        tileStack.addAll(Collections.nCopies(2, new Tile('G', 2)));
        tileStack.addAll(Collections.nCopies(2, new Tile('H', 4)));
        tileStack.addAll(Collections.nCopies(8, new Tile('I', 1)));
        tileStack.addAll(Collections.nCopies(2, new Tile('J', 8)));
        tileStack.addAll(Collections.nCopies(2, new Tile('K', 5)));
        tileStack.addAll(Collections.nCopies(4, new Tile('L', 1)));
        tileStack.addAll(Collections.nCopies(2, new Tile('M', 3)));
        tileStack.addAll(Collections.nCopies(6, new Tile('N', 1)));
        tileStack.addAll(Collections.nCopies(8, new Tile('O', 1)));
        tileStack.addAll(Collections.nCopies(2, new Tile('P', 3)));
        tileStack.addAll(Collections.nCopies(1, new Tile('Q', 10)));
        tileStack.addAll(Collections.nCopies(6, new Tile('R', 1)));
        tileStack.addAll(Collections.nCopies(4, new Tile('S', 1)));
        tileStack.addAll(Collections.nCopies(6, new Tile('T', 1)));
        tileStack.addAll(Collections.nCopies(4, new Tile('U', 1)));
        tileStack.addAll(Collections.nCopies(2, new Tile('V', 4)));
        tileStack.addAll(Collections.nCopies(2, new Tile('W', 4)));
        tileStack.addAll(Collections.nCopies(1, new Tile('X', 8)));
        tileStack.addAll(Collections.nCopies(2, new Tile('Y', 4)));
        tileStack.addAll(Collections.nCopies(1, new Tile('Z', 10)));
        tileStack.addAll(Collections.nCopies(2, new Tile('*', 0)));
    }
}
