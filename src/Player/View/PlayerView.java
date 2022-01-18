package Player.View;

import java.util.List;

/**
 * The Player view - handles exposing the player data
 */
public class PlayerView {
    private String id;
    public char[] availableTiles;
    /**
     * Updates the view
     */
    public void updateView(String id, char[] availableTiles)
    {
        this.id = id;
        this.availableTiles = availableTiles;
    }

    /**
     * Prints the board
     */
    public void printPlayer()
    {
        System.out.print("Player " + id + " - Available tiles: [");
        for(char c : availableTiles)
        {
            System.out.print("[" + c + "]");
        }
        System.out.println("]");
    }
}
