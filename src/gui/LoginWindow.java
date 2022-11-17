package gui;

import main.GameController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginWindow extends JFrame implements ActionListener {
    private final JButton Login = new JButton();
    private final JButton disconnect = new JButton();

    private final JTextField nameField = new JTextField();

    public LoginWindow() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setSize(720, 720);
        setTitle("Game Launcher");
        setLocationRelativeTo(null);

        Login.setText("Login");
        Login.addActionListener(this);
        Login.setBounds(200, 300, 100, 50);

        JLabel label = new JLabel();
        label.setBounds(250,200,200,100);
        label.setText("Enter player name");

        nameField.setLayout(null);
        nameField.setBounds(250, 500, 200, 50);
        nameField.setVisible(true);
        nameField.replaceSelection("Enter player name here");
        nameField.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        add(disconnect);
        add(Login);
        add(label);
        add(nameField);

        disconnect.setBounds(20, 20, 75, 75);
        disconnect.setText("Disconnect");
        disconnect.addActionListener(this);
    }

    public void mainFrame() {
        setLocation(GameController.getInstance().getGUI().getLastLocation());
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        GUI gui = GameController.getInstance().getGUI();

        if (e.getSource() == Login){
            String playerName = nameField.getText();

            if (playerName.contains(" ")) {
                JOptionPane.showMessageDialog(this, "Please enter a valid name", "Failed to log in", JOptionPane.ERROR_MESSAGE);
            }
            else {
                try {
                    gui.guiEventListener.onLogin(playerName);

                    setVisible(false);
                    gui.setLastLocation(getLocation());
                    gui.loggedIn();
                    gui.nextWindow();
                }
                catch (ActionFailedException exc) {
                    JOptionPane.showMessageDialog(this, exc.getMessage(), "Failed to log in", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        else if (e.getSource() == disconnect) {
            gui.setLastLocation(getLocation());
            setVisible(false);
            gui.disconnect();
        }
    }
}
