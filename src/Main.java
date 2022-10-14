public class Main {
    public static void main(String[] args) {
        Connection conn = new Connection();
        conn.connect("localhost", 7789);

        System.out.println(conn.command("login Erwin", false));
        System.out.println(conn.command("get gamelist", true));

        conn.onMatchEvent.addListener(System.out::println);

        //conn.disconnect();
    }
}