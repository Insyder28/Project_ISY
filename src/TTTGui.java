import javax.swing.*;
import java.awt.event.ActionEvent;

public class TTTGui extends JFrame{
    JButton button = new JButton();
    JButton button2 = new JButton();
    JButton back = new JButton();
    JButton exit = new JButton();
    JLabel label = new JLabel();

    JButton[] grid = new JButton[9];

    TTTGui(){
        button.setBounds(200, 300, 100, 50);
        button.setText("Local");
        button.addActionListener(this::actionPerformed2);

        button2.setBounds(400, 300, 100, 50);
        button2.addActionListener(this::actionPerformed2);
        button2.setText("Online");

        label.setBounds(250,200,200,100);
        label.setText("Do you want to play local or online?");

        back.setBounds(20, 20, 75, 75);
        back.addActionListener(this::actionPerformed2);
        back.setText("Return");

        exit.setBounds(20, 40, 75, 75);
        exit.addActionListener(this::actionPerformed2);
        exit.setText("Exit");


        this.setTitle("Game Launcher");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(720, 720);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setLayout(null);

        this.add(button);
        this.add(button2);
        this.add(label);
        this.add(back);
        this.add(exit);
    }
    public void actionPerformed2(ActionEvent e) {
        if(e.getSource()==back) {
            this.dispose();
            new Launcher();
        }
        if(e.getSource()==exit){
            this.dispose();
        }
    }

}