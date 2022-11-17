package gui;

import main.GameController;

import javax.swing.*;
import java.awt.*;

class LoadingWindow extends JFrame {
    JPanel panel = new JPanel();
    JLabel label = new JLabel();

    public LoadingWindow() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(720, 720);
        setTitle("Game Launcher");

        panel.setLayout(new GridLayout());
        add(panel);

        label.setBounds(250,200,200,100);
        label.setFont(new Font("Monospace", Font.BOLD, 50));
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setOpaque(true);

        panel.add(label);
    }

    public void stopLoading() {
        setVisible(false);
        GameController.getInstance().getGUI().setLastLocation(getLocation());
    }
}
