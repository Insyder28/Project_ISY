package gui;

import games.Icon;
import main.GameController;
import players.PlayerType;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class SelectPlayerTypeWindow extends JFrame implements ActionListener {
    JButton ai = new JButton();
    JButton human = new JButton();
    JButton exit = new JButton();
    JLabel label = new JLabel();
    JButton back = new JButton();

    private Icon playerToSelect;

    SelectPlayerTypeWindow(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setSize(720, 720);
        setTitle("Game Launcher");

        ai.setText("AI");
        ai.addActionListener(this);
        ai.setBounds(200, 300, 100, 50);

        human.setText("Human");
        human.addActionListener(this);
        human.setBounds(400, 300, 100, 50);

        label.setBounds(250,200,200,100);

        exit.setBounds(600, 20, 75, 75);
        exit.setText("Exit");
        exit.addActionListener(this);

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

    public void mainFrame() {
        mainFrame(Icon.NO_ICON);
    }

    public void mainFrame(Icon playerToSelect) {
        this.playerToSelect = playerToSelect;

        if (playerToSelect == Icon.NO_ICON) label.setText("Select Player Type");
        else label.setText("Select Player Type for " + playerToSelect.getChar());

        showWindow();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        GUI gui = GameController.getInstance().getGUI();

        if (e.getSource()== ai){
            gui.setSelectedPlayerType(PlayerType.AI, playerToSelect);
            setVisible(false);
            gui.next();
        }

        else if (e.getSource()== human){
            gui.setSelectedPlayerType(PlayerType.HUMAN, playerToSelect);
            setVisible(false);
            gui.next();
        }

        else if(e.getSource()==back){
            setVisible(false);
            gui.previous();
        }

        else if (e.getSource()==exit){
        }
    }

    private void showWindow() {
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
