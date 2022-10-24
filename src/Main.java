import networking.GameSocket;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            GameSocket gs = new GameSocket("localhost", 7789);

            gs.close();
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }
}