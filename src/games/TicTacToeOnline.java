package games;

import events.EventListener;
import networking.GameSocket;
import networking.ServerRuntimeException;
import networking.ServerTimedOutException;
import players.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;


public class TicTacToeOnline {
    private Board board;
    private GameSocket gameSocket;
    private Player player;
    private String opponentName;
    private int count = 0;

    final AtomicBoolean foundMatch = new AtomicBoolean(false);
    final AtomicBoolean receivedOpponentMove = new AtomicBoolean(false);

    private final EventListener onMatch = this::onMatch;
    private final EventListener onYourTurn = this::onYourTurn;
    private final EventListener onMove = this::onMove;
    private final EventListener onLoss = this::onLoss;
    private final EventListener onWin = this::onWin;
    private final EventListener onDraw = this::onDraw;

    public void startGame(Player player, GameSocket gameSocket) {
        if (!gameSocket.isLoggedIn()) throw new ServerRuntimeException("Not logged in");

        this.gameSocket = gameSocket;
        this.player = player;
        this.board = new Board(3, 3);
        player.setIcon(Icon.CROSS);

        gameSocket.onMatchEvent.addListener(onMatch);
        gameSocket.onYourTurnEvent.addListener(onYourTurn);
        gameSocket.onMoveEvent.addListener(onMove);
        gameSocket.onLossEvent.addListener(onLoss);
        gameSocket.onWinEvent.addListener(onWin);
        gameSocket.onDrawEvent.addListener(onDraw);

        try {
            System.out.println("Waiting for game...");
            gameSocket.subscribe("tic-tac-toe");
        }
        catch (ServerTimedOutException e) {
            onServerTimedOut(e);
        }

        synchronized (this) {
            try { this.wait(); }
            catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void onMatch(String args) {
        Map<String, String> data = toMap(args);
        opponentName = data.get("OPPONENT");
        System.out.println("Found match!\nPlaying against: " + opponentName);

        if (!data.get("PLAYERTOMOVE").equals(gameSocket.getPlayerName())) {
            System.out.println("\n" + opponentName + "'s turn\n" + board + "\n\n" + opponentName + " entering move...");
        }
        else {
            synchronized (receivedOpponentMove) {
                receivedOpponentMove.set(true);
                receivedOpponentMove.notify();
            }
        }

        synchronized (foundMatch) {
            foundMatch.set(true);
            foundMatch.notify();
        }
    }

    private void onYourTurn(String args) {
        synchronized (foundMatch) {
            if (!foundMatch.get()) {
                try { foundMatch.wait(); }
                catch (InterruptedException ignored) { }
            }
        }

        synchronized (receivedOpponentMove) {
            if (!receivedOpponentMove.get()) {
                try { receivedOpponentMove.wait(); }
                catch (InterruptedException ignored) { }
            }
            receivedOpponentMove.set(false);
        }

        try {
            System.out.println("\nYour turn\n" + board);
            gameSocket.move(player.move(board));
        }
        catch (ServerTimedOutException e) {
            onServerTimedOut(e);
        }
    }

    private void onMove(String args) {
        count++;

        Map<String, String> data = toMap(args);
        boolean ownMove = data.get("PLAYER").equals(gameSocket.getPlayerName());

        Icon currentPlayer = ownMove ? player.getIcon() : player.getIcon().opponentIcon();
        board.set(Integer.parseInt(data.get("MOVE")), currentPlayer);

        if (ownMove) {
            if (!checkWinner(currentPlayer) && count < 9)
                System.out.println("\n" + opponentName + "'s turn\n" + board + "\n\n" + opponentName + " entering move...");
        }
        else {
            synchronized (receivedOpponentMove) {
                receivedOpponentMove.set(true);
                receivedOpponentMove.notify();
            }
        }
    }

    private void onLoss(String args) {
        System.out.println("\nYou lost\n" + board);
        endGame();
    }

    private void onWin(String args) {
        System.out.println("\nYou won!\n" + board);
        endGame();
    }

    private void onDraw(String args) {
        System.out.println("\nIt's a draw!\n" + board);
        endGame();
    }

    private void endGame() {
        gameSocket.onMatchEvent.removeListener(onMatch);
        gameSocket.onYourTurnEvent.removeListener(onYourTurn);
        gameSocket.onMoveEvent.removeListener(onMove);
        gameSocket.onLossEvent.removeListener(onLoss);
        gameSocket.onWinEvent.removeListener(onWin);
        gameSocket.onDrawEvent.removeListener(onDraw);

        synchronized (this) {
            this.notify(); //TODO: use private object
        }
    }

    private void onServerTimedOut(Throwable e) {
        System.out.println("\nServerTimedOut");
        throw new RuntimeException(e);
    }

    private Map<String, String> toMap(String s) {
        Map<String, String> map = new HashMap<>();
        String[] pairs = s.substring(1, s.length() - 2).split(", ");

        for (String pair : pairs) {
            String[] split = pair.split(": ");
            map.put(split[0], split[1].substring(1, split[1].length() - 1));
        }

        return map;
    }


    private boolean checkWinner(Icon icon) {
        // Check rows
        for (int i = 0; i < board.height; i++)
            if (checkRow(i, icon)) return true;

        // Check columns
        for (int i = 0; i < board.width; i++) {
            if (checkCol(i, icon)) return true;
        }

        // Check top-left to bottom-right
        if (board.data[0][0] == icon && board.data[1][1] == icon && board.data[2][2] == icon)
            return true;

        // Check bottom-left to top right
        return board.data[2][0] == icon && board.data[1][1] == icon && board.data[0][2] == icon;
    }

    private boolean checkRow(int index, Icon icon) {
        for (Icon col : board.data[index])
            if (col != icon) return false;
        return true;
    }

    private boolean checkCol(int index, Icon icon) {
        for (int i = 0; i < board.height; i++)
            if (board.data[i][index] != icon) return false;
        return true;
    }
}
