import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TTTGui implements ActionListener {


    JFrame frame = new JFrame();
    JPanel grid_panel = new JPanel();
    JButton[] grid = new JButton[9];

    TTTGui(){
        frame.setTitle("TicTacToe");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(720, 720);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        frame.setVisible(true);

        grid_panel.setLayout(new GridLayout(3,3));
        grid_panel.setBackground(new Color(150, 150, 150));

        for (int i=0; i<9; i++){
            grid[i] = new JButton();
            grid_panel.add(grid[i]);
            grid[i].setFont(new Font("MV Boli", Font.BOLD,120));
            grid[i].setFocusable(false);
            grid[i].addActionListener(this);
        }

        frame.add(grid_panel);


    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}