public class Main {
    public static void main(String[] args) {
        Connection conn = new Connection();

        try { conn.connect("localhost", 7789); } catch (Exception ignored) { }
        try { conn.login("Erwin"); } catch (Exception ignored) { }

        conn.getGameList();

        conn.disconnect();
    }
}