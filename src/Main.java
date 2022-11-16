import games.GameType;
import games.Icon;
import games.TicTacToe;
import gui.GUI;
import networking.GameSocket;
import networking.MultiplayerHandler;
import players.AIPlayer;
import players.HumanPlayer;
import players.Player;
import players.PlayerType;

import javax.swing.*;
import java.io.IOException;

public class Main {
    private static final GUI gui = new GUI();

    public static void main(String[] args) {
        switch (gui.selectMode()) {
            case LOCAL -> selectedLocal();
            case ONLINE -> selectedOnline();
        }
    }

    // Local
    private static void selectedLocal() {
        switch (gui.selectGameType()) {
            case TICTACTOE -> startLocalTicTacToe();
            case OTHELLO -> startLocalOthello();
        }
    }

    private static void startLocalTicTacToe() {
        Player xPlayer = gui.selectPlayerType(Icon.CROSS) == PlayerType.HUMAN ? new HumanPlayer() : new AIPlayer();
        Player oPlayer = gui.selectPlayerType(Icon.NOUGHT) == PlayerType.HUMAN ? new HumanPlayer() : new AIPlayer();
        new TicTacToe().startGame(xPlayer, oPlayer);
    }

    private static void startLocalOthello() {

    }

    // Online
    private static void selectedOnline() {
        PlayerType playerType = gui.selectPlayerType();
        String address = gui.getIp();

        gui.startLoading("Connecting to server...");
        int port = 7789;
        if (address.contains(":")) {
            String[] addressAndPort = address.split(":");
            port = Integer.parseInt(addressAndPort[1]);
            address = addressAndPort[2];
        }

        GameSocket gameSocket;

        try {
            gameSocket = new GameSocket(address, port);
        }
        catch (IOException e) {
            gui.stopLoading();
            gui.showMessage(e.getMessage(), "Failed to connect", JOptionPane.ERROR_MESSAGE);
        }
    }
}