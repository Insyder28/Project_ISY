package gui;

import games.Icon;
import main.GameController;
import players.PlayerType;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class SelectPlayerTypeWindow extends JFrame implements ActionListener {
    private final JButton ai = new JButton();
    private final JButton human = new JButton();
    private final JLabel label = new JLabel();
    private final JButton back = new JButton();

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

        add(back);
        add(human);
        add(ai);
        add(label);

        back.setBounds(20, 20, 75, 75);
        back.setText("Return");
        back.addActionListener(this);
    }

    public void mainFrame() {
        mainFrame(Icon.NO_ICON);
    }

    public void mainFrame(Icon playerToSelect) {
        this.playerToSelect = playerToSelect;

        if (playerToSelect == Icon.NO_ICON) label.setText("Select Player Type");
        else label.setText("Select Player Type for " + playerToSelect.getChar());

        setLocation(GameController.getInstance().getGUI().getLastLocation());
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        GUI gui = GameController.getInstance().getGUI();

        if (e.getSource()== ai){
            gui.setSelectedPlayerType(PlayerType.AI, playerToSelect);
            setVisible(false);
            gui.setLastLocation(getLocation());
            gui.nextWindow();
        }

        else if (e.getSource()== human){
            gui.setSelectedPlayerType(PlayerType.HUMAN, playerToSelect);
            setVisible(false);
            gui.setLastLocation(getLocation());
            gui.nextWindow();
        }

        else if(e.getSource()==back){
            setVisible(false);
            gui.setLastLocation(getLocation());
            gui.previousWindow();
        }
    }
}
