package games.Othello;

import games.data.Board;
import games.data.Icon;
import games.data.OnlineGame;
import gui.GUI;
import gui.OthelloGUI;
import main.GameController;
import networking.GameSocket;
import networking.ServerTimedOutException;
import players.Player;

import java.util.Map;
import java.util.concurrent.ExecutionException;

public class OthelloOnline implements OnlineGame {
    private final GameSocket gameSocket;
    private final GUI gui = GameController.getInstance().getGUI();
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
            player.setIcon(Icon.CROSS);
        else
            player.setIcon(Icon.NOUGHT);

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

        board.set(Integer.parseInt(data.get("MOVE")), currentPlayer);OthelloGUI.updateBoard(board);
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
}
