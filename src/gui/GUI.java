package gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame implements ActionListener {

    JButton TTT = new JButton();
    JButton Othello = new JButton();
    JLabel label = new JLabel();
    JButton exit = new JButton();
    TTTGui ttt = new TTTGui(this);
    String game;
    public char icon;

    public GUI(){
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
    }

    public void MainFrame (){
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
            System.out.println(game);

        }

        if (e.getSource()==exit){
            dispose();
        }
    }

    public void endGame (String message){
        JOptionPane.showMessageDialog(null, message, "End of game", JOptionPane.PLAIN_MESSAGE);
    }

    public void setCurrentPlayer(char icon){
        ttt.textField.setText(icon + " turn");
        this.icon = icon;
    }

    public int getMove(){
        setCurrentPlayer(icon);
        ttt.buttonPressed.awaitMessage();
        return ttt.buttonPressed.getMessage();
    }

    public void updateBoard(char[][] board) {
        ttt.updateBoard(board);
    }

}
