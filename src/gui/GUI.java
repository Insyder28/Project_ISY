package gui;

import games.Board;
import games.Icon;
import players.PlayerType;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame implements ActionListener {

    private static GUI instance = null;
    public static GUI getInstance() {
        if (instance == null) throw new InstanceNotFoundException();
        return instance;
    }


    JButton TTT = new JButton();
    JButton Othello = new JButton();
    JLabel label = new JLabel();
    JButton exit = new JButton();
    TTTGui ttt = new TTTGui();
    String game;
    public Icon icon = Icon.NO_ICON;

    public GUI(){
        if (instance != null) throw new InstanceAlreadyExistsException();
        instance = this;

        TTT.setBounds(200, 300, 100, 50);
        TTT.addActionListener(this);
        TTT.setText("TicTacToe");

        Othello.setBounds(400, 300, 100, 50);
        Othello.addActionListener(this);
        Othello.setText("Othello");

        exit.setBounds(600, 20, 75, 75);
        exit.setText("Exit");
        exit.addActionListener(this);

        label.setBounds(300,200,200,100);
        label.setText("Choose a game to play!");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setSize(720, 720);
        setTitle("Game Launcher");
        setLocationRelativeTo(null);
        add(TTT);
        add(Othello);
        add(label);
        add(exit);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==TTT){
            dispose();
            new SecondFrame();
            game = "TicTacToe";
            System.out.println(game);
            ttt.MainFrame();
        }

        if(e.getSource()==Othello){
            dispose();
            new SecondFrame();
            game = "Othello";

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
