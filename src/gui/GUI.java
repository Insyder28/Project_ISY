package gui;

import events.EventListener;
import games.GameType;
import games.Mode;
import games.Icon;
import players.PlayerType;
import util.InstanceNotFoundException;

import javax.swing.*;

public class GUI {
    // Instance fields
    private final SelectModeWindow selectModeWindow = new SelectModeWindow();
    private final SelectGameWindow selectGameWindow = new SelectGameWindow();
    private final SelectPlayerTypeWindow selectPlayerTypeWindow = new SelectPlayerTypeWindow();
    private final ConnectWindow connectWindow = new ConnectWindow();
    private final LoadingWindow loadingWindow = new LoadingWindow();

    private Mode selectedMode = null;
    private GameType selectedGameType = null;
    private PlayerType selectedPlayerType = null;
    private PlayerType selectedXPlayerType = null;
    private PlayerType selectedOPlayerType = null;

    private TicTacToeGUI ticTacToeGUI = null;

    private EventListener onStartLocalGame;

    // Constructor
    public GUI(EventListener onStartLocalGame) {
        this.onStartLocalGame = onStartLocalGame;
        next();
    }

    // Getters and Setters
    public TicTacToeGUI getTicTacToeGUI() {
        if (ticTacToeGUI == null) throw new InstanceNotFoundException("There is currently no instance of TicTacToeGUI");
        return ticTacToeGUI;
    }

    public Mode getSelectedMode() {
        return this.selectedMode;
    }

    public GameType getSelectedGameType() {
        return selectedGameType;
    }

    public PlayerType getSelectedPlayerType() {
        return getSelectedPlayerType(Icon.NO_ICON);
    }

    public PlayerType getSelectedPlayerType(Icon player) {
        switch (player) {
            case CROSS -> { return selectedXPlayerType; }
            case NOUGHT -> { return selectedOPlayerType; }
            default ->  { return selectedPlayerType; }
        }
    }

    void setSelectedMode(Mode selectedMode) {
        this.selectedMode = selectedMode;
    }

    void setSelectedGameType(GameType gameType) {
        this.selectedGameType = gameType;
    }

    void setSelectedPlayerType(PlayerType playerType, Icon player) {
        switch (player) {
            case CROSS -> this.selectedXPlayerType = playerType;
            case NOUGHT -> this.selectedOPlayerType = playerType;
            default -> this.selectedPlayerType = playerType;
        }
    }

    // Public Methods
    public void startLoading(String message) {
        loadingWindow.startLoading(message);
    }

    public void stopLoading() {
        loadingWindow.stopLoading();
    }

    public void showMessage(String message, String title,  int messageType) {
        JOptionPane.showMessageDialog(null, message, title, messageType);
    }

    public TicTacToeGUI startTicTacToe() {
        TicTacToeGUI gui = new TicTacToeGUI();
        this.ticTacToeGUI = gui;
        return gui;
    }

    public void dispose() {
        selectPlayerTypeWindow.dispose();
        selectGameWindow.dispose();
        selectModeWindow.dispose();
    }

    // Package private Methods
    void next() {
        new Thread(() -> {
            if (selectedMode == null) {
                selectModeWindow.mainFrame();
                return;
            }

            switch (selectedMode) {
                case LOCAL -> {
                    if (selectedGameType == null) selectGameWindow.mainFrame();
                    else if (selectedXPlayerType == null) selectPlayerTypeWindow.mainFrame(Icon.CROSS);
                    else if (selectedOPlayerType == null) selectPlayerTypeWindow.mainFrame(Icon.NOUGHT);
                    else onStartLocalGame.onEvent(null);
                }

                case ONLINE -> {
                    if (selectedPlayerType == null) selectPlayerTypeWindow.mainFrame();
                }
            }
        }).start();
    }

    void previous() {
        if (selectedMode == null) return;

        switch (selectedMode) {
            case LOCAL -> {
                if (selectedGameType == null) {
                    selectedMode = null;
                    selectModeWindow.mainFrame();
                }
                else if (selectedXPlayerType == null) {
                    selectedGameType = null;
                    selectGameWindow.mainFrame();
                }
                else if (selectedOPlayerType == null) {
                    selectedXPlayerType = null;
                    selectPlayerTypeWindow.mainFrame(Icon.CROSS);
                }
            }
            case ONLINE -> {}
        }
    }
}
