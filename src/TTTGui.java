import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TTTGui extends JFrame implements ActionListener {

    JPanel grid_panel = new JPanel();
    JPanel title_panel = new JPanel();
    JLabel textField = new JLabel();
    JButton[] grid = new JButton[9];


    TTTGui(){
        setTitle("TicTacToe");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(720, 720);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setVisible(true);

        grid_panel.setLayout(new GridLayout(3,3));
        grid_panel.setBackground(new Color(150, 150, 150));

        textField.setBackground(new Color(25, 25, 25));
        textField.setForeground(new Color(25, 255, 0));
        textField.setFont(new Font("Monospace", Font.BOLD,75));
        textField.setHorizontalAlignment(JLabel.CENTER);
        textField.setText("Tic-Tac-Toe");
        textField.setOpaque(true);

        title_panel.setLayout(new BorderLayout());
        title_panel.setBounds(0, 0, 720, 80);

        title_panel.add(textField);

        for (int i=0; i<9; i++){
            grid[i] = new JButton();
            grid_panel.add(grid[i]);
            grid[i].setFont(new Font("MV Boli", Font.BOLD,120));
            grid[i].setFocusable(false);
            grid[i].addActionListener(this);
        }

        add(grid_panel);
        add(title_panel, BorderLayout.NORTH);


    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}