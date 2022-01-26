package main.Game;

import lib.Protocol;
import main.Board.Controller.BoardController;
import main.Common.Tile;
import main.Exceptions.*;
import main.TilePool.Controller.TilePoolController;

import java.util.ArrayList;
import java.util.List;

import static main.Common.GameSpecifics.encodeMessage;

public class GameSession implements Runnable{
    private BoardController boardController;
    private TilePoolController tilePoolController;
    private List<PlayerSession> players;
    private int currentPlayer;
    private final Object turn;

    public GameSession(List<PlayerSession> players) {
        this.boardController = new BoardController();
        this.tilePoolController = new TilePoolController();
        turn = new Object();
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
        synchronized (turn)
        {
            passTurn();
        }
        // GAME ONGOING


        // END GAME
        for(PlayerSession s : players)
        {
            s.endSession();
        }
    }

    private void listen(PlayerSession s, int id) {
        while(true)
        {
            try{
                String message = s.getNextMessage();
                synchronized (turn)
                {
                    if (currentPlayer == id) {
                        s.addScore(doAction(message, s));
                        passTurn();
                    } else {
                        s.sendMessage(encodeMessage(Protocol.BasicCommand.ERROR.name(), Protocol.Error.E009.name()));
                    }
                }
            } catch (GameException e)
            {
                s.sendMessage(encodeMessage(Protocol.BasicCommand.ERROR.name(), e.getError().name()));
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
    }

    private void broadcast(String message) {
        for(PlayerSession s : players)
        {
            s.sendMessage(message);
        }
    }

    private int doAction(String message, PlayerSession s)
            throws InvalidInputException, InitialWordNotOnCenterException,
            InvalidMoveException, NotEnoughTilesException {
        if(message == null)
            return 0;

        int score = 0;
        String[] parts = message.split(String.valueOf(Protocol.UNIT_SEPARATOR));
        String msg = null;
        switch (Protocol.BasicCommand.valueOf(parts[0]))
        {
            case MAKEMOVE:
                //TODO validate command
                String shouldReceiveTiles = "";
                String informMessage = "";
                if(parts[1].equals("WORD"))
                {
                    if(!s.hasTiles(parts[4]))
                        throw new InvalidInputException(Protocol.Error.E008);
                    score = boardController.handleMove(parts[2], parts[3], parts[4]);
                    s.removeTiles(parts[4]);
                    shouldReceiveTiles = tilePoolController.getTilesFromPool(parts[4].length());
                    informMessage = encodeMessage(Protocol.BasicCommand.INFORMMOVE.name(), s.getName(),
                            parts[1], parts[2], parts[3], parts[4]);
                }
                else if(parts[1].equals("SWAP"))
                {
                    //TODO validate
                    if(!s.hasTiles(parts[2]))
                    {
                        throw new InvalidMoveException(Protocol.Error.E008);
                    }
                    shouldReceiveTiles = tilePoolController.swapTiles(parts[2].toCharArray());
                    informMessage = encodeMessage(Protocol.BasicCommand.INFORMMOVE.name(), s.getName(),
                            parts[1], parts[2]);
                }
                s.sendMessage(encodeMessage(Protocol.BasicCommand.NEWTILES.name(), shouldReceiveTiles));
                s.addTiles(shouldReceiveTiles);
                broadcast(informMessage);
                passTurn();
                break;
            case ERROR:
                // TODO
            case GAMEOVER:
                // TODO
                break;
        }

        return score;
    }
}
