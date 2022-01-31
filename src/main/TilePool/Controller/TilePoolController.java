package main.TilePool.Controller;

import main.Exceptions.NotEnoughTilesException;
import main.TilePool.Model.TilePoolModel;
import main.TilePool.View.TilePoolView;

/**
 * The tile pool controller
 */
public class TilePoolController {
    /**
     * The tile pool model
     */
    private final TilePoolModel tilePoolModel;

    /**
     * The tile pool view
     */
    private final TilePoolView tilePoolView;

    /**
     * Instantiates a new tile pool controller
     */
    public TilePoolController()
    {
        this.tilePoolModel = new TilePoolModel();
        this.tilePoolView = new TilePoolView();
        updateView();
    }

    /**
     * Randomly retrieves numTiles from the board tileStack
     * @param numTiles
     * @return retrieved numTiles tiles
     */
    public String getTilesFromPool(int numTiles)
    {
        return tilePoolModel.getTilesFromPool(numTiles);
    }

    /**
     * Randomly retrieves numTiles from the board tileStack
     * @param tiles
     * @return swapped tiles
     * @throws NotEnoughTilesException if there are not enough tiles in the pool for the swap
     */
    public String swapTiles(char[] tiles) throws NotEnoughTilesException {
        return tilePoolModel.swapTiles(tiles);
    }

    /**
     * Updates the view data
     */
    private void updateView()
    {
     tilePoolView.updateView(tilePoolModel.getTilesViewData());
     tilePoolView.printTilePool();
    }

    /**
     * Checks if the tile pool is empty
     * @return whether the tile pool is empty
     */
    public boolean isEmpty() {
        return this.tilePoolModel.isEmpty();
    }
}
