import games.TicTacToeOnline;
import networking.GameSocket;
import players.AIPlayer;
import players.HumanPlayer;
import players.Player;

public class Main {
    public static void main(String[] args) {
        String ip = args[0];
        int port = Integer.parseInt(args[1]);
        String playerName = args[2];
        String playerType = args[3];


        GameSocket gameSocket;

        try {
            gameSocket = new GameSocket(ip, port);
            gameSocket.login(playerName);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

        TicTacToeOnline ticTacToeOnline = new TicTacToeOnline();

        Player player = playerType.equals("AI") ? new AIPlayer() : new HumanPlayer();

        ticTacToeOnline.startGame(player, gameSocket);

        System.out.println("\nDisconnecting...");
        gameSocket.close();
    }
}