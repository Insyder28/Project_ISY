package gui;

import games.GameType;
import games.Mode;
import games.Icon;
import players.PlayerType;

import java.awt.*;

public class GUI {
    // Instance fields
    private Point lastLocation = null;

    private final SelectModeWindow selectModeWindow = new SelectModeWindow();
    private final SelectGameWindow selectGameWindow = new SelectGameWindow();
    private final SelectPlayerTypeWindow selectPlayerTypeWindow = new SelectPlayerTypeWindow();
    private final ConnectWindow connectWindow = new ConnectWindow();
    private final LoginWindow loginWindow = new LoginWindow();
    private final MultiplayerWindow multiplayerWindow = new MultiplayerWindow();

    private final LoadingWindow loadingWindow = new LoadingWindow();

    private Mode selectedMode = null;
    private GameType selectedGameType = null;
    private PlayerType selectedPlayerType = null;
    private PlayerType selectedXPlayerType = null;
    private PlayerType selectedOPlayerType = null;
    private boolean isConnected = false;
    private boolean isLoggedIn = false;

    private TicTacToeGUI ticTacToeGUI = null;

    final GUIEventListener guiEventListener;

    // Constructor
    public GUI(GUIEventListener guiEventListener) {
        this.guiEventListener = guiEventListener;
        nextWindow();
    }

    // Getters and Setters
    public TicTacToeGUI getTicTacToeGUI() {
        return ticTacToeGUI;
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

    public Point getLastLocation() {
        return lastLocation;
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

    void connected() {
        this.isConnected = true;
    }

    void loggedIn() {
        this.isLoggedIn = true;
    }

    public void setLastLocation(Point lastLocation) {
        this.lastLocation = lastLocation;
    }

    // Public Methods

    public void stopLoading() {
        loadingWindow.stopLoading();
    }

    public TicTacToeGUI startTicTacToe() {
        return startTicTacToe(false);
    }

    public TicTacToeGUI startTicTacToe(boolean showPlayer) {
        if (this.ticTacToeGUI != null) this.ticTacToeGUI.dispose();
        this.ticTacToeGUI = new TicTacToeGUI(showPlayer);
        return this.ticTacToeGUI;
    }

    // Package private Methods
    void nextWindow() {
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
                    else guiEventListener.onStartLocalGame();
                }

                case ONLINE -> {
                    if (selectedPlayerType == null) selectPlayerTypeWindow.mainFrame();
                    else if (!isConnected) connectWindow.mainFrame();
                    else if (!isLoggedIn) loginWindow.mainFrame();
                    else multiplayerWindow.mainFrame();
                }
            }
        }).start();
    }

    void previousWindow() {
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
            case ONLINE -> {
                if (selectedPlayerType == null) {
                    selectedMode = null;
                    selectModeWindow.mainFrame();
                }
                else if (!isConnected) {
                    selectedPlayerType = null;
                    selectPlayerTypeWindow.mainFrame();
                }
            }
        }
    }

    void disconnect() {
        guiEventListener.onDisconnect();
        isConnected = false;
        isLoggedIn = false;
        selectedPlayerType = null;
        selectModeWindow.mainFrame();
    }

    void gameEnded() {
        if (selectedMode == Mode.LOCAL){
            selectedMode = null;
            selectedGameType = null;
            selectedPlayerType = null;
            selectedXPlayerType = null;
            selectedOPlayerType = null;
            isConnected = false;
            isLoggedIn = false;
        }
    }
}
