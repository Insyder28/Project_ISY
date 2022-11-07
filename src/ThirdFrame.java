import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ThirdFrame extends JFrame implements ActionListener {

    JButton ai = new JButton();
    JButton human = new JButton();
    JLabel label = new JLabel();
    JButton back = new JButton();
    JButton exit = new JButton();
    String player;

    ThirdFrame(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setSize(720, 720);
        setVisible(true);
        setTitle("Game Launcher");
        setLocationRelativeTo(null);


        ai.setText("AI");
        ai.addActionListener(this);
        ai.setBounds(200, 300, 100, 50);

        human.setText("Human");
        human.addActionListener(this);
        human.setBounds(400, 300, 100, 50);

        label.setBounds(250,200,200,100);
        label.setText("Do you want to play or let AI play?");

        add(back);
        add(human);
        add(ai);
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
        if (e.getSource()==ai){
            dispose();
            player = "ai";
            System.out.println(player);
        }

        if (e.getSource()==human){
            dispose();
            player = "human";
            System.out.println(player);
        }

        if(e.getSource()==back){
            dispose();
            new SecondFrame();
        }

        if (e.getSource()==exit){
            dispose();
        }
    }
}
