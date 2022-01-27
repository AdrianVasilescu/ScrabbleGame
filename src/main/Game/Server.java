package main.Game;

import com.sun.deploy.util.StringUtils;
import lib.Protocol;
import main.Exceptions.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.Semaphore;

import static main.Common.GameSpecifics.decodeRequestGame;
import static main.Common.GameSpecifics.encodeMessage;

public class Server implements Runnable{
    private Map<String, PlayerSession> sessions;
    private Map<Integer, List<String>> gameQueues;

    private final Semaphore stopGame;
    private final Semaphore gameRequest;

    private ServerSocket socket;
    private int port;
    private Thread connectionsThread;
    private Thread gameQueuesHandlerThread;
    private List<Thread> games;

    /**
     * Instantiates a new server
     */
    public Server(int port)
    {
        this.sessions = Collections.synchronizedMap(new HashMap<>());
        this.gameQueues = Collections.synchronizedMap(new HashMap<>());
        this.stopGame = new Semaphore(0);
        this.gameRequest = new Semaphore(0);
        this.games = new ArrayList<>();
        this.port = port;
    }

    @Override
    public void run() {
        openServerSocket();
        connectionsThread = new Thread(() -> listenForConnections());
        gameQueuesHandlerThread = new Thread(() -> superviseQueues());

        try {
            connectionsThread.start();
            gameQueuesHandlerThread.start();

            connectionsThread.join();
            gameQueuesHandlerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void openServerSocket() {
        System.out.println("Server starting...");
        try {
            socket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("...server accepting connections!");
    }

    private void listenForConnections()
    {
        while(true)
        {
            try {
                Socket s = socket.accept();
                PlayerConnector playerConnector = new PlayerConnector(s);
                System.out.println("New Connection!");
                PlayerSession session = new PlayerSession(playerConnector);
                Thread thread = new Thread(() -> {
                    try {
                        handlePlayerSession(session);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                thread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void superviseQueues() {
        while(true)
        {
            try {
                gameRequest.acquire();
                synchronized (gameQueues)
                {
                    for (int numP : gameQueues.keySet()) {
                        if (gameQueues.get(numP).size() == numP)
                        {
                            List<PlayerSession> players = new ArrayList<>();
                            String param = StringUtils.join(gameQueues.get(numP), Protocol.UNIT_SEPARATOR + "");
                            for(String playerName : gameQueues.get(numP))
                            {
                                players.add(sessions.get(playerName));
                            }
                            GameSession gameSession = new GameSession(players);
                            Thread game = new Thread(gameSession);
                            game.start();
                            games.add(game);
                            gameQueues.remove(numP);
                        }
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void handlePlayerSession(PlayerSession serverSession) throws IOException {
        boolean playerNamed = false;
        boolean requestedGame = false;

        while(!playerNamed || !requestedGame)
        {
            String[] message = serverSession.getNextMessage().split(Protocol.UNIT_SEPARATOR + "");
            try {
                if(!playerNamed)
                {
                    String name = decodeAnnounce(message);
                    checkName(name);
                    sessions.put(name, serverSession);
                    serverSession.setName(name);
                    playerNamed = true;
                    serverSession.sendMessage(encodeMessage(Protocol.BasicCommand.WELCOME.name(), name));
                }
                else if(!requestedGame)
                {
                    int numPlayers = decodeRequestGame(message);
                    synchronized (gameQueues)
                    {
                        List<String> queue = gameQueues.computeIfAbsent(numPlayers,
                                k -> Collections.synchronizedList(new ArrayList<>()));
                        queue.add(serverSession.getName());

                        for(String name : queue)
                        {
                            PlayerSession s = sessions.get(name);
                            s.sendMessage(encodeMessage(Protocol.BasicCommand.INFORMQUEUE.name(),
                                    queue.size() + "", numPlayers + ""));
                        }
                        requestedGame = true;
                        gameRequest.release();
                    }
                }
            } catch (GameException e) {
                serverSession.sendMessage(
                        encodeMessage(Protocol.BasicCommand.ERROR.name(), e.getError().name()));
            }
        }
    }

    private void checkName(String name) throws InvalidInputException {
        if(sessions.containsKey(name))
        {
            throw new InvalidInputException(Protocol.Error.E001);
        }
    }

    private String decodeAnnounce(String[] command) throws InvalidMoveException, InvalidInputException {
        if(command.length != 2 ||
                !command[0].equals(Protocol.BasicCommand.ANNOUNCE.name()))
        {
            throw new InvalidMoveException(Protocol.Error.E002);
        }
        return command[1];
    }
}
