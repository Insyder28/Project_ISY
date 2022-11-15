package gui;

import games.Icon;
import players.PlayerType;
import threading.Buffer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class SelectPlayerTypeWindow extends JFrame implements ActionListener {

    private final Buffer<PlayerType> playerTypeBuffer = new Buffer<>();

    JButton ai = new JButton();
    JButton human = new JButton();
    JButton exit = new JButton();
    JLabel label = new JLabel();
    JButton back = new JButton();

    SelectPlayerTypeWindow(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setSize(720, 720);
        setTitle("Game Launcher");

        ai.setText("AI");
        ai.addActionListener(this);
        ai.setBounds(200, 300, 100, 50);

        human.setText("Human");
        human.addActionListener(this);
        human.setBounds(400, 300, 100, 50);

        label.setBounds(250,200,200,100);

        exit.setBounds(600, 20, 75, 75);
        exit.setText("Exit");
        exit.addActionListener(this);

        add(back);
        add(human);
        add(ai);
        add(label);
        add(exit);

        back.setBounds(20, 20, 75, 75);
        back.setText("Return");
        back.addActionListener(this);

        exit.setBounds(600, 20, 75, 75);
        exit.setText("Exit");
        exit.addActionListener(this);
    }

    public PlayerType getPlayerType() {
        return getPlayerType(Icon.NO_ICON);
    }

    public PlayerType getPlayerType(Icon icon) {
        if (icon == Icon.NO_ICON) label.setText("Select Player Type");
        else label.setText("Select Player Type for " + icon.getChar());

        setLocationRelativeTo(null);
        setVisible(true);
        return playerTypeBuffer.await();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()== ai){
            playerTypeBuffer.set(PlayerType.AI);
            setVisible(false);
        }

        if (e.getSource()== human){
            playerTypeBuffer.set(PlayerType.HUMAN);
            setVisible(false);
        }

        if(e.getSource()==back){
            setVisible(false);
            new SelectModeWindow();
        }

        if (e.getSource()==exit){
            setVisible(false);
        }
    }
}
