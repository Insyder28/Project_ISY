package gui;

import games.Board;
import games.Icon;
import players.PlayerType;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame implements ActionListener {

    // Singleton logic
    private static GUI instance = null;
    public static GUI getInstance() {
        if (instance == null) throw new InstanceNotFoundException();
        return instance;
    }

    // instance fields
    JButton local = new JButton();
    JButton online = new JButton();

    JLabel label = new JLabel();
    JButton exit = new JButton();
    public Icon icon = Icon.NO_ICON;
    TTTGui ttt = new TTTGui();

    // constructor
    public GUI(){
        if (instance != null) throw new InstanceAlreadyExistsException();
        instance = this;

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
        setLocationRelativeTo(null);
        add(local);
        add(online);
        add(label);
        add(exit);

        setVisible(true);
    }

    // methods
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==local){
            dispose();
            new PlayerXLocal();
        }

        if(e.getSource()== online){
            dispose();
            new SelectPlayerOnline();

        }

        if (e.getSource()==exit){
            dispose();
        }
    }

    public void endGame (String message){
        JOptionPane.showMessageDialog(null, message, "End of game", JOptionPane.PLAIN_MESSAGE);
    }

    public void setCurrentPlayer(Icon icon){
        ttt.textField.setText(icon.getChar() + " turn");
        this.icon = icon;
    }

    public int getMove(){
        setCurrentPlayer(icon);
        ttt.buttonPressed.awaitMessage();
        return ttt.buttonPressed.getMessage();
    }

    public PlayerType getSelectedPlayerType() {
        //TODO: finish method
        return PlayerType.HUMAN;
    }

    public void updateBoard(Board board) {
        ttt.updateBoard(board);
    }

}
