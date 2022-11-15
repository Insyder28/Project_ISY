package gui;

import games.GameType;
import threading.Buffer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class SelectGameWindow extends JFrame implements ActionListener {
    private Buffer<GameType> gameTypeBuffer = new Buffer<>();

    JButton ttt = new JButton();
    JButton Othello = new JButton();
    JLabel label = new JLabel();
    JButton back = new JButton();
    JButton exit = new JButton();

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

        label.setBounds(250,200,200,100);
        label.setText("What game do you want to play?");

        add(back);
        add(Othello);
        add(ttt);
        add(label);
        add(exit);

        back.setBounds(20, 20, 75, 75);
        back.setText("Return");
        back.addActionListener(this);

        exit.setBounds(600, 20, 75, 75);
        exit.setText("Exit");
        exit.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()== ttt){
            gameTypeBuffer.set(GameType.TICTACTOE);
            setVisible(false);
        }

        if (e.getSource()== Othello){
            gameTypeBuffer.set(GameType.OTHELLO);
            setVisible(false);
        }

        if(e.getSource()==back){
            setVisible(false);
        }

        if (e.getSource()==exit){
            setVisible(false);
        }
    }

    public GameType getGameType() {
        setLocationRelativeTo(null);
        setVisible(true);
        return gameTypeBuffer.await();
    }
}
