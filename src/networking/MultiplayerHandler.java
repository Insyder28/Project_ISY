package networking;

import events.EventListener;
import games.OnlineGame;
import games.TicTacToeOnline;
import gui.GUI;
import players.*;

import java.io.Closeable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class MultiplayerHandler implements Closeable {
    private GameSocket gameSocket;
    private GUI gui;
    private String playerName;

    private OnlineGame currentGame;

    final AtomicBoolean foundMatch = new AtomicBoolean(false);
    final AtomicBoolean receivedOpponentMove = new AtomicBoolean(false);

    // Create EventListener objects, so they can be removed from the events later
    private final EventListener onMatch = this::onMatch;
    private final EventListener onYourTurn = this::onYourTurn;
    private final EventListener onMove = this::onMove;
    private final EventListener onLoss = this::onLoss;
    private final EventListener onWin = this::onWin;
    private final EventListener onDraw = this::onDraw;

    public MultiplayerHandler(GameSocket gameSocket) {
        this.gui = GUI.getInstance();
        this.gameSocket = gameSocket;

        this.gameSocket.onMatchEvent.addListener(onMatch);
        this.gameSocket.onYourTurnEvent.addListener(onYourTurn);
        this.gameSocket.onMoveEvent.addListener(onMove);
        this.gameSocket.onLossEvent.addListener(onLoss);
        this.gameSocket.onWinEvent.addListener(onWin);
        this.gameSocket.onDrawEvent.addListener(onDraw);
    }

    public void subscribe(String gameType) {
        try { gameSocket.subscribe(gameType); }
        catch (ServerTimedOutException e) {
            onServerTimeout(e);
        }
    }

    @Override
    public void close() {
        this.gameSocket.onMatchEvent.removeListener(onMatch);
        this.gameSocket.onYourTurnEvent.removeListener(onYourTurn);
        this.gameSocket.onMoveEvent.removeListener(onMove);
        this.gameSocket.onLossEvent.removeListener(onLoss);
        this.gameSocket.onWinEvent.removeListener(onWin);
        this.gameSocket.onDrawEvent.removeListener(onDraw);
    }

    private void onMatch(String args) {
        Map<String, String> data = toMap(args);   // Create map from server data

        switch (data.get("GAMETYPE")) {
            case "Tic-tac-toe" -> startTicTacToe();
            case "Othello" -> startOthello();
        }

        currentGame.onMatch(data);

        // Set foundMatch to true
        synchronized (foundMatch) {
            foundMatch.set(true);
            foundMatch.notify();
        }

        // Set receivedOpponentMove to true when we have to do first move
        if (data.get("PLAYERTOMOVE").equals(gameSocket.getPlayerName())) {
            synchronized (receivedOpponentMove) {
                receivedOpponentMove.set(true);
                receivedOpponentMove.notify();
            }
        }
    }

    private void onYourTurn(String args) {
        Map<String, String> data = toMap(args);   // Create map from server data

        // Check if onMatch event has been called. If not sleep thread until it is called.
        synchronized (foundMatch) {
            if (!foundMatch.get()) {
                try { foundMatch.wait(); }
                catch (InterruptedException ignored) { }
            }
        }

        // Check if the opponent move has been set on the board before doing own move.
        synchronized (receivedOpponentMove) {
            if (!receivedOpponentMove.get()) {
                try { receivedOpponentMove.wait(); }
                catch (InterruptedException ignored) { }
            }
            receivedOpponentMove.set(false);
        }

        try {
            currentGame.onYourTurn(data);
        }
        catch (ServerTimedOutException e) {
            onServerTimeout(e);
        }
    }

    private void onMove(String args) {
        Map<String, String> data = toMap(args);   // Create map from server data

        currentGame.onMove(data);

        if (!data.get("PLAYER").equals(playerName)) {
            // Set receivedOpponentMove to true
            synchronized (receivedOpponentMove) {
                receivedOpponentMove.set(true);
                receivedOpponentMove.notify();
            }
        }
    }

    private void onLoss(String args) {
        Map<String, String> data = toMap(args);   // Create map from server data

        currentGame.onLoss(data);
    }

    private void onWin(String args) {
        Map<String, String> data = toMap(args);   // Create map from server data

        currentGame.onWin(data);
    }

    private void onDraw(String args) {
        Map<String, String> data = toMap(args);   // Create map from server data

        currentGame.onDraw(data);
    }


    private void startTicTacToe() {
        PlayerType playerType = gui.getSelectedPlayerType();
        Player player;

        switch (playerType) {
            case AI -> player = new AIPlayer();
            case RANDOM -> player = new RandomPlayer();
            default -> player = new HumanPlayer();
        }

        currentGame = new TicTacToeOnline(player, gameSocket);
    }

    private void startOthello() {

    }

    private void onServerTimeout(Throwable e) {
        gameSocket.close();
    }

    private Map<String, String> toMap(String s) {
        Map<String, String> map = new HashMap<>();
        if (s.length() < 6) return map;   // No valid map can be made with less than 6 chars.
        String[] pairs = s.substring(1, s.length() - 2).split(", ");   // Create pairs, example of pair: 'KEY: "value"'

        // Put pairs in map
        for (String pair : pairs) {
            String[] split = pair.split(": ");
            map.put(split[0], split[1].substring(1, split[1].length() - 1));
        }

        return map;
    }
}
