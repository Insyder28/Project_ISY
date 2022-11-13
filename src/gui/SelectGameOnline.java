package gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SelectGameOnline extends JFrame implements ActionListener {

    JButton ai = new JButton();
    JButton human = new JButton();
    JLabel label = new JLabel();
    JButton exit = new JButton();
    JButton back = new JButton();

    SelectGameOnline(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setSize(720, 720);
        setVisible(true);
        setTitle("Game Launcher");
        setLocationRelativeTo(null);


        ai.setText("Tick Tack Toe");
        ai.addActionListener(this);
        ai.setBounds(200, 300, 100, 50);

        human.setText("Othello");
        human.addActionListener(this);
        human.setBounds(400, 300, 100, 50);

        label.setBounds(250,200,200,100);
        label.setText("Choose a game to subscribe to");

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
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==ai){
            dispose();
        }

        if (e.getSource()==human){
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
