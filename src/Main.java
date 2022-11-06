import games.TicTacToe;
import games.TicTacToeOnline;
import networking.GameSocket;
import networking.ServerException;
import players.HumanPlayer;

public class Main {
    public static void main(String[] args) {
        GameSocket gameSocket;

        try {
            gameSocket = new GameSocket(args[0], Integer.parseInt(args[1]));
            gameSocket.login(args[2]);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

        TicTacToeOnline ticTacToeOnline = new TicTacToeOnline();
        ticTacToeOnline.startGame(new HumanPlayer(), gameSocket);

        System.out.println("\nDisconnecting...");
        gameSocket.close();
    }
}