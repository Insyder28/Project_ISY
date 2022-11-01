import games.TicTacToe;
import players.HumanPlayer;

public class Main {
    public static void main(String[] args) {
        TicTacToe ticTacToe = new TicTacToe();

        ticTacToe.startLocalGame(new HumanPlayer(), new HumanPlayer());
    }
}