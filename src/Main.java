public class Main {
    public static void main(String[] args) {
        Connection conn = new Connection();
        conn.connect("localhost", 7789);
        //conn.login("Erwin");

        System.out.println(conn.command("pik", true));

        conn.disconnect();
    }
}