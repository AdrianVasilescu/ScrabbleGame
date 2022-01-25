package main.Common;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.Semaphore;

public class PlayerInteractor {
    private JTextField playerInput = new JTextField(20);
    private JTextArea serverMessages = new JTextArea(10, 20);
    private JTextArea gameState = new JTextArea();
    private JTextArea availableTiles = new JTextArea(1, 14);
    private GridBagConstraints c = new GridBagConstraints();
    private final JPanel gui = new JPanel(new BorderLayout(3, 3));
    private volatile String input;
    private Semaphore inputSem;
    private Thread guiThread;

    public PlayerInteractor()
    {
        inputSem = new Semaphore(0);
        guiThread = new Thread(() -> initGui());
        guiThread.start();
    }

    public void updateBoard(String board)
    {
        this.gameState.setText(board);
    }

    public void updateTiles(String tiles)
    {
        this.availableTiles.setText("AVAILABLE TILES:\n" + tiles);
    }

    public String getInput()
    {
        String ret;
        try {
            playerInput.setEditable(true);
            inputSem.acquire();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ret = this.input;
        this.input = null;

        return ret;
    }

    public void initGui()
    {
        gui.setBorder(new EmptyBorder(5, 5, 5, 5));
        gui.setLayout(new GridBagLayout());

        c.gridx = 0;
        c.gridy = 0;
        c.weighty = 3/4;
        c.weightx = 1/2;
        c.fill = GridBagConstraints.VERTICAL;
        serverMessages.setEditable(false);
        JScrollPane serverPane = new JScrollPane(serverMessages);
        gui.add(serverPane, c);

        c.gridx = 0;
        c.gridy = 1;
        c.weighty = 1/4;
        c.weightx = 1/2;
        playerInput.setEditable(false);
        playerInput.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER && playerInput.isEditable())
                {
                    input = playerInput.getText();
                    playerInput.setText("");
                    playerInput.setEditable(false);
                    serverMessages.append("ENTER\n");
                    inputSem.release();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        gui.add(playerInput, c);

        c.gridx = 1;
        c.gridy = 0;
        c.weighty = 1;
        c.weightx = 1/2;
        gameState.setEditable(false);
        gui.add(gameState, c);

        c.gridx = 1;
        c.gridy = 1;
        c.weighty = 1;
        c.weightx = 1/2;
        this.availableTiles.setText("AVAILABLE TILES:\n[ ]");
        availableTiles.setEditable(false);
        gui.add(availableTiles, c);

        JFrame frame = new JFrame("Scrabble");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(gui);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
}
