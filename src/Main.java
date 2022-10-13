public class Main {
    public static void main(String[] args) {
        Connection conn = new Connection();
        conn.connect("localhost", 7789);
        //conn.login("Erwin");

        conn.command("get gamelist", true);

        conn.disconnect();
    }
}