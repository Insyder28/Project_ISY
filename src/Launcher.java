import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Launcher extends JFrame implements ActionListener {

    JButton TTT = new JButton();
    JButton Othello = new JButton();
    JLabel label = new JLabel();
    public String game;


    Launcher(){
        TTT.setBounds(200, 300, 100, 50);
        TTT.addActionListener(this::actionPerformed);
        TTT.setText("TicTacToe");

        Othello.setBounds(400, 300, 100, 50);
        Othello.addActionListener(this::actionPerformed);
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
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==TTT){
            dispose();
            new SecondFrame();
            game = "TicTacToe";
            System.out.println(game);
        }

        if(e.getSource()==Othello){
            dispose();
            new SecondFrame();
            game = "Othello";
            System.out.println(game);

        }
    }
}
