import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TTTGui extends JFrame implements ActionListener {

    public JPanel grid_panel = new JPanel();
    public JPanel title_panel = new JPanel();
    public JLabel textField = new JLabel();
    public JButton[] grid = new JButton[9];
    public MessageBuffer<Integer> buttonPressed = new MessageBuffer<>();

    private final GUI mainGUI;

    TTTGui(GUI mainGUI) {
        this.mainGUI = mainGUI;

        setTitle("TicTacToe");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(720, 720);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        grid_panel.setLayout(new GridLayout(3, 3));
        grid_panel.setBackground(new Color(150, 150, 150));

        textField.setBackground(new Color(25, 25, 25));
        textField.setForeground(new Color(25, 255, 0));
        textField.setFont(new Font("Monospace", Font.BOLD, 75));
        textField.setHorizontalAlignment(JLabel.CENTER);
        textField.setText("Tic-Tac-Toe");
        textField.setOpaque(true);

        title_panel.setLayout(new BorderLayout());
        title_panel.setBounds(0, 0, 720, 80);

        title_panel.add(textField);

        for (int i = 0; i < 9; i++) {
            grid[i] = new JButton();
            grid_panel.add(grid[i]);
            grid[i].setFont(new Font("MV Boli", Font.BOLD, 120));
            grid[i].setFocusable(false);
            grid[i].addActionListener(this);
        }

        add(grid_panel);
        add(title_panel, BorderLayout.NORTH);


    }

    public void MainFrame() {
        setVisible(true);
    }

    public void updateBoard(char[][] board) {
        int index = 0;
        for (int i = 0; i < board[0].length; i++) {
            for (int j = 0; j < board.length; j++) {
                char icon = board[i][j];

                grid[index].setText(Character.toString(icon));
                grid[index].setForeground(icon == 'X' ? new Color(0, 0, 255) : new Color(255, 0, 0));
                index++;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < 9; i++) {
            if (mainGUI.icon == 'X') {
                if (e.getSource() == grid[i]) {
//                    if (grid[i].getText() == "") {
//                        grid[i].setForeground(new Color(255, 0, 0));
//                        grid[i].setText("X");
                    buttonPressed.setMessage(i);

                }
            }
//            else {
//                if (e.getSource() == grid[i]) {
//                    if (grid[i].getText() == "") {
//                        grid[i].setForeground(new Color(0, 0, 255));
//                        grid[i].setText("O");
//                }
//            }
        }
    }
}