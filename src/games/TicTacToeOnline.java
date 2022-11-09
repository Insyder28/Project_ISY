package games;

import events.EventListener;
import gui.GUI;
import networking.GameSocket;
import networking.ServerRuntimeException;
import networking.ServerTimedOutException;
import players.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Class for playing Tic-Tac-Toe on a server.
 * @author Erwin Veenhoven
 */
public class TicTacToeOnline {
    public Board board = new Board(3, 3);
    private GUI gui;
    private GameSocket gameSocket;
    private Player player;
    private String opponentName;

    private int count = 0;   // Keeps track of how many moves have been set

    final AtomicBoolean foundMatch = new AtomicBoolean(false);
    final AtomicBoolean receivedOpponentMove = new AtomicBoolean(false);

    private final Object threadHolder = new Object();

    // Create EventListener objects, so they can be removed from the events later
    private final EventListener onMatch = this::onMatch;
    private final EventListener onYourTurn = this::onYourTurn;
    private final EventListener onMove = this::onMove;
    private final EventListener onLoss = this::onLoss;
    private final EventListener onWin = this::onWin;
    private final EventListener onDraw = this::onDraw;

    /**
     * Starts a multiplayer Tic-Tac-Toe game.
     * @param player The player type that plays the game.
     * @param gameSocket The transport for playing the game.
     */
    public void startGame(Player player, GameSocket gameSocket, GUI gui) {
        this.gui = gui;

        if (!gameSocket.isLoggedIn()) throw new ServerRuntimeException("Not logged in");   // Check if player is logged in.

        // Initialise
        this.gameSocket = gameSocket;
        this.player = player;

        gameSocket.onMatchEvent.addListener(onMatch);
        gameSocket.onYourTurnEvent.addListener(onYourTurn);
        gameSocket.onMoveEvent.addListener(onMove);
        gameSocket.onLossEvent.addListener(onLoss);
        gameSocket.onWinEvent.addListener(onWin);
        gameSocket.onDrawEvent.addListener(onDraw);

        // Subscribe to Tic-Tac-Toe
        System.out.println("Waiting for game...");
//        try {
//            System.out.println("Waiting for game...");
//            gameSocket.subscribe("tic-tac-toe");
//        }
//        catch (ServerTimedOutException e) {
//            onServerTimedOut(e);
//        }

        // Sleep thread until game is finished
        synchronized (threadHolder) {
            try { threadHolder.wait(); }
            catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void onMatch(String args) {
        Map<String, String> data = toMap(args);   // Create map from server data
        opponentName = data.get("OPPONENT");
        //System.out.println("Found match!\nPlaying against: " + opponentName);

        gui.setCurrentPlayer(Icon.CROSS);
        gui.updateBoard(board);

        if (!data.get("PLAYERTOMOVE").equals(gameSocket.getPlayerName())) {   // If opponent has first move
            player.setIcon(Icon.NOUGHT);

//            System.out.println("\n" + opponentName + "'s turn");
//            System.out.println(board);
//            System.out.println("\n" + opponentName + " entering move...");
        }
        else {
            player.setIcon(Icon.CROSS);

            synchronized (receivedOpponentMove) {
                receivedOpponentMove.set(true);
                receivedOpponentMove.notify();
            }
        }

        // Set foundMatch to true
        synchronized (foundMatch) {
            foundMatch.set(true);
            foundMatch.notify();
        }
    }

    private void onYourTurn(String args) {
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

        // Send the move to the server
        try {
            //System.out.println("\nYour turn\n" + board);
            gameSocket.move(player.move(board));
        }
        catch (ServerTimedOutException e) {
            onServerTimedOut(e);
        }
    }

    private void onMove(String args) {
        count++;   // Increment move counter

        Map<String, String> data = toMap(args);   // Convert server data to map
        boolean ownMove = data.get("PLAYER").equals(gameSocket.getPlayerName());  // Check if the move was done by us

        Icon currentPlayer = ownMove ? player.getIcon() : player.getIcon().opponentIcon();   // Get the icon of the current player
        board.set(Integer.parseInt(data.get("MOVE")), currentPlayer);   // Set the move on the board

        gui.updateBoard(board);
        gui.setCurrentPlayer(currentPlayer);

        if (!ownMove) {
            // Set receivedOpponentMove to true
            synchronized (receivedOpponentMove) {
                receivedOpponentMove.set(true);
                receivedOpponentMove.notify();
            }
        }
    }

    private void onLoss(String args) {
        //System.out.println("\nYou lost\n" + board);
        gui.updateBoard(board);
        gui.endGame("You lost");
        endGame();
    }

    private void onWin(String args) {
        //System.out.println("\nYou won!\n" + board);
        gui.updateBoard(board);
        gui.endGame("You won!");
        endGame();
    }

    private void onDraw(String args) {
        //System.out.println("\nIt's a draw\n" + board);
        gui.updateBoard(board);
        gui.endGame("It's a draw");
        endGame();
    }

    private void endGame() {
        // Remove all listeners from events
//        gameSocket.onMatchEvent.removeListener(onMatch);
//        gameSocket.onYourTurnEvent.removeListener(onYourTurn);
//        gameSocket.onMoveEvent.removeListener(onMove);
//        gameSocket.onLossEvent.removeListener(onLoss);
//        gameSocket.onWinEvent.removeListener(onWin);
//        gameSocket.onDrawEvent.removeListener(onDraw);

        // Notify the object that holds the lock on the main thread.
//        synchronized (threadHolder) {
//            threadHolder.notify();
//        }

        gui.setCurrentPlayer(Icon.NO_ICON);
        System.out.println("end: \n" + board);
        board.clear();
    }

    private void onServerTimedOut(Throwable e) {
        System.out.println("\nServerTimedOut");
        throw new RuntimeException(e);
    }

    private Map<String, String> toMap(String s) {
        Map<String, String> map = new HashMap<>();
        String[] pairs = s.substring(1, s.length() - 2).split(", ");   // Create pairs, example of pair: 'KEY: "value"'

        // Put pairs in map
        for (String pair : pairs) {
            String[] split = pair.split(": ");
            map.put(split[0], split[1].substring(1, split[1].length() - 1));
        }

        return map;
    }
}
