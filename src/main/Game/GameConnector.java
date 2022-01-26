package main.Game;

import lib.Protocol;

import java.net.Socket;

public class GameConnector {
    Socket socket;
    String message;

    public void sendMessage(String message)
    {
        System.out.println(message);
    }

    public String getNextMessage()
    {
        return Protocol.BasicCommand.WELCOME.name() + Protocol.UNIT_SEPARATOR + "TEST" + Protocol.MESSAGE_SEPARATOR;
    }
}
