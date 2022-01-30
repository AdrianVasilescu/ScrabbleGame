package main.Common;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.Semaphore;

import static main.Game.GameSpecifics.EMPTY_SLOT;

public class PlayerInteractor {
    private final JTextField playerInput = new JTextField(20);
    private final JTextArea serverMessages = new JTextArea(10, 20);
    private final JPanel boardPanel = new JPanel(new GridLayout(0,16));
    private final JLabel[][] boardLabels = new JLabel[16][16];
    private final JTextArea availableTiles = new JTextArea(1, 14);
    private final GridBagConstraints c = new GridBagConstraints();
    private final JPanel gui = new JPanel(new BorderLayout(3, 3));
    private final JFrame frame = new JFrame("Scrabble");
    private volatile String input;
    private final Semaphore inputSem;
    private final Thread guiThread;

    public PlayerInteractor()
    {
        inputSem = new Semaphore(0);
        guiThread = new Thread(() -> initGui());
        guiThread.start();
    }

    public void updateBoard(char[][] board)
    {
        for(int i = 0; i < 15; i++)
        {
            for(int j = 0; j < 15; j++)
            {
                if(board[i][j] != EMPTY_SLOT)
                {
                    populateLabel(boardLabels[i + 1][j + 1], String.valueOf(board[i][j]));
                }
            }
        }
    }

    public void updateTiles(String tiles)
    {
        this.availableTiles.setText("AVAILABLE TILES:\n" + tiles);
    }

    public String getInput() throws InterruptedException {
        String ret;
        playerInput.setEditable(true);
        inputSem.acquire();
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
        serverMessages.setLineWrap(true);
        JScrollPane serverPane = new JScrollPane(serverMessages, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        gui.add(serverPane, c);

        c.gridx = 0;
        c.gridy = 1;
        c.weighty = 1/4;
        c.weightx = 1/2;
        playerInput.setEditable(false);
        initPlayerInput();
        gui.add(playerInput, c);

        initBoard();
        c.gridx = 1;
        c.gridy = 0;
        c.weighty = 1;
        c.weightx = 1/2;
        gui.add(boardPanel, c);

        c.gridx = 1;
        c.gridy = 1;
        c.weighty = 1;
        c.weightx = 1/2;
        this.availableTiles.setText("AVAILABLE TILES:\n[ ]");
        availableTiles.setEditable(false);
        gui.add(availableTiles, c);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(gui);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public void closeGui()
    {
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        guiThread.interrupt();
    }

    private void initPlayerInput() {
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
                    inputSem.release();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        playerInput.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if("CMD-param-...-param".equals(playerInput.getText()))
                {
                    playerInput.setText("");
                    playerInput.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if(playerInput.getText() == null || playerInput.getText().isEmpty())
                {
                    playerInput.setForeground(Color.GRAY);
                    playerInput.setText("CMD-param-...-param");
                }
            }
        });
    }

    private void initBoard() {
        for(int i = 0; i < 16; i ++)
        {
            for(int j = 0; j < 16; j++)
            {
                JLabel label = new JLabel("", SwingConstants.CENTER);
                boardLabels[i][j] = label;
                label.setMaximumSize(new Dimension(30, 30));
                label.setPreferredSize(new Dimension(30, 30));
                label.setMinimumSize(new Dimension(30, 30));
                if(i != 0 && j != 0)
                {
                    label.setBorder(BorderFactory.createLineBorder(Color.black));
                }
                else
                {
                    label.setBackground(Color.orange);
                }
                if(i == 0)
                {
                    if(j != 0)
                    {
                        label.setText((char)(64 + j) + "");
                    }
                }
                else if (j == 0)
                {
                    if(i != 0)
                    {
                        label.setText(i + "");
                    }
                }
                else
                {
                    populateLabel(label, "");
                }
                label.setOpaque(true);
                boardPanel.add(label);
            }
        }
    }

    private void populateLabel(JLabel label, String c) {
        if(c.isEmpty())
        {
            label.setBackground(Color.decode("#dddddd"));
        }
        else
        {
            label.setBackground(Color.decode("#f5f5dc"));
        }
        label.setText(c);
    }

    public void printMessage(String s) {
        serverMessages.append(">" + s + "\n");
    }
}
