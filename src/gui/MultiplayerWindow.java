package gui;

import main.GameController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class MultiplayerWindow extends JFrame implements ActionListener {
    private final JButton ttt = new JButton();
    private final JButton Othello = new JButton();
    private final JButton disconnect = new JButton();

    MultiplayerWindow(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setSize(720, 720);
        setTitle("Game Launcher");

        ttt.setText("Tick Tack Toe");
        ttt.addActionListener(this);
        ttt.setBounds(200, 300, 100, 50);

        Othello.setText("Orthello");
        Othello.addActionListener(this);
        Othello.setBounds(400, 300, 100, 50);

        JLabel label = new JLabel();
        label.setBounds(250,200,200,100);
        label.setText("Subscribe to game");

        add(disconnect);
        add(Othello);
        add(ttt);
        add(label);

        disconnect.setBounds(20, 20, 75, 75);
        disconnect.setText("Disconnect");
        disconnect.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        GUI gui = GameController.getInstance().getGUI();

        if (e.getSource()== ttt){
            GameController.getInstance().getMultiplayerHandler().subscribe("tic-tac-toe");
            gui.setLastLocation(getLocation());
            setVisible(false);
            gui.nextWindow();
        }

        else if (e.getSource()== Othello){
            GameController.getInstance().getMultiplayerHandler().subscribe("reversi");
            gui.setLastLocation(getLocation());
            setVisible(false);
            gui.nextWindow();
        }

        else if (e.getSource()== disconnect){
            gui.setLastLocation(getLocation());
            setVisible(false);
            gui.disconnect();
        }
    }

    public void mainFrame() {
        setLocation(GameController.getInstance().getGUI().getLastLocation());
        setVisible(true);
    }
}
