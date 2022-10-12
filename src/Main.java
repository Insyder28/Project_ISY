public class Main {
    public static void main(String[] args) {
        //System.out.println("Hello world!");

        Connection conn = new Connection();
        conn.connect("localhost", 7789);
        conn.login("Erwin");
        conn.login("Erwin");

    }
}