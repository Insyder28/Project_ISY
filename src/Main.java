import networking.GameSocket;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        GameSocket gameSocket;

        try { gameSocket = new GameSocket("localhost", 7789); }
        catch (IOException e) { throw new RuntimeException(e); }

        String[] games = gameSocket.getGameList();
        for (var game : games) System.out.println(game);

        gameSocket.close();
    }
}