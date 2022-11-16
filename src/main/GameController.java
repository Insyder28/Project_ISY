package main;

import games.Icon;
import games.TicTacToe;
import gui.GUI;
import players.AIPlayer;
import players.HumanPlayer;
import players.Player;
import players.PlayerType;
import util.InstanceAlreadyExistsException;
import util.InstanceNotFoundException;

public class GameController {
    // Singleton logic
    private static GameController instance = null;
    public static GameController getInstance() {
        if (instance == null) throw new InstanceNotFoundException("There is no instance of GameController");
        return instance;
    }

    // Instance fields
    private final GUI gui = new GUI(this::onStartLocalGame);

    // Constructor
    public GameController() {
        if (instance != null) throw new InstanceAlreadyExistsException("There is already an instance of GameController");
        instance = this;
    }

    // Methods
    public GUI getGUI() {
        return this.gui;
    }


    private void onStartLocalGame(String ignored) {
        switch (gui.getSelectedGameType()) {
            case TICTACTOE -> {
                Player xPlayer = gui.getSelectedPlayerType(Icon.CROSS) == PlayerType.HUMAN ? new HumanPlayer() : new AIPlayer();
                Player oPlayer = gui.getSelectedPlayerType(Icon.NOUGHT) == PlayerType.HUMAN ? new HumanPlayer() : new AIPlayer();

                new TicTacToe().startGame(xPlayer, oPlayer);
            }
            case OTHELLO -> {}
        }
    }
}
