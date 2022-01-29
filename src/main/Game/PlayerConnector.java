package main.Game;

import lib.Protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class PlayerConnector {
    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    public PlayerConnector(Socket socket)
    {
        this.socket = socket;
        try {
            this.dataInputStream = new DataInputStream(socket.getInputStream());
            this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message)
    {
        try {
            //System.out.println("SENDING: " + message);
            dataOutputStream.writeChars(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getNextMessage() throws IOException {
        String message = "";
        char c = dataInputStream.readChar();

        while(c != Protocol.MESSAGE_SEPARATOR)
        {
            message += c;
            c = dataInputStream.readChar();
        }

        //System.out.println("GOT: " + message);
        return message;
    }
}
