package main.Game;


import lib.Protocol;
import main.Board.Controller.BoardController;
import main.Common.PlayerInputService;
import main.Player.Controller.PlayerController;
import main.TilePool.Controller.TilePoolController;

/**
 * The Game
 */
public class Game implements Runnable{
    private TilePoolController tilePoolController;
    private PlayerController playerController;
    private BoardController boardController;
    private GameConnector gameConnector;

    /**
     * Instantiates a new game
     */
    public Game()
    {
        this.tilePoolController = new TilePoolController();
        this.playerController = new PlayerController();
        this.boardController = new BoardController();
        this.gameConnector = new GameConnector();
    }

    @Override
    public void run() {
        // GET USER NAME
        String name = playerController.getInput("Please enter your name:");
        gameConnector.sendMessage(buildAnnounce(name));

        //wait for WELCOME

        while(true) // TODO
        {

        }
    }

    private String buildAnnounce(String name)
    {
        return Protocol.BasicCommand.ANNOUNCE.toString() + Protocol.UNIT_SEPARATOR + name + Protocol.MESSAGE_SEPARATOR;
    }
}
