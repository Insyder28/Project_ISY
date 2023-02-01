package games.Othello;

import games.data.Board;
import games.data.Icon;
import games.data.OnlineGame;
import gui.GUI;
import main.GameController;
import networking.GameSocket;
import networking.ServerTimedOutException;
import players.Player;

import java.util.Map;
import java.util.concurrent.ExecutionException;

public class OthelloOnline implements OnlineGame {
    private final GameSocket gameSocket;
    private final Player player;
    private gui.OthelloGUI OthelloGUI;
    private final Board board = new Board(8, 8);

    public OthelloOnline(Player player, GameSocket gameSocket) {
        this.player = player;
        this.gameSocket = gameSocket;

        boardSetup();
    }

    private void boardSetup() {
        board.clear();
        board.set(27, Icon.CROSS);
        board.set(28, Icon.NOUGHT);
        board.set(35, Icon.NOUGHT);
        board.set(36, Icon.CROSS);
        System.out.println(board);
    }

    @Override
    public void onMatch(Map<String, String> data) {
        OthelloGUI = GameController.getInstance().getGUI().startOthello(true);

        if (data.get("PLAYERTOMOVE").equals(gameSocket.getPlayerName()))
            player.setIcon(Icon.NOUGHT);
        else
            player.setIcon(Icon.CROSS);

        OthelloGUI.setPlayer(player.getIcon());
        OthelloGUI.setCurrentPlayer(Icon.CROSS);
        OthelloGUI.updateBoard(board);
    }

    @Override
    public void onYourTurn(Map<String, String> data) {
        try {
            int move = player.move(board);
            gameSocket.move(move);
        }
        catch (InterruptedException | ServerTimedOutException ignored) { } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onMove(Map<String, String> data) {
        Icon currentPlayer = data.get("PLAYER").equals(gameSocket.getPlayerName())
                ? player.getIcon() : player.getIcon().opponentIcon();

        int move = Integer.parseInt(data.get("MOVE"));
        int row = move / board.width;
        int col = move % board.width;
        makeMove(row, col, currentPlayer);

        OthelloGUI.updateBoard(board);
        OthelloGUI.setCurrentPlayer(currentPlayer.opponentIcon());
    }

    @Override
    public void onLoss(Map<String, String> data) {
        OthelloGUI.updateBoard(board);
        OthelloGUI.endGame("You lost");
    }

    @Override
    public void onWin(Map<String, String> data) {
        OthelloGUI.updateBoard(board);
        OthelloGUI.endGame("You won!");
    }

    @Override
    public void onDraw(Map<String, String> data) {
        OthelloGUI.updateBoard(board);
        OthelloGUI.endGame("It's a draw");
    }

    void makeMove(int row, int col, Icon icon) {
        String[] directions = {"UpLeft", "Up", "UpRight", "Left", "Right", "BottomLeft", "Bottom", "BottomRight"};

        for (String dir : directions) {
            if (checkDirection(dir, row, col, icon)) {
                flipDirection(dir, row, col, icon);
            }
        }
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
