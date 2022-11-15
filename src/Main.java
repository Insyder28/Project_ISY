import games.Icon;
import games.TicTacToe;
import gui.GUI;
import players.AIPlayer;
import players.HumanPlayer;
import players.Player;
import players.PlayerType;

public class Main {
    private static final GUI gui = new GUI();

    public static void main(String[] args) {
        switch (gui.selectMode()) {
            case LOCAL -> selectedLocal();
            case ONLINE -> selectedOnline();
        }
    }

    // Local
    private static void selectedLocal() {
        switch (gui.selectGameType()) {
            case TICTACTOE -> startLocalTicTacToe();
            case OTHELLO -> startLocalOthello();
        }
    }

    private static void startLocalTicTacToe() {
        Player xPlayer = gui.selectPlayerType(Icon.CROSS) == PlayerType.HUMAN ? new HumanPlayer() : new AIPlayer();
        Player oPlayer = gui.selectPlayerType(Icon.NOUGHT) == PlayerType.HUMAN ? new HumanPlayer() : new AIPlayer();
        new TicTacToe().startGame(xPlayer, oPlayer);
    }

    private static void startLocalOthello() {

    }

    // Online
    private static void selectedOnline() {

    }
}