package main.TilePool.View;

/**
 * The tile pool view - handles exposing the tile pool
 */
public class TilePoolView {
    /**
     * The available tiles
     */
    char[] tiles;

    /**
     * Updates the view data
     * @param tilesViewData
     */
    public void updateView(char[] tilesViewData) {
        this.tiles = tilesViewData;
    }

    /**
     * Prints the tile pool
     */
    public void printTilePool()
    {
        System.out.println("\nTILES POOL:\n");
        int spacingCount = 0;
        System.out.print("[");
        for(char c : tiles)
        {
            if(spacingCount == 20) {
                System.out.print("\n");
                spacingCount = 0;
            }
            System.out.print("[" + c + "]");
            spacingCount++;
        }
        System.out.println("]");
    }
}
