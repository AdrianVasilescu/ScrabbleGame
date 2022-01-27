package main.Player.View;

/**
 * The java.Player view - handles exposing the player data
 */
public class PlayerView {
    private String id;
    public String availableTiles;
    /**
     * Updates the view
     */
    public void updateView(String id, String availableTiles)
    {
        this.id = id;
        this.availableTiles = availableTiles;
    }

    /**
     * Prints the board
     */
    public void printPlayer()
    {
        //TODO might remove this class
    }
}
