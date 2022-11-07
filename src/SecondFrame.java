import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SecondFrame extends JFrame implements ActionListener {

    JButton local = new JButton();
    JButton online = new JButton();
    JLabel label = new JLabel();
    JButton back = new JButton();

    public String server;


    SecondFrame(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setSize(720, 720);
        setVisible(true);
        setTitle("Game Launcher");
        setLocationRelativeTo(null);


        local.setText("Local");
        local.addActionListener(this::actionPerformed);
        local.setBounds(200, 300, 100, 50);

        online.setText("Online");
        online.addActionListener(this::actionPerformed);
        online.setBounds(400, 300, 100, 50);

        label.setBounds(250,200,200,100);
        label.setText("Do you want to play local or online?");

        add(back);
        add(online);
        add(local);
        add(label);

        back.setBounds(20, 20, 75, 75);
        back.setText("Return");
        back.addActionListener(this::actionPerformed);
    }

    public String getServer(){
        return this.server;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==local){
            new ThirdFrame();
            this.server = "local";
            System.out.println(server);
        }

        if (e.getSource()==online){
            dispose();
            new ThirdFrame();
            this.server = "online";
            System.out.println(server);
        }

        if(e.getSource()==back){
            dispose();
            new Launcher();
        }
    }
}
