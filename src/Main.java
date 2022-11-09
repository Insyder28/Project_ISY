import games.TicTacToe;
import gui.GUI;
import networking.GameSocket;
import networking.MultiplayerHandler;
import players.HumanPlayer;
import players.PlayerType;

public class Main {
    public static void main(String[] args) {
        String ip = args[0];
        int port = Integer.parseInt(args[1]);
        String playerName = args[2];

        GameSocket gameSocket;

        try {
            gameSocket = new GameSocket(ip, port);
            System.out.println("connected to: " + ip + ":" + port);
            gameSocket.login(playerName);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

        new GUI();

        MultiplayerHandler multiplayerHandler = new MultiplayerHandler(gameSocket);
        multiplayerHandler.subscribe("tic-tac-toe");
    }
}