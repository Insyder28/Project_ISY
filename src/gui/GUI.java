package gui;

import games.Board;
import games.GameType;
import games.Icon;
import games.Mode;
import players.PlayerType;

import javax.swing.*;

public class GUI {
    // Singleton logic
    private static GUI instance = null;
    public static GUI getInstance() {
        if (instance == null) throw new InstanceNotFoundException("There is no instance of GUI");
        return instance;
    }

    // Instance fields
    private final SelectPlayerTypeWindow selectPlayerTypeWindow = new SelectPlayerTypeWindow();
    private final SelectModeWindow selectModeWindow = new SelectModeWindow();
    private final SelectGameWindow selectGameWindow = new SelectGameWindow();

    private TicTacToeGUI ticTacToeGUI = null;

    // Constructor
    public GUI() {
        if (instance != null) throw new InstanceAlreadyExistsException("There is already an instance of GUI");
        instance = this;
    }

    // Getters and Setters
    public TicTacToeGUI getTicTacToeGUI() {
        if (ticTacToeGUI == null) throw new InstanceNotFoundException("There is no instance of TicTacToeGUI");
        return ticTacToeGUI;
    }

    // Public Methods
    public PlayerType selectPlayerType() {
        return selectPlayerTypeWindow.getPlayerType();
    }

    public PlayerType selectPlayerType(Icon icon) {
        return selectPlayerTypeWindow.getPlayerType(icon);
    }

    public Mode selectMode() {
        return selectModeWindow.getMode();
    }

    public GameType selectGameType() {
        return selectGameWindow.getGameType();
    }

    public TicTacToeGUI startTicTacToe() {
        TicTacToeGUI gui = new TicTacToeGUI();
        this.ticTacToeGUI = gui;
        return gui;
    }
}
