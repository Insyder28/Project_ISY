package games.Othello;

import games.data.Board;
import games.data.Icon;
import gui.OthelloGUI;
import main.GameController;
import players.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.ExecutionException;

public class Othello {
    private final Board board;
    private boolean gameRunning = false;

    private long oldTime = System.currentTimeMillis();
    private long longestTime = 0;

    public Othello() {
        this.board = new Board(8, 8);
    }

    public long getLongestTime() {
        return longestTime;
    }

    public Icon startGame(Player xPlayer, Player oPlayer) {
        if (gameRunning) throw new RuntimeException("Game already running");
        gameRunning = true;

        OthelloGUI othelloGUI = GameController.getInstance().getGUI().startOthello();

        // Board setup
        boardSetup();

        // Player setup
        Player[] players = getPlayers(xPlayer, oPlayer);

        // Game start
        boolean loop = true;
        Player winner = null;

        while (loop) {
            for (Player player : players) {

                System.out.println("\n" + player.getIcon() + "'s turn\n");
                othelloGUI.updateBoard(board);
                othelloGUI.setCurrentPlayer(player.getIcon());

                int pos;
                try {
                    pos = player.move(board);
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }

                elapsedTime();

                if (pos != -9) {
                    int row = pos / board.width;
                    int col = pos % board.width;

                    if (!validateMove(row, col, player.getIcon())) {
                        if (player.getIcon() == Icon.CROSS) winner = oPlayer;
                        else winner = xPlayer;

                        System.out.println("Invalid move!");
                        System.out.println(board);
                        othelloGUI.updateBoard(board);
                        othelloGUI.endGame("Invalid move! " + winner.getIcon() + " has won");

                        loop = false;
                        break;
                    }

                    makeMove(row, col, player.getIcon());
                }
                if (gameOver(board)) {
                    int x = countPieces(Icon.CROSS);
                    int o = countPieces(Icon.NOUGHT);

                    winner = oPlayer;
                    if (x > o) {
                        winner = xPlayer;
                    }

                    System.out.println("No more moves! " + winner.getIcon() + " wins!");
                    System.out.println(board);
                    othelloGUI.updateBoard(board);
                    othelloGUI.endGame("No more moves! " + winner.getIcon() + " wins!");
                    loop = false;
                    break;
                }
            }
        }
        System.out.println(winner.getIcon() + " has won");
        System.out.println(board);
        othelloGUI.updateBoard(board);
        othelloGUI.endGame(winner.getIcon() + " has won");
        return winner.getIcon();
    }

    private void elapsedTime() {
        long currentTime = System.currentTimeMillis();
        System.out.println(currentTime - oldTime + " milliseconds, this turn");
        if (currentTime - oldTime > longestTime){
            longestTime = currentTime - oldTime;
        }
        System.out.println(longestTime + " milliseconds, at most");
        oldTime = System.currentTimeMillis();
    }

    private static Player[] getPlayers(Player xPlayer, Player oPlayer) {
        Player[] players = new Player[2];

        players[0] = xPlayer;
        xPlayer.setIcon(Icon.CROSS);

        players[1] = oPlayer;
        oPlayer.setIcon(Icon.NOUGHT);
        return players;
    }

    private void boardSetup() {
        board.clear();
        board.set(27, Icon.CROSS);
        board.set(28, Icon.NOUGHT);
        board.set(35, Icon.NOUGHT);
        board.set(36, Icon.CROSS);
        System.out.println(board);
    }

    int countPieces(Icon icon) {
        int counter = 0;
        for (int row = 0; row < board.height; row++) {
            for (int col = 0; col < board.width; col++) {
                if (board.data[row][col] == icon) {
                    counter++;
                }
            }
        }
        return counter;
    }

    void makeMove(int row, int col, Icon icon) {
        String[] directions = {"UpLeft", "Up", "UpRight", "Left", "Right", "BottomLeft", "Bottom", "BottomRight"};

        for (String dir : directions) {
            if (checkDirection(dir, row, col, icon)) {
                flipDirection(dir, row, col, icon);
            }
        }
    }

    private List<int[]> generateMoves(Board board, Icon icon) {
        List<int[]> nextMoves = new ArrayList<>(); // allocate List

        for (int row = 0; row < board.height; row++) {
            for (int col = 0; col < board.width; col++) {
                if (validateMove(row, col, icon)) {
                    nextMoves.add(new int[] {row, col});
                }
            }
        }
        return nextMoves;
    }

    boolean gameOver(Board board) {
        return board.generateMoves(Icon.CROSS).isEmpty() && board.generateMoves(Icon.NOUGHT).isEmpty();
    }

    boolean validateMove(int row, int col, Icon icon) {
        String[] directions = {"UpLeft", "Up", "UpRight", "Left", "Right", "BottomLeft", "Bottom", "BottomRight"};

        if (board.data[row][col] != Icon.NO_ICON) {
            return false;
        }

        for (String dir : directions) {
            if (checkDirection(dir, row, col, icon)) {
                return true;
            }
        }

        return false;
    }

    boolean checkDirection(String dir, int row, int col, Icon icon) {
        Icon oppIcon = icon == Icon.CROSS ? Icon.NOUGHT : Icon.CROSS;

        int deltaRow = 0, deltaCol = 0;
        if ("UpLeft".equals(dir)) {
            deltaRow = -1;
            deltaCol = -1;
        } else if ("Up".equals(dir)) {
            deltaRow = -1;
        } else if ("UpRight".equals(dir)) {
            deltaRow = -1;
            deltaCol = 1;
        } else if ("Left".equals(dir)) {
            deltaCol = -1;
        } else if ("Right".equals(dir)) {
            deltaCol = 1;
        } else if ("BottomLeft".equals(dir)) {
            deltaRow = 1;
            deltaCol = -1;
        } else if ("Bottom".equals(dir)) {
            deltaRow = 1;
        } else if ("BottomRight".equals(dir)) {
            deltaRow = 1;
            deltaCol = 1;
        }
        if (row + deltaRow < 0 || col + deltaCol < 0 || row + deltaRow >= board.height || col + deltaCol >= board.width) {
            return false; // out of bounds
        }
        if (board.data[row + deltaRow][col + deltaCol] == oppIcon) {
            while ((row >= 0) && (row < board.height) && (col >= 0) && (col < board.width)) {
                row += deltaRow;
                col += deltaCol;
                if (row < 0 || col < 0 || row >= board.height || col >= board.width) {
                    return false; // out of bounds, so edge of board reached
                }
                if (board.data[row][col] == Icon.NO_ICON) {
                    return false;  // There is a gap
                }
                if (board.data[row][col] == icon) {
                    return true;  // There is a piece of our own after opponents piece(s)
                }
            }
        }
        return false;  // Edge of board or no oppIcon
    }
    void flipDirection(String dir, int row, int col, Icon icon) {
        int deltaRow = 0, deltaCol = 0;
        if ("UpLeft".equals(dir)) {
            deltaRow = -1;
            deltaCol = -1;
        } else if ("Up".equals(dir)) {
            deltaRow = -1;
        } else if ("UpRight".equals(dir)) {
            deltaRow = -1;
            deltaCol = 1;
        } else if ("Left".equals(dir)) {
            deltaCol = -1;
        } else if ("Right".equals(dir)) {
            deltaCol = 1;
        } else if ("BottomLeft".equals(dir)) {
            deltaRow = 1;
            deltaCol = -1;
        } else if ("Bottom".equals(dir)) {
            deltaRow = 1;
        } else if ("BottomRight".equals(dir)) {
            deltaRow = 1;
            deltaCol = 1;
        }
        board.data[row][col] = icon;
        row += deltaRow;
        col += deltaCol;

        while (board.data[row][col] != icon) {
            board.data[row][col] = icon;
            row += deltaRow;
            col += deltaCol;
        }
    }
}
