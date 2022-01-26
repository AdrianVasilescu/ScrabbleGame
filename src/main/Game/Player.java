package main.Game;

import lib.Protocol;
import main.Board.View.BoardView;
import main.Common.PlayerInteractor;
import main.Common.Tile;
import main.Exceptions.*;
import main.Player.Controller.PlayerController;
import main.TilePool.View.TilePoolView;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Semaphore;

import static java.lang.Thread.sleep;
import static main.Common.GameSpecifics.extractInlineTiles;
import static main.Common.GameSpecifics.extractTiles;

public class Player implements Runnable{
    private static final char LOCAL_DELIMITER = '-';
    private TilePoolView tilePool;
    private PlayerController playerController;
    private BoardView board;
    private GameConnector gameConnector;
    private PlayerInteractor playerInteractor;
    private String name;
    boolean connected = false;
    private final Semaphore stopGame;
    private final Semaphore playerSem;

    private Thread playerInputThread;
    private Thread serverConnecitonThread;

    /**
     * Instantiates a new player
     */
    public Player()
    {
        this.tilePool = new TilePoolView();
        this.board = new BoardView();
        this.playerInteractor = new PlayerInteractor();
        this.playerController = new PlayerController(playerInteractor);
        populateInitialBoardView();
        playerInteractor.updateBoard(board.printBoard());
        this.gameConnector = new GameConnector();
        this.stopGame = new Semaphore(0);
        this.playerSem = new Semaphore(0);
    }

    private void populateInitialBoardView() {
        char[][] initialBoard = new char[15][15];
        for(int i = 0; i < 15; i++)
            Arrays.fill(initialBoard[i], '_');
        board.updateView(initialBoard);
    }

    @Override
    public void run() {
        connectToServer();
        playerInputThread = new Thread(() -> listenToPlayer());
        serverConnecitonThread = new Thread(() -> listenToServer());
        serverConnecitonThread.start();

        try {
            // WAIT FOR GAME TO START
            playerSem.acquire();
            playerInputThread.start();

            // GAME ONGOING
            stopGame.acquire();

            // STOP THE GAME
            stopGame();
            playerInputThread.join();
            serverConnecitonThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void stopGame() {
        playerInputThread.interrupt();
        serverConnecitonThread.interrupt();
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
                doAction(message);
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
                doAction(message);
            } catch (GameException e) {
                handleError(e.getError().name());
            }
            try {
                sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void doAction(String message)
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
                throw new InvalidMoveException(Protocol.Error.E002);
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
                throw new InvalidMoveException(Protocol.Error.E002);
            case NEWTILES:
                // TODO validate command
                List<Tile> tiles = extractInlineTiles(parts[1]);
                playerController.receiveTiles(tiles);
                msg = "Got tiles " + parts[1];
                break;
            case INFORMMOVE:
                // TODO validate command
                // TODO keep summary of board on player side
                List<Tile> concernedTiles;
                if("WORD".equals(parts[2]))
                {
                    char [][] boardView = board.getCurrentView();
                    concernedTiles = extractTiles(parts[3], parts[4], parts[5]);
                    for(Tile t : concernedTiles)
                    {
                        boardView[t.getRow()][t.getColumn()] = t.getLetter();
                    }
                    board.updateView(boardView);
                    playerInteractor.updateBoard(board.printBoard());
                }
                else if ("SWAP".equals(parts[2]))
                {
                    concernedTiles = extractInlineTiles(parts[3]);
                }
                else
                {
                    throw new InvalidMoveException(Protocol.Error.E002);
                }

                if(name.equals(parts[1]))
                {
                    playerController.removeTiles(concernedTiles);
                }
                break;
            case ERROR:
                // TODO validate error
                handleError(parts[1]);
                msg = "Error: " + parts[1];
                break;
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
                msg = "A player has disconnected";
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
