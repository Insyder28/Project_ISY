package gui;

import games.data.Board;
import games.data.Icon;
import main.GameController;
import util.Buffer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OthelloGUI extends JFrame implements ActionListener {
    public JPanel grid_panel = new JPanel();

    public JPanel title_panel = new JPanel();
    public JLabel currentPlayer = new JLabel();
    public JLabel player = new JLabel();

    GridLayout titleLayout = new GridLayout(1, 2);

    public JButton[] grid = new JButton[64];


    public Buffer<Integer> buttonPressed = new Buffer<>();

    public OthelloGUI(boolean showPlayer) {
        setTitle("Othello");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(720, 720);

        setLocation(GameController.getInstance().getGUI().getLastLocation());

        setLayout(new BorderLayout());

        grid_panel.setLayout(new GridLayout(8, 8));
        grid_panel.setBackground(new Color(150, 150, 150));

        currentPlayer.setBackground(new Color(25, 25, 25));
        currentPlayer.setForeground(new Color(25, 255, 0));
        currentPlayer.setFont(new Font("Monospace", Font.BOLD, 50));
        currentPlayer.setHorizontalAlignment(JLabel.CENTER);
        currentPlayer.setOpaque(true);

        title_panel.setLayout(new BorderLayout());
        title_panel.setBounds(0, 0, 720, 80);

        title_panel.add(currentPlayer);

        if (showPlayer) {
            player.setBackground(new Color(25, 25, 25));
            player.setForeground(new Color(25, 255, 0));
            player.setFont(new Font("Monospace", Font.BOLD, 50));
            player.setHorizontalAlignment(JLabel.CENTER);
            player.setOpaque(true);

            title_panel.setLayout(titleLayout);
            title_panel.add(player);
        }

        for (int i = 0; i < 64; i++) {
            grid[i] = new JButton();
            grid_panel.add(grid[i]);
            //grid[i].setFont(new Font("MV Boli", Font.BOLD, 50));
            grid[i].setFont(new Font("", Font.BOLD, 50));
            grid[i].setFocusable(false);
            grid[i].addActionListener(this);
            grid[i].setBackground(new Color(0, 144, 103));
        }

        add(grid_panel);
        add(title_panel, BorderLayout.NORTH);

        setVisible(true);
    }

    public void endGame(String message) {
        buttonPressed.interrupt();
        title_panel.remove(player);
        title_panel.setLayout(new GridLayout());
        currentPlayer.setText(message);

        GameController.getInstance().getGUI().gameEnded();
    }

    public void setCurrentPlayer(Icon icon) {
        currentPlayer.setText(icon.getChar() + " turn");
    }

    public void setPlayer(Icon icon) {
        player.setText("You are: " + icon.getChar());
    }

    public int getMove() throws InterruptedException {
        return buttonPressed.await();
    }


    public void updateBoard(Board board) {
        Icon[][] data = board.data;

        int index = 0;
        for (int i = 0; i < board.height; i++) {
            for (int j = 0; j < board.width; j++) {
                char icon = ' ';
                if (data[i][j] != Icon.NO_ICON)
                    icon = '●';

                grid[index].setText(String.valueOf(icon));
                grid[index].setForeground(data[i][j] == Icon.CROSS ? new Color(0, 0, 0) : new Color(255, 255, 255));
                index++;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < 64; i++) {
            if (e.getSource() == grid[i]) {
                buttonPressed.set(i);
            }
        }
    }
}