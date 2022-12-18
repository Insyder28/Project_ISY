package games.TicTacToe;

import games.data.OnlineGame;
import games.data.Board;
import games.data.Icon;
import networking.GameSocket;
import networking.ServerTimedOutException;
import players.Player;

import java.util.Map;

public class TicTacToeOnline implements OnlineGame {
    private final Board board = new Board(3, 3);
    private final GameSocket gameSocket;
    private final Player player;

    public TicTacToeOnline(Player player, GameSocket gameSocket) {
        this.player = player;
        this.gameSocket = gameSocket;
    }

    @Override
    public void onMatch(Map<String, String> data) {
        if (data.get("PLAYERTOMOVE").equals(gameSocket.getPlayerName()))
            player.setIcon(Icon.CROSS);
        else
            player.setIcon(Icon.NOUGHT);
    }

    @Override
    public void onYourTurn(Map<String, String> data) throws ServerTimedOutException {
        try {
            int move = player.move(board);
            gameSocket.move(move);
        }
        catch (InterruptedException ignored) { }
    }

    @Override
    public void onMove(Map<String, String> data) {
        Icon currentPlayer = data.get("PLAYER").equals(gameSocket.getPlayerName())
                ? player.getIcon() : player.getIcon().opponentIcon();

        board.set(Integer.parseInt(data.get("MOVE")), currentPlayer);
    }

    @Override
    public void onLoss(Map<String, String> data) {
    }

    @Override
    public void onWin(Map<String, String> data) {
    }

    @Override
    public void onDraw(Map<String, String> data) {
    }
}
