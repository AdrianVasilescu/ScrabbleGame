package main.Game;

import lib.Protocol;
import main.Board.Controller.BoardController;
import main.Common.Tile;
import main.Exceptions.*;
import main.TilePool.Controller.TilePoolController;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Semaphore;

import static main.Game.GameSpecifics.*;

public class GameSession implements Runnable{
    private BoardController boardController;
    private TilePoolController tilePoolController;
    private List<PlayerSession> players;
    private Semaphore turnEnd;
    private int currentPlayer;
    private volatile boolean gameOn;
    private final Object turn;
    private volatile boolean disconnect = false;

    public GameSession(List<PlayerSession> players) {
        this.players = players;
        this.boardController = new BoardController();
        this.tilePoolController = new TilePoolController();
        this.turn = new Object();
        this.turnEnd = new Semaphore(-1);
    }

    @Override
    public void run() {
        //START GAME
        String startGameParams = "";
        currentPlayer = -1;
        gameOn = true;

        try {
            for (int i = 0; i < players.size(); i++) {
                int id = i;
                PlayerSession s = players.get(i);
                Thread t = new Thread(() -> {
                    try {
                        listen(s, id);
                    } catch (IOException e) {
                        System.out.println("Connection failure on session(" + s.getName() +"): " + e.getMessage());
                    }
                });
                s.setPlayerThread(t);
                startGameParams += Protocol.UNIT_SEPARATOR + players.get(i).getName();
                t.start();
            }
            broadcast(encodeMessage(Protocol.BasicCommand.STARTGAME.name(),
                    startGameParams.substring(1)));
            for (PlayerSession player : players) {
                String firstTiles = tilePoolController.getTilesFromPool(7);
                player.sendMessage(encodeMessage(Protocol.BasicCommand.NEWTILES.name(), firstTiles));
                player.addTiles(firstTiles);
            }
            synchronized (turn) {
                passTurn();
            }
            // GAME ONGOING

            //Check game status
            do {
                turnEnd.acquire();
            } while (gameOnGoing());

            broadcast(buildGameover());
        } catch (IOException | InterruptedException e) {
            System.out.println("Error running the game:" + e.getMessage());
        }
        // END GAME
        for(PlayerSession s : players)
        {
            s.endSession();
        }
    }

    private String buildGameover() {
        String scores = "";
        String result = "WIN";

        for(PlayerSession p : players)
        {
            scores +=  Protocol.UNIT_SEPARATOR + p.getName() + Protocol.UNIT_SEPARATOR;
            if(p.isDisconnected())
            {
                result = "DISCONNECT";
                scores += "0";
            }
            else
            {
                scores += p.getScore();
            }
        }
        return encodeMessage(Protocol.BasicCommand.GAMEOVER.name(),
                result + scores);
    }

    private boolean gameOnGoing() {
        if(disconnect)
        {
            return false;
        }

        if(!tilePoolController.isEmpty())
        {
            return true;
        }

        for(PlayerSession p : players)
        {
         if(anyPossibleMoves(boardController.getBoardStateSnapshot(), p.getTiles()))
             return true;
        }

        return false;
    }

    private void listen(PlayerSession s, int id) throws IOException {
        String message = "";
        while(gameOn)
        {
            try{
                message = s.getNextMessage();
                synchronized (turn)
                {
                    if (currentPlayer == id) {
                        s.addScore(doAction(message, s));
                        passTurn();
                    } else {
                        s.sendMessage(encodeMessage(Protocol.BasicCommand.ERROR.name(), Protocol.Error.E009.name()));
                    }
                }
            }
            catch (GameException e)
            {
                s.sendMessage(encodeMessage(Protocol.BasicCommand.ERROR.name(), e.getError().name()));
                if(e.isReturnTiles())
                {
                    s.sendMessage((encodeMessage(Protocol.BasicCommand.NEWTILES.name(),
                            extractTilesFromCommand(message))));
                }
                passTurn();
            } catch (IOException e) {
                // PLAYER DISCONNECTED
                disconnect = true;
                gameOn = false;
                s.disconnect();
                broadcast(encodeMessage(Protocol.BasicCommand.PLAYERDISCONNECTED.name(),
                        s.getName()));
                turnEnd.release();
                break;
            }
        }
    }

    private void passTurn() throws IOException {
        currentPlayer = (currentPlayer + 1) % players.size();
        String playerName = players.get(currentPlayer).getName();
        for(int j = 0; j < players.size(); j++)
        {
            int val = 0;
            if(j == currentPlayer)
            {
                val = 1;
            }
            players.get(j)
                    .sendMessage(encodeMessage(Protocol.BasicCommand.NOTIFYTURN.name(),
                            val + "", playerName));
        }
        turnEnd.release();
    }

    private void broadcast(String message) throws IOException
    {
        for(PlayerSession s : players)
        {
            if(!s.isDisconnected())
            {
                s.sendMessage(message);
            }
        }
    }

    private int doAction(String message, PlayerSession s)
            throws InvalidInputException, InitialWordNotOnCenterException, InvalidMoveException, NotEnoughTilesException, IOException {
        if(message == null)
            return 0;

        int score = 0;
        String[] parts = message.split(String.valueOf(Protocol.UNIT_SEPARATOR));
        if (Protocol.BasicCommand.valueOf(parts[0]) == Protocol.BasicCommand.MAKEMOVE)
        {
            if (parts.length < 2) {
                throw new InvalidMoveException(Protocol.Error.E003);
            }
            String shouldReceiveTiles = "";
            String informMessage = "";
            if (parts[1].equals("WORD")) {
                if (parts.length != 5) {
                    throw new InvalidMoveException(Protocol.Error.E003);
                }
                List<Tile> tiles = extractTiles(parts[2], parts[3], parts[4], boardController.getBoard());
                if (tiles.size() == 0)
                    throw new InvalidInputException(Protocol.Error.E005);
                else if(!s.hasTiles(tiles))
                    throw new InvalidInputException(Protocol.Error.E008);

                score = boardController.handleMove(tiles);
                s.removeTiles(tiles);
                shouldReceiveTiles = tilePoolController.getTilesFromPool(tiles.size());
                informMessage = encodeMessage(Protocol.BasicCommand.INFORMMOVE.name(), s.getName(),
                        parts[1], parts[2], parts[3], parts[4]);
            } else if (parts[1].equals("SWAP")) {
                if (parts.length != 3) {
                    throw new InvalidMoveException(Protocol.Error.E003);
                }
                if (!s.hasTiles(parts[2])) {
                    throw new InvalidMoveException(Protocol.Error.E008);
                }
                shouldReceiveTiles = tilePoolController.swapTiles(parts[2].toCharArray());
                informMessage = encodeMessage(Protocol.BasicCommand.INFORMMOVE.name(), s.getName(),
                        parts[1], parts[2]);
            }

            if(!shouldReceiveTiles.isEmpty())
            {
                s.sendMessage(encodeMessage(Protocol.BasicCommand.NEWTILES.name(), shouldReceiveTiles));
                s.addTiles(shouldReceiveTiles);
            }
            broadcast(informMessage);
        } else {
            throw new InvalidMoveException(Protocol.Error.E002);
        }
        return score;
    }
}
