package games;

import gui.GUI;
import networking.GameSocket;
import networking.ServerTimedOutException;
import players.Player;

import java.util.Map;

public class TicTacToeOnline implements OnlineGame {
    private final Board board = new Board(3, 3);
    private final GameSocket gameSocket;
    private final Player player;
    private final GUI gui;

    public TicTacToeOnline(Player player, GameSocket gameSocket, GUI gui) {
        this.player = player;
        this.gameSocket = gameSocket;
        this.gui = gui;
    }

    @Override
    public void onMatch(Map<String, String> data) {
        System.out.println("kinker");

        if (data.get("PLAYERTOMOVE").equals(gameSocket.getPlayerName()))
            player.setIcon(Icon.CROSS);
        else
            player.setIcon(Icon.NOUGHT);

        gui.setCurrentPlayer(Icon.CROSS);
        gui.updateBoard(board);
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

        gui.updateBoard(board);
        gui.setCurrentPlayer(currentPlayer.opponentIcon());
    }

    @Override
    public void onLoss(Map<String, String> data) {
        gui.updateBoard(board);
        gui.endGame("You lost");
    }

    @Override
    public void onWin(Map<String, String> data) {
        gui.updateBoard(board);
        gui.endGame("You won!");
    }

    @Override
    public void onDraw(Map<String, String> data) {
        gui.updateBoard(board);
        gui.endGame("It's a draw");
    }
}
