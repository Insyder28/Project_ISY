import networking.GameSocket;

public class Main {
    public static void main(String[] args) {
        try {
            GameSocket gs = new GameSocket("localhost", 7789, true);
            String[] games = gs.getGameList();

            for(var game : games) System.out.println(game);
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
}