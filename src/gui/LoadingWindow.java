package gui;

import javax.swing.*;

class LoadingWindow extends JFrame {
    JLabel label = new JLabel();

    public LoadingWindow() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setSize(720, 720);
        setTitle("Game Launcher");
    }

    public void startLoading(String message) {
        label.setBounds(250,200,200,100);
        label.setText(message);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void stopLoading() {
        setVisible(false);
    }
}
