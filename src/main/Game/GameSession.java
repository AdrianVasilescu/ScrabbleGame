package main.Game;

import lib.Protocol;
import main.Board.Controller.BoardController;
import main.Exceptions.*;
import main.TilePool.Controller.TilePoolController;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Semaphore;

import static main.Common.GameSpecifics.encodeMessage;
import static main.Common.GameSpecifics.extractTilesFromCommand;

public class GameSession implements Runnable{
    private BoardController boardController;
    private TilePoolController tilePoolController;
    private List<PlayerSession> players;
    private Semaphore turnEnd;
    private int currentPlayer;
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

        for(int i = 0; i < players.size(); i++)
        {
            int id = i;
            PlayerSession s = players.get(i);
            Thread t = new Thread(() -> listen(s, id));
            s.setPlayerThread(t);
            startGameParams += Protocol.UNIT_SEPARATOR + players.get(i).getName();
        }
        broadcast(encodeMessage(Protocol.BasicCommand.STARTGAME.name(),
                startGameParams.substring(1)));
        for(PlayerSession player : players)
        {
            player.sendMessage(encodeMessage(Protocol.BasicCommand.NEWTILES.name(),
                    tilePoolController.getTilesFromPool(7)));
        }
        synchronized (turn)
        {
            passTurn();
        }
        // GAME ONGOING

        //Check game status
        do
        {
            try {
                turnEnd.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while(gameOnGoing());

        // END GAME
        for(PlayerSession s : players)
        {
            s.endSession();
        }
    }

    private boolean gameOnGoing() {
        //TODO find when there are no more moves
        return disconnect;
    }

    private void listen(PlayerSession s, int id) {
        String message = "";
        while(true)
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
                broadcast(encodeMessage(Protocol.BasicCommand.PLAYERDISCONNECTED.name(),
                        s.getName()));
                s.endSession();
                turnEnd.release();
            }
        }
    }

    private void passTurn() {
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

    private void broadcast(String message) {
        for(PlayerSession s : players)
        {
            s.sendMessage(message);
        }
    }

    private int doAction(String message, PlayerSession s)
            throws InvalidInputException, InitialWordNotOnCenterException, InvalidMoveException, NotEnoughTilesException {
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
                if (!s.hasTiles(parts[4]))
                    throw new InvalidInputException(Protocol.Error.E008);

                score = boardController.handleMove(parts[2], parts[3], parts[4]);
                s.removeTiles(parts[4]);
                shouldReceiveTiles = tilePoolController.getTilesFromPool(parts[4].length());
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
            s.sendMessage(encodeMessage(Protocol.BasicCommand.NEWTILES.name(), shouldReceiveTiles));
            s.addTiles(shouldReceiveTiles);
            broadcast(informMessage);
        } else {
            throw new InvalidMoveException(Protocol.Error.E002);
        }
        return score;
    }
}
