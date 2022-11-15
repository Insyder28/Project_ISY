package games;

import gui.GUI;
import gui.TicTacToeGUI;
import networking.GameSocket;
import networking.ServerTimedOutException;
import players.Player;

import java.util.Map;

public class TicTacToeOnline implements OnlineGame {
    private final Board board = new Board(3, 3);
    private final GameSocket gameSocket;
    private final Player player;
    private TicTacToeGUI ticTacToeGUI;

    public TicTacToeOnline(Player player, GameSocket gameSocket) {
        this.player = player;
        this.gameSocket = gameSocket;
    }

    @Override
    public void onMatch(Map<String, String> data) {
        ticTacToeGUI = GUI.getInstance().startTicTacToe();

        if (data.get("PLAYERTOMOVE").equals(gameSocket.getPlayerName()))
            player.setIcon(Icon.CROSS);
        else
            player.setIcon(Icon.NOUGHT);

        ticTacToeGUI.setCurrentPlayer(Icon.CROSS);
        ticTacToeGUI.updateBoard(board);
    }

    @Override
    public void onYourTurn(Map<String, String> data) throws ServerTimedOutException {
        gameSocket.move(player.move(board));
    }

    @Override
    public void onMove(Map<String, String> data) {
        Icon currentPlayer = data.get("PLAYER").equals(gameSocket.getPlayerName())
                ? player.getIcon() : player.getIcon().opponentIcon();

        board.set(Integer.parseInt(data.get("MOVE")), currentPlayer);

        ticTacToeGUI.updateBoard(board);
        ticTacToeGUI.setCurrentPlayer(currentPlayer.opponentIcon());
    }

    @Override
    public void onLoss(Map<String, String> data) {
        ticTacToeGUI.updateBoard(board);
        ticTacToeGUI.endGame("You lost");
    }

    @Override
    public void onWin(Map<String, String> data) {
        ticTacToeGUI.updateBoard(board);
        ticTacToeGUI.endGame("You won!");
    }

    @Override
    public void onDraw(Map<String, String> data) {
        ticTacToeGUI.updateBoard(board);
        ticTacToeGUI.endGame("It's a draw");
    }
}
