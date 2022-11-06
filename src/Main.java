import games.TicTacToeOnline;
import networking.GameSocket;
import players.HumanPlayer;

public class Main {
    public static void main(String[] args) {
        GameSocket gameSocket;

        try {
            gameSocket = new GameSocket("localhost", 7789);
            gameSocket.login(args[0]);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

        TicTacToeOnline ticTacToeOnline = new TicTacToeOnline();
        ticTacToeOnline.startGame(new HumanPlayer(), gameSocket);

        System.out.println("closing");
        gameSocket.close();
    }
}