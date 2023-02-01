package games.TicTacToe;

import games.data.Board;
import games.data.Icon;
import networking.GameSocket;
import networking.ServerException;
import players.*;
import util.Mapper;

import java.util.Map;

public class TicTacToeOnline {
    private final Board board = new Board(3, 3);

    private final GameSocket gameSocket;

    private final String opponentName;
    private final String playerName;

    private final Player player;

    public TicTacToeOnline(GameSocket gameSocket, String playerToMove, String opponentName, String playerName, PlayerType playerType) {
        this.gameSocket = gameSocket;

        this.opponentName = opponentName;
        this.playerName = playerName;

        // initialise player
        switch (playerType) {
            case AI -> this.player = new AIPlayer();
            case HUMAN -> this.player = new HumanPlayer();
            case CONSOLE -> this.player = new HumanConsolePlayer();
            default -> this.player = new RandomPlayer();
        }

        // give player the right icon
        player.setIcon(playerToMove.equals(opponentName) ? Icon.NOUGHT : Icon.CROSS);
    }

    public void onYourTurn(String info) {
        int move = player.move(board);
        try { gameSocket.move(move); }
        catch (ServerException e) { }
    }

    public void onMove(String info) {
        Map<String, String> data = Mapper.messageToMap(info);

        Icon icon = data.get("PLAYER").equals(opponentName) ? player.getIcon().opponentIcon() : player.getIcon();

        board.set(Integer.parseInt(data.get("MOVE")), icon);

        System.out.println(board);
    }

    public void onWin(String info) {
        System.out.println("win");
    }

    public void onLoss(String info) {
        System.out.println("loss");
    }

    public void onDraw(String info) {
        System.out.println("draw");
    }
}
