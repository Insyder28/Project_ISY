package networking;

import events.EventListener;
import games.Othello.OthelloOnline;
import games.data.OnlineGame;
import games.TicTacToe.TicTacToeOnline;
import players.*;

import java.io.Closeable;
import java.util.HashMap;
import java.util.Map;

//TODO: fix player sometimes not putting move on the board
public class MultiplayerHandler implements Closeable {
    private final GameSocket gameSocket;

    private final PlayerType playerType;

    private OnlineGame currentGame;

    // Create EventListener objects, so they can be removed from the events later
    private final EventListener onMatch = this::onMatch;
    private final EventListener onYourTurn = this::onYourTurn;
    private final EventListener onMove = this::onMove;
    private final EventListener onLoss = this::onLoss;
    private final EventListener onWin = this::onWin;
    private final EventListener onDraw = this::onDraw;

    public MultiplayerHandler(GameSocket gameSocket, PlayerType playerType) {
        this.gameSocket = gameSocket;
        this.playerType = playerType;

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
            case "Reversi" -> startOthello();
        }

        currentGame.onMatch(data);
    }

    private void onYourTurn(String args) {
        Map<String, String> data = toMap(args);   // Create map from server data

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
    }

    private void onLoss(String args) {
        Map<String, String> data = toMap(args);   // Create map from server data
        currentGame.onLoss(data);
        endMatch();
    }

    private void onWin(String args) {
        Map<String, String> data = toMap(args);   // Create map from server data
        currentGame.onWin(data);
        endMatch();
    }

    private void onDraw(String args) {
        Map<String, String> data = toMap(args);   // Create map from server data
        currentGame.onDraw(data);
        endMatch();
    }

    private void endMatch() {
    }


    private void startTicTacToe() {
        Player player;

        switch (playerType) {
            case AI -> player = new AIPlayer();
            case RANDOM -> player = new RandomPlayer();
            default -> player = new HumanPlayer();
        }

        currentGame = new TicTacToeOnline(player, gameSocket);
    }

    private void startOthello() {
        Player player;

        switch (playerType) {
            case AI -> player = new OthelloAIMT();
            case RANDOM -> player = new RandomPlayer();
            default -> player = new HumanPlayer();
        }

        currentGame = new OthelloOnline(player, gameSocket);
    }

    private void onServerTimeout(Throwable ignored) {
        gameSocket.close();
        close();
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
