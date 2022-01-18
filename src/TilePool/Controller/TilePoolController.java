package TilePool.Controller;

import Common.Tile;
import Exceptions.NotEnoughTilesException;
import TilePool.Model.TilePoolModel;
import TilePool.View.TilePoolView;

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
    public List<Tile> getTilesFromPool(int numTiles)
    {
        return tilePoolModel.getTilesFromPool(numTiles);
    }

    /**
     * Randomly retrieves numTiles from the board tileStack
     * @param tiles
     * @return swapped tiles
     * @throws NotEnoughTilesException if there are not enough tiles in the pool for the swap
     */
    public List<Tile> swapTiles(List<Tile> tiles) throws NotEnoughTilesException {
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
