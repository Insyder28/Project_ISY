public class Main {
    public static void main(String[] args) {
        GameSocket conn = new GameSocket();

        try { conn.connect("localhost", 7789, true); } catch (Exception ignored) { }
        try { conn.login("Erwin"); } catch (Exception ignored) { }

        String[] games = conn.getGameList();

        for (var game : games) System.out.println(game);

        conn.disconnect();
    }
}