import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ThirdFrameOnline extends JFrame implements ActionListener {

    JButton ai = new JButton();
    JButton human = new JButton();
    JLabel label = new JLabel();
    JButton exit = new JButton();
    JButton back = new JButton();
    JTextField serverIP = new JTextField();
    String player;
    String IP;

    ThirdFrameOnline(){
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

        serverIP.setLayout(null);
        serverIP.setBounds(250, 500, 200, 50);
        serverIP.setVisible(true);
        serverIP.replaceSelection("Enter IP here");
        serverIP.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        exit.setBounds(600, 20, 75, 75);
        exit.setText("Exit");
        exit.addActionListener(this);


        add(back);
        add(human);
        add(ai);
        add(label);
        add(serverIP);
        add(exit);

        back.setBounds(20, 20, 75, 75);
        back.setText("Return");
        back.addActionListener(this);
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

        IP = serverIP.getText();
        System.out.println(IP);
    }
}
