package gui;

import games.Mode;
import main.GameController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class SelectModeWindow extends JFrame implements ActionListener {
    private final JButton local = new JButton();
    private final JButton online = new JButton();

    public SelectModeWindow(){
        local.setBounds(200, 300, 100, 50);
        local.addActionListener(this);
        local.setText("Local");

        online.setBounds(400, 300, 100, 50);
        online.addActionListener(this);
        online.setText("Online");

        JLabel label = new JLabel();
        label.setBounds(300,200,200,100);
        label.setText("How do you want to play?");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setSize(720, 720);
        setTitle("Game Launcher");

        add(local);
        add(online);
        add(label);
    }

    // methods
    @Override
    public void actionPerformed(ActionEvent e) {
        GUI gui = GameController.getInstance().getGUI();

        if(e.getSource()==local){
            gui.setSelectedMode(Mode.LOCAL);
            setVisible(false);
            gui.setLastLocation(getLocation());
            gui.nextWindow();
        }

        else if(e.getSource()== online){
            gui.setSelectedMode(Mode.ONLINE);
            setVisible(false);
            gui.setLastLocation(getLocation());
            gui.nextWindow();
        }
    }

    public void mainFrame() {
        Point lastLocation = GameController.getInstance().getGUI().getLastLocation();

        if (lastLocation == null) setLocationRelativeTo(null);
        else setLocation(lastLocation);
        setVisible(true);
    }
}
