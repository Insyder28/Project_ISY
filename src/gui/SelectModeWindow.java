package gui;

import games.Icon;
import games.Mode;
import threading.Buffer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class SelectModeWindow extends JFrame implements ActionListener {
    private final Buffer<Mode> modeBuffer = new Buffer<>();

    JButton local = new JButton();
    JButton online = new JButton();

    JLabel label = new JLabel();
    JButton exit = new JButton();

    public SelectModeWindow(){
        local.setBounds(200, 300, 100, 50);
        local.addActionListener(this);
        local.setText("Local");

        online.setBounds(400, 300, 100, 50);
        online.addActionListener(this);
        online.setText("Online");

        exit.setBounds(600, 20, 75, 75);
        exit.setText("Exit");
        exit.addActionListener(this);

        label.setBounds(300,200,200,100);
        label.setText("How do you want to play?");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setSize(720, 720);
        setTitle("Game Launcher");

        add(local);
        add(online);
        add(label);
        add(exit);
    }

    // methods
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==local){
            modeBuffer.set(Mode.LOCAL);
            setVisible(false);
        }

        if(e.getSource()== online){
            modeBuffer.set(Mode.ONLINE);
            setVisible(false);
        }

        if (e.getSource()==exit){
            setVisible(false);
        }
    }

    public Mode getMode() {
        setLocationRelativeTo(null);
        setVisible(true);
        return modeBuffer.await();
    }
}
