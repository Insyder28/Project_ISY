import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ThirdFrame extends JFrame implements ActionListener {

    JButton ai = new JButton();
    JButton human = new JButton();
    JLabel label = new JLabel();
    JButton back = new JButton();
    JTextField serverIP = new JTextField("Enter server IP here");
    public String player;

    SecondFrame second = new SecondFrame();



    ThirdFrame(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setSize(720, 720);
        setVisible(true);
        setTitle("Game Launcher");
        setLocationRelativeTo(null);


        ai.setText("AI");
        ai.addActionListener(this::actionPerformed);
        ai.setBounds(200, 300, 100, 50);

        human.setText("Human");
        human.addActionListener(this::actionPerformed);
        human.setBounds(400, 300, 100, 50);

        label.setBounds(250,200,200,100);
        label.setText("Do you want to play or let AI play?");

        serverIP.setLayout(null);
        serverIP.setBounds(300, 500, 200, 50);
        dispose();
        if (second.getServer() == "online"){
            serverIP.setVisible(true);
            System.out.println(second.getServer());

        }

        add(back);
        add(human);
        add(ai);
        add(label);
        add(serverIP);

        back.setBounds(20, 20, 75, 75);
        back.setText("Return");
        back.addActionListener(this::actionPerformed);
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
    }
}
