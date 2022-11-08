package GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SecondFrame extends JFrame implements ActionListener {

    JButton local = new JButton();
    JButton online = new JButton();
    JButton exit = new JButton();
    JLabel label = new JLabel();
    JButton back = new JButton();
    String server;


    SecondFrame(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setSize(720, 720);
        setVisible(true);
        setTitle("Game Launcher");
        setLocationRelativeTo(null);


        local.setText("Local");
        local.addActionListener(this);
        local.setBounds(200, 300, 100, 50);

        online.setText("Online");
        online.addActionListener(this);
        online.setBounds(400, 300, 100, 50);

        label.setBounds(250,200,200,100);
        label.setText("Do you want to play local or online?");

        exit.setBounds(600, 20, 75, 75);
        exit.setText("Exit");
        exit.addActionListener(this);

        add(back);
        add(online);
        add(local);
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
        if (e.getSource()==local){
            dispose();
            new ThirdFrame();
            server = "local";
            System.out.println(server);
        }

        if (e.getSource()==online){
            dispose();
            new ThirdFrameOnline();
            server = "online";
            System.out.println(server);
        }

        if(e.getSource()==back){
            dispose();
            new GUI();
        }

        if (e.getSource()==exit){
            dispose();
        }
    }
}
