package main.Game;

import lib.Protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * The socket connector for input / output handling
 */
public class SocketConnector {
    /**
     * The socket
     */
    private Socket socket;
    /**
     * The inout stream
     */
    private DataInputStream dataInputStream;
    /**
     * The output stream
     */
    private DataOutputStream dataOutputStream;

    /**
     * Creates a new connector
     * @param socket the socket
     */
    public SocketConnector(Socket socket)
    {
        this.socket = socket;
        try {
            this.dataInputStream = new DataInputStream(socket.getInputStream());
            this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a message on the socket
     * @param message the message
     * @throws IOException in case the connection interrupts
     */
    public void sendMessage(String message) throws IOException {
        dataOutputStream.writeChars(message);
    }

    /**
     * Gets the next message sequence from the socket
     * @return the next message
     * @throws IOException
     */
    public String getNextMessage() throws IOException {
        String message = "";
        char c = dataInputStream.readChar();

        while(c != Protocol.MESSAGE_SEPARATOR)
        {
            message += c;
            c = dataInputStream.readChar();
        }

        System.out.println("GOT: " + message);
        return message;
    }

    /**
     * Close the connection and all associated streams
     */
    public void disconnect()
    {
        try {
            this.dataOutputStream.close();
            this.dataInputStream.close();
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
