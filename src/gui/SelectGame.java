package gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SelectGame extends JFrame implements ActionListener {

    JButton TTT = new JButton();
    JButton Othello = new JButton();
    JLabel label = new JLabel();
    JButton back = new JButton();
    JButton exit = new JButton();
    TTTGui ttt = new TTTGui();

    SelectGame(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setSize(720, 720);
        setVisible(true);
        setTitle("Game Launcher");
        setLocationRelativeTo(null);


        TTT.setText("Tick Tack Toe");
        TTT.addActionListener(this);
        TTT.setBounds(200, 300, 100, 50);

        Othello.setText("Orthello");
        Othello.addActionListener(this);
        Othello.setBounds(400, 300, 100, 50);

        label.setBounds(250,200,200,100);
        label.setText("What game do you want to play?");

        add(back);
        add(Othello);
        add(TTT);
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
        if (e.getSource()== TTT){
            dispose();
            ttt.MainFrame();
        }

        if (e.getSource()== Othello){
            dispose();
        }

        if(e.getSource()==back){
            dispose();
            new SelectPlayerOnline();
        }

        if (e.getSource()==exit){
            dispose();
        }
    }
}
