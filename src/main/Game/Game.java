package main.Game;


import lib.Protocol;
import main.Board.Controller.BoardController;
import main.Common.PlayerInteractor;
import main.Player.Controller.PlayerController;
import main.TilePool.Controller.TilePoolController;

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
    private Semaphore stopGame;

    Thread playerThread;
    Thread gameThread;

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
    }

    @Override
    public void run() {
        connectToServer();
        playerThread = new Thread(() -> listenToPlayer());
        gameThread = new Thread(() -> listenToServer());
        playerThread.start();
        gameThread.start();

        try {
            //GAME ONGOING
            stopGame.acquire();

            //STOP THE GAME
            stopGame();
            playerThread.join();
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void stopGame() {
        playerThread.interrupt();
        gameThread.interrupt();
    }

    private void connectToServer() {
        while(!connected)
        {
            // GET USER NAME

            System.out.println("Please enter your name:");
            String name = playerController.getInput();
            gameConnector.sendMessage(buildAnnounce(name));
            try {
                sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // WAITING FOR HELLO
            String message = gameConnector.getNextMessage();

            decodeMessage(message);
        }
    }
    private void listenToPlayer()
    {
        while(connected)
        {
            String command = playerController.getInput();
            gameConnector.sendMessage(prepareLocalCommand(command));
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

    private void decodeMessage(String message) {
        if(message == null)
            return;

        String[] parts = message.split(String.valueOf(Protocol.UNIT_SEPARATOR));

        switch (Protocol.BasicCommand.valueOf(parts[0]))
        {
            case WELCOME:
            {
                System.out.println("Connected to server!");
                connected = true;
                this.name = parts[1];
            }
            case REQUESTGAME:
                break;
            case INFORMQUEUE:
                break;
            case STARTGAME:
                break;
            case NOTIFYTURN:
                break;
            case MAKEMOVE:
                break;
            case NEWTILES:
                break;
            case INFORMMOVE:
                break;
            case ERROR:
                handleError(parts[1]);
            case GAMEOVER:
                break;
            case PLAYERDISCONNECTED:
                break;
        }
    }

    private void handleError(String errorCode) {
        Protocol.Error error = Protocol.Error.valueOf(errorCode);
        switch (error)
        {
            case E001:
                connected = false;
                System.out.println(error.getDescription());
                break;
            case E002:
                break;
            case E003:
                break;
            case E004:
                break;
            case E005:
                break;
            case E006:
                break;
            case E007:
                break;
            case E008:
                break;
            case E009:
                break;
        }
    }

    private String buildAnnounce(String name)
    {
        return Protocol.BasicCommand.ANNOUNCE.toString() + Protocol.UNIT_SEPARATOR + name + Protocol.MESSAGE_SEPARATOR;
    }
}
