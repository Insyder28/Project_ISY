import games.Game;
import games.Othello;
import games.TicTacToe;

public class Main {
    public static void main(String[] args) {
        Game test = new TicTacToe();
        test.createBoard();
        test.showBoard();
    }
}