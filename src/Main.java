import games.TicTacToeOnline;
import networking.GameSocket;
import players.AIPlayer;
import players.HumanPlayer;
import players.Player;

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

        Player player = args[3].equals("AI") ? new AIPlayer() : new HumanPlayer();

        ticTacToeOnline.startGame(player, gameSocket);

        System.out.println("\nDisconnecting...");
        gameSocket.close();
    }
}