package main;

import games.Othello.Othello;
import games.data.Icon;
import games.TicTacToe.TicTacToe;
import gui.ActionFailedException;
import gui.GUI;
import gui.GUIEventListener;
import networking.GameSocket;
import networking.MultiplayerHandler;
import networking.ServerException;
import players.*;
import util.InstanceAlreadyExistsException;
import util.InstanceNotFoundException;

import java.io.IOException;

public class GameController implements GUIEventListener {
    // Singleton logic
    private static GameController instance = null;
    public static GameController getInstance() {
        if (instance == null) throw new InstanceNotFoundException("There is no instance of GameController");
        return instance;
    }

    // Instance fields
    private final GUI gui = new GUI(this);
    private GameSocket gameSocket;
    private MultiplayerHandler multiplayerHandler;

    // Constructor
    public GameController() {
        if (instance != null) throw new InstanceAlreadyExistsException("There is already an instance of GameController");
        instance = this;
    }

    // Methods
    public GUI getGUI() {
        return this.gui;
    }

    public MultiplayerHandler getMultiplayerHandler() {
        return multiplayerHandler;
    }

    // Interface Methods
    @Override
    public void onConnect(String ipAddress) throws ActionFailedException {
        //TODO: pass port and IP separately

        String address;
        int port = 7789;

        if (ipAddress.contains(":")) {
            String[] addressAndPort = ipAddress.split(":");

            address = addressAndPort[0];
            port = Integer.parseInt(addressAndPort[1]);
        }
        else {
            address = ipAddress;
        }


        try {
            gameSocket = new GameSocket(address, port);
        }
        catch (IOException e) {
            throw new ActionFailedException("Failed to connect to the server.", e);
        }
    }

    @Override
    public void onLogin(String name) throws ActionFailedException {

        try {
            gameSocket.login(name);
        }
        catch (ServerException e) {
            gui.stopLoading();
            throw new ActionFailedException(e);
        }

        multiplayerHandler = new MultiplayerHandler(gameSocket, gui.getSelectedPlayerType());
    }

    @Override
    public void onDisconnect() {
        gameSocket.close();
        multiplayerHandler.close();
        gameSocket = null;
        multiplayerHandler = null;
    }

    @Override
    public void onStartLocalGame() {
        switch (gui.getSelectedGameType()) {
            case TICTACTOE -> {
                Player xPlayer = gui.getSelectedPlayerType(Icon.CROSS) == PlayerType.HUMAN ? new HumanPlayer() : new AIPlayer();
                Player oPlayer = gui.getSelectedPlayerType(Icon.NOUGHT) == PlayerType.HUMAN ? new HumanPlayer() : new AIPlayer();

                new TicTacToe().startGame(xPlayer, oPlayer);
            }
            case OTHELLO -> {
                Player xPlayer = gui.getSelectedPlayerType(Icon.CROSS) == PlayerType.HUMAN ? new HumanPlayer() : new OthelloAIMT();
                Player oPlayer = gui.getSelectedPlayerType(Icon.NOUGHT) == PlayerType.HUMAN ? new HumanPlayer() : new OthelloAIMT();

                new Othello().startGame(xPlayer, oPlayer);
            }
        }
    }
}
