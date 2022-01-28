package main.Game;

import lib.Protocol;
import main.Board.View.BoardView;
import main.Common.PlayerInteractor;
import main.Common.Tile;
import main.Exceptions.*;
import main.Player.Controller.PlayerController;
import main.TilePool.View.TilePoolView;

import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Semaphore;

import static java.lang.Thread.sleep;
import static main.Common.GameSpecifics.*;

public class Player implements Runnable{
    private static final char LOCAL_DELIMITER = '-';
    private TilePoolView tilePool;
    private PlayerController playerController;
    private BoardView board;
    private PlayerConnector gameConnector;
    private PlayerInteractor playerInteractor;
    private String name;
    private volatile boolean connected = false;
    private volatile boolean gameOngoing = false;
    private volatile boolean gameRequested = false;
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
        this.stopGame = new Semaphore(0);
        this.playerSem = new Semaphore(0);
    }

    public void openConnection(String address, int port)
    {
        try {
            Socket s = new Socket(address, port);
            this.gameConnector = new PlayerConnector(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void populateInitialBoardView() {
        char[][] initialBoard = new char[15][15];
        for(int i = 0; i < 15; i++)
            Arrays.fill(initialBoard[i], EMPTY_SLOT);
        board.updateView(initialBoard);
    }

    @Override
    public void run() {
        connectToGame();
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

    private void connectToGame() {
        while(!connected)
        {
            // GET USER NAME
            playerController.printMessage("Please enter your name");
            String name = playerController.getInput();
            playerInteractor.updateBoard(board.getBoard());
            gameConnector.sendMessage(buildAnnounce(name));
            // WAITING FOR HELLO
            String message = null;
            try {
                message = gameConnector.getNextMessage();
                playerController.printMessageFromServer(message);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                doAction(message);
            } catch (GameException e) {
                e.printStackTrace();
                playerController.printMessage("Error: "
                        + e.getError() + ": "
                        + Protocol.Error.valueOf(e.getError().name()).getDescription());
            }
        }

        while(!gameRequested)
        {
            playerController.printMessage("Going to request game.\n" +
                    "How many players do you want?(2/3/4)");
            String message = playerController.getInput();
            if(!isNumeric(message)
                    && (Integer.parseInt(message) < 2 || Integer.parseInt(message) > 4))
            {
                playerController.printMessage("Number of players must be between 2 and 4!");
            }
            else
            {
                gameConnector.sendMessage(Protocol.BasicCommand.REQUESTGAME.name() +
                        Protocol.UNIT_SEPARATOR + message.trim() + Protocol.MESSAGE_SEPARATOR);
                gameRequested = true;
            }
        }
    }

    private void listenToPlayer()
    {
        while(connected)
        {
            if(gameOngoing) {
                try {
                    playerSem.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else
            playerController.printMessage("Enter your command!");
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
            try {
                String message = gameConnector.getNextMessage();
                playerController.printMessageFromServer(message);
                doAction(message);
            } catch (GameException e) {
                playerController.printMessage("Error: "
                        + e.getError() + ": "
                        + Protocol.Error.valueOf(e.getError().name()).getDescription());
            } catch (IOException e) {
                connected = false;
                // TODO disconnect from server
                e.printStackTrace();
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

        if (Protocol.BasicCommand.WELCOME.name().equals(parts[0]))
        {
            if (parts.length != 2) {
                throw new InvalidInputException(Protocol.Error.E003);
            }
            playerController.printMessage("Connected to server!");
            connected = true;
            this.name = parts[1];
        }
        else if (Protocol.BasicCommand.INFORMQUEUE.name().equals(parts[0]))
        {
            if (parts.length != 3 && !isNumeric(parts[1]) && !isNumeric(parts[2]))
            {
                throw new InvalidInputException(Protocol.Error.E003);
            }
            msg = "There are currently " + parts[1] + " players in the queue. Game needs "
                    + parts[2];
        }
        else if (Protocol.BasicCommand.STARTGAME.name().equals(parts[0]))
        {
            if (parts.length < 3)
            {
                throw new InvalidInputException(Protocol.Error.E003);
            }
            String players = "";
            for (int i = 1; i < parts.length; i++)
            {
                players += parts[i] + ",";
            }
            msg = "Players:" + players;
            gameOngoing = true;
            playerSem.release();
        } else if (Protocol.BasicCommand.NOTIFYTURN.name().equals(parts[0]))
        {
            if (parts.length != 3 && !isNumeric(parts[1]))
            {
                throw new InvalidInputException(Protocol.Error.E003);
            }
            if (Integer.parseInt(parts[1]) == 1)
            {
                msg = "Your turn!";
                playerSem.release();
            }
            else
            {
                msg = parts[2] + "'s Turn!";
            }
        }
        else if (Protocol.BasicCommand.NEWTILES.name().equals(parts[0]))
        {
            if (parts.length != 2)
            {
                throw new InvalidInputException(Protocol.Error.E003);
            }
            List<Tile> tiles = extractInlineTiles(parts[1]);
            playerController.receiveTiles(tiles);
            msg = "Got tiles " + parts[1];
        }
        else if (Protocol.BasicCommand.INFORMMOVE.name().equals(parts[0]))
        {
            if (parts.length < 3)
            {
                throw new InvalidInputException(Protocol.Error.E003);
            }
            List<Tile> concernedTiles;
            if ("WORD".equals(parts[2]))
            {
                if (parts.length != 6)
                {
                    throw new InvalidInputException(Protocol.Error.E003);
                }
                char[][] boardView = board.getCurrentView();
                concernedTiles = extractTiles(parts[3], parts[4], parts[5]);
                for (Tile t : concernedTiles)
                {
                    boardView[t.getRow()][t.getColumn()] = t.getLetter();
                }
                board.updateView(boardView);
                playerInteractor.updateBoard(board.getBoard());
            }
            else if ("SWAP".equals(parts[2]))
            {
                if (parts.length != 4)
                {
                    throw new InvalidInputException(Protocol.Error.E003);
                }
                concernedTiles = extractInlineTiles(parts[3]);
            }
            else
            {
                throw new InvalidMoveException(Protocol.Error.E002);
            }

            if (name.equals(parts[1]))
            {
                playerController.removeTiles(concernedTiles);
            }
        }
        else if (Protocol.BasicCommand.ERROR.name().equals(parts[0]))
        {
            if (parts.length != 2)
            {
                throw new InvalidInputException(Protocol.Error.E003);
            }
            msg = "Error: " + parts[1] + ": " + Protocol.Error.valueOf(parts[1]).getDescription();
        }
        else if (Protocol.BasicCommand.GAMEOVER.name().equals(parts[0]))
        {
            msg = "Game finished with the status" + parts[1] + ".\nScores:\n";
            for (int i = 2; i < parts.length; i += 2)
            {
                msg += "Player " + parts[i] + ", score: " + parts[i + 1] + "\n";
            }
            stopGame.release();
        }
        else if (Protocol.BasicCommand.PLAYERDISCONNECTED.name().equals(parts[0]))
        {
            msg = parts[1] + " has disconnected";
        }
        else
        {
            throw new InvalidMoveException(Protocol.Error.E002);
        }

        if(msg != null)
            playerController.printMessage(msg);
    }

    private String buildAnnounce(String name)
    {
        return Protocol.BasicCommand.ANNOUNCE.toString() + Protocol.UNIT_SEPARATOR + name + Protocol.MESSAGE_SEPARATOR;
    }
}
