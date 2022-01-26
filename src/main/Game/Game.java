package main.Game;


import lib.Protocol;
import main.Board.Controller.BoardController;
import main.Common.PlayerInteractor;
import main.Common.Tile;
import main.Exceptions.*;
import main.Player.Controller.PlayerController;
import main.Player.Model.PlayerModel;
import main.TilePool.Controller.TilePoolController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import static java.lang.Thread.sleep;

/**
 * The Game
 */
public class Game implements Runnable{
    private static final char LOCAL_DELIMITER = '-';
    private TilePoolController tilePoolController;
    private PlayerController playerController;
    private BoardController boardController;
    private GameConnector gameConnector;
    private PlayerInteractor playerInteractor;
    private String name;
    boolean connected = false;
    private final Semaphore stopGame;
    private final Semaphore playerSem;

    Thread playerThread;
    Thread serverThread;

    /**
     * Instantiates a new game
     */
    public Game()
    {
        this.tilePoolController = new TilePoolController();
        this.playerInteractor = new PlayerInteractor();
        this.playerController = new PlayerController(playerInteractor);
        this.boardController = new BoardController();
        boardController.mapModelDataToViewData();
        playerInteractor.updateBoard(boardController.printBoard());
        this.gameConnector = new GameConnector();
        this.stopGame = new Semaphore(0);
        this.playerSem = new Semaphore(0);
    }

    @Override
    public void run() {
        connectToServer();
        playerThread = new Thread(() -> listenToPlayer());
        serverThread = new Thread(() -> listenToServer());
        serverThread.start();

        try {
            // WAIT FOR GAME TO START
            playerSem.acquire();
            playerThread.start();

            // GAME ONGOING
            stopGame.acquire();

            // STOP THE GAME
            stopGame();
            playerThread.join();
            serverThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void stopGame() {
        playerThread.interrupt();
        serverThread.interrupt();
    }

    private void connectToServer() {
        while(!connected)
        {
            // GET USER NAME
            playerController.printMessage("Please enter your name");
            String name = playerController.getInput();
            gameConnector.sendMessage(buildAnnounce(name));
            // WAITING FOR HELLO
            String message = gameConnector.getNextMessage();

            try {
                decodeMessage(message);
            } catch (GameException e) {
                e.printStackTrace();
                // TODO
            }
        }
    }

    private void listenToPlayer()
    {
        while(connected)
        {
            try {
                playerSem.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String command = playerController.getInput();
            playerController.printMessage("Processing Command...");
            gameConnector.sendMessage(prepareLocalCommand(command));
            playerController.printMessage("Command sent to server!");
        }
    }

    private String prepareLocalCommand(String command) {
        return command
                .replace(LOCAL_DELIMITER, Protocol.UNIT_SEPARATOR).trim() + Protocol.MESSAGE_SEPARATOR;
    }

    private void listenToServer()
    {
        while(connected)
        {
            String message = gameConnector.getNextMessage();
            try {
                sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("GOT message: " + message);
        }
    }

    private void decodeMessage(String message)
            throws InvalidInputException, InitialWordNotOnCenterException,
            InvalidMoveException, NotEnoughTilesException {
        if(message == null)
            return;

        String[] parts = message.split(String.valueOf(Protocol.UNIT_SEPARATOR));
        String msg = null;
        switch (Protocol.BasicCommand.valueOf(parts[0]))
        {
            case WELCOME:
                //TODO validate command
                playerController.printMessage("Connected to server!");
                connected = true;
                this.name = parts[1];
            case REQUESTGAME:
                //TODO validate command
                break;
            case INFORMQUEUE:
                //TODO validate command
                // Nothing to do
                msg = "There are currently " + parts[1] + " players in the queue. Game needs "
                        + parts[2];
                break;
            case STARTGAME:
                //TODO validate command
                String players = "";
                for(int i = 1; i < parts.length; i++)
                {
                    players += parts[i] + ",";
                    i++;
                }
                msg = "Players:" + players;
                playerSem.release();
                break;
            case NOTIFYTURN:
                //TODO validate command
                msg = "Your turn!";
                playerSem.release();
                break;
            case MAKEMOVE:
                //TODO validate command
                if(parts[1].equals("WORD"))
                {
                    boardController.handleMove(parts[2], parts[3], parts[4]);
                }
                else if(parts[1].equals("SWAP"))
                {
                    List<Tile> tiles = new ArrayList<>();
                    for(char c : parts[2].toCharArray())
                    {
                        Tile tile = new Tile(c);
                        //TODO keep player data on server and check if he owns them
                        if(!true)
                        {
                            throw new InvalidInputException(Protocol.Error.E008);
                        }
                        tiles.add(tile);
                    }
                    List<Tile> newTiles = tilePoolController.swapTiles(tiles);
                    //TODO send to player
                }
                break;
            case NEWTILES:
                // TODO validate command
                List<Tile> tiles = new ArrayList<>();
                for(char c : parts[1].toCharArray())
                {
                    Tile tile = new Tile(c);
                    tiles.add(tile);
                }
                playerController.receiveTiles(tiles);
                msg = "Got tiles " + parts[1];
                break;
            case INFORMMOVE:
                // TODO validate command
                // TODO keep summary of board on player side
                break;
            case ERROR:
                // TODO validate error
                handleError(parts[1]);
                msg = "Error: " + parts[1];
            case GAMEOVER:
                // TODO validate GAME OVER
                msg = "Game finished with the status" + parts[1] + ".\nScores:\n";
                for(int i = 2; i < parts.length; i += 2)
                {
                    msg += "Player " + parts[i] + ", score: " + parts[i+1] + "\n";
                }
                stopGame.release();
                break;
            case PLAYERDISCONNECTED:
                //TODO
                break;
        }
        if(msg != null)
            playerController.printMessageFromServer(msg);
    }

    private void handleError(String errorCode) {
        Protocol.Error error = Protocol.Error.valueOf(errorCode);
        switch (error)
        {
            case E001:
                connected = false;
            case E002:
            case E003:
            case E004:
            case E005:
            case E006:
            case E007:
            case E008:
            case E009:
                playerController.printMessageFromServer(error.getDescription());
        }
    }

    private String buildAnnounce(String name)
    {
        return Protocol.BasicCommand.ANNOUNCE.toString() + Protocol.UNIT_SEPARATOR + name + Protocol.MESSAGE_SEPARATOR;
    }
}
