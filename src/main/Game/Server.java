package main.Game;

import lib.Protocol;
import main.Exceptions.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.Semaphore;

import static main.Game.GameSpecifics.decodeRequestGame;
import static main.Game.GameSpecifics.encodeMessage;

/**
 * The server runnable
 */
public class Server implements Runnable{
    /**
     * All the active named player sessions
     */
    private final Map<String, PlayerSession> sessions;
    /**
     * All the game queues with incomplete games
     */
    private final Map<Integer, List<String>> gameQueues;
    /**
     * Semaphore used to sync game requests with queue supervising
     */
    private final Semaphore gameRequest;
    /**
     * The socket on which this server listens for connections
     */
    private ServerSocket socket;
    /**
     * The port on which this server will open
     */
    private final int port;
    /**
     * A list of all the active games
     */
    private final List<Thread> games;

    /**
     * Instantiates a new server
     */
    public Server(int port)
    {
        this.sessions = Collections.synchronizedMap(new HashMap<>());
        this.gameQueues = Collections.synchronizedMap(new HashMap<>());
        this.gameRequest = new Semaphore(0);
        this.games = new ArrayList<>();
        this.port = port;
    }

    @Override
    public void run() {
        openServerSocket();

        Thread connectionsThread = new Thread(() -> listenForConnections());
        Thread gameQueuesHandlerThread = new Thread(() -> superviseQueues());

        try {
            connectionsThread.start();
            gameQueuesHandlerThread.start();

            connectionsThread.join();
            gameQueuesHandlerThread.join();
            for(Thread t : games)
            {
                t.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens the server socket
     */
    private void openServerSocket() {
        System.out.println("Server starting...");
        try {
            socket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("...server accepting connections!");
    }

    /**
     * Actively listens for connections
     */
    private void listenForConnections()
    {
        while(true)
        {
            try {
                Socket s = socket.accept();
                SocketConnector socketConnector = new SocketConnector(s);
                System.out.println("New Connection!");
                PlayerSession session = new PlayerSession(socketConnector);
                putPlayerInLobby(session);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Puts the player back in the state where he needs to request a new game
     * @param session the player session
     */
    private void putPlayerInLobby(PlayerSession session) {
        Thread thread = new Thread(() -> {
            try {
                handlePlayerSession(session);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    /**
     * Supervises all game queues and decides when a game can be started
     */
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
                            List<PlayerSession> players = Collections.synchronizedList(new ArrayList<>());
                            for(String playerName : gameQueues.get(numP))
                            {
                                players.add(sessions.get(playerName));
                            }
                            GameSession gameSession = new GameSession(players);
                            Thread game = new Thread(() ->
                            {
                                gameSession.run();
                                for(PlayerSession player : players)
                                {
                                    putPlayerInLobby(player);
                                }
                            });
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

    /**
     * Handles a new player session until it announces and requests a game
     * @param serverSession the session
     * @throws IOException
     */
    private void handlePlayerSession(PlayerSession serverSession) throws IOException {
        boolean playerNamed = !serverSession.getName().isEmpty();
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

    /**
     * Checks if a player name is already taken
     * @param name the name
     * @throws InvalidInputException
     */
    private void checkName(String name) throws InvalidInputException {
        if(sessions.containsKey(name))
        {
            throw new InvalidInputException(Protocol.Error.E001);
        }
    }

    /**
     * Tries to decode an announce message
     * @param message the message
     * @return the name of the announced player
     * @throws InvalidMoveException
     * @throws InvalidInputException
     */
    private String decodeAnnounce(String[] message) throws InvalidMoveException, InvalidInputException {
        if(message.length != 2 ||
                !message[0].equals(Protocol.BasicCommand.ANNOUNCE.name()))
        {
            throw new InvalidMoveException(Protocol.Error.E002);
        }
        return message[1];
    }
}
