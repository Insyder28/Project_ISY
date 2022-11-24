package gui;

import games.data.GameType;
import main.GameController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class SelectGameWindow extends JFrame implements ActionListener {
    private final JButton ttt = new JButton();
    private final JButton Othello = new JButton();
    private final JButton back = new JButton();

    SelectGameWindow(){
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
        label.setText("What game do you want to play?");

        add(back);
        add(Othello);
        add(ttt);
        add(label);

        back.setBounds(20, 20, 75, 75);
        back.setText("Return");
        back.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        GUI gui = GameController.getInstance().getGUI();

        if (e.getSource()== ttt){
            gui.setSelectedGameType(GameType.TICTACTOE);
            setVisible(false);
            gui.setLastLocation(getLocation());
            gui.nextWindow();
        }

        else if (e.getSource()== Othello){
            gui.setSelectedGameType(GameType.OTHELLO);
            setVisible(false);
            gui.setLastLocation(getLocation());
            gui.nextWindow();
        }

        else if(e.getSource()==back){
            setVisible(false);
            gui.setLastLocation(getLocation());
            gui.previousWindow();
        }
    }

    public void mainFrame() {
        setLocation(GameController.getInstance().getGUI().getLastLocation());
        setVisible(true);
    }
}
