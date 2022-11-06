import javax.swing.*;
import java.awt.event.ActionEvent;

public class Launcher extends JFrame{

    JButton TTT = new JButton();
    JButton Othello = new JButton();
    JButton back = new JButton();
    JLabel label = new JLabel();
    JTextField serverIP = new JTextField("Enter server IP here");
    String game;
    String server;


    Launcher(){
        TTT.setBounds(200, 300, 100, 50);
        TTT.addActionListener(new AbstractAction("TTT") {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource()==TTT){
                    SecondFrame();
                    game = "TicTacToe";
                    System.out.println(game);

                }
            }
        });
        TTT.setText("TicTacToe");

        Othello.setBounds(400, 300, 100, 50);
        Othello.addActionListener(new AbstractAction("Othello") {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource()==Othello){
                    SecondFrame();
                    game = "Othello";
                    System.out.println(game);

                }
            }
        });
        Othello.setText("Othello");

        label.setBounds(300,200,200,100);
        label.setText("Choose a game to play!");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setSize(720, 720);
        setVisible(true);
        setTitle("Game Launcher");
        setLocationRelativeTo(null);
        add(TTT);
        add(Othello);
        add(label);
    }

    public void SecondFrame(){
        TTT.setText("Local");
        TTT.addActionListener(new AbstractAction("local") {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource()==TTT){
                    server = "local";
                    System.out.println(server);
                }
            }
        });

        Othello.setText("Online");
        Othello.addActionListener(new AbstractAction("online") {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource()==Othello){
                    server = "online";
                    System.out.println(server);
                    Server();
                }
            }
        });

        label.setBounds(250,200,200,100);
        label.setText("Do you want to play local or online?");

        add(back);

        back.setBounds(20, 20, 75, 75);
        back.setText("Return");
        back.addActionListener(new AbstractAction("return") {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource()==back){
                    dispose();
                    new Launcher();
                }
            }
        });



    }

    public void Server(){
        serverIP.setLayout(null);
        serverIP.setBounds(300, 500, 200, 50);
        serverIP.setVisible(true);
        add(serverIP);
    }
}
