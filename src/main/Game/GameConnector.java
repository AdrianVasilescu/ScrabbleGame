package main.Game;

import java.net.Socket;

public class GameConnector {
    Socket socket;

    public void sendMessage(String message)
    {
        System.out.println(message);
    }

    public String getNextMessage()
    {
        return "";
    }
}
