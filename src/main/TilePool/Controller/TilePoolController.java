package main.TilePool.Controller;

import main.Common.Tile;
import main.Exceptions.NotEnoughTilesException;
import main.TilePool.View.TilePoolView;
import main.TilePool.Model.TilePoolModel;

import java.util.List;

/**
 * The tile pool controller
 */
public class TilePoolController {
    /**
     * The tile pool model
     */
    private TilePoolModel tilePoolModel;

    /**
     * The tile pool view
     */
    private TilePoolView tilePoolView;

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
}
