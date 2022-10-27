import games.TicTacToe;
import players.HumanPlayer;

public class Main {
    public static void main(String[] args) {
        TicTacToe ticTacToe = new TicTacToe(new HumanPlayer(), new HumanPlayer());

        ticTacToe.startGame();
    }
}