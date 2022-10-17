import networking.GameSocket;

public class Main {
    public static void main(String[] args) {
        GameSocket gameSocket = new GameSocket();

        try {
            gameSocket.connect("localhost", 7789, true);
            gameSocket.login("Erwin");
        }
        catch (Exception ignored) { }

        String[] games = gameSocket.getGameList();
        for (var game : games) System.out.println(game);
    }
}