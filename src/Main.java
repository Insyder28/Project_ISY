import gui.GUI;
import networking.GameSocket;
import networking.MultiplayerHandler;
import players.PlayerType;

public class Main {
    public static void main(String[] args) {
        String ip = args[0];
        int port = Integer.parseInt(args[1]);
        String playerName = args[2];
        String playerType = args[3];

        GameSocket gameSocket;

        try {
            gameSocket = new GameSocket(ip, port);
            System.out.println("connected to: " + ip + ":" + port);
            gameSocket.login(playerName);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

        GUI gui = new GUI();

        MultiplayerHandler multiplayerHandler = new MultiplayerHandler(gameSocket, PlayerType.HUMAN, gui);
        multiplayerHandler.subscribe("tic-tac-toe");
    }
}