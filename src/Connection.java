import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Connection {
    private Socket socket;
    private InputStreamReader in;
    private PrintWriter out;

    private Thread serverListenerThread;


    public void connect(String hostName, int portNumber) {

        try {
            socket = new Socket(hostName, portNumber);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new InputStreamReader(socket.getInputStream());

            if (socket == null) return;

            ServerListener serverListener = new ServerListener(in);
            serverListenerThread = new Thread(serverListener);
            serverListenerThread.start();

        } catch (IOException e) { }
    }

    public void disconnect() {
        serverListenerThread.stop();
    }

    public void login(String playerName) {
        out.println("login " + playerName);
    }
}

class ServerListener implements Runnable {
    private InputStreamReader in;

    ServerListener(InputStreamReader in) {
        this.in = in;
    }

    @Override
    public void run() {
        StringBuilder sb = new StringBuilder();

        while (true) {
            char c = ' ';
            try {
                c = (char)in.read();
            } catch (IOException e) { }

            if (c != '\n')
                sb.append(c);
            else {
                String response = sb.toString();
                System.out.println("Server: " + response);
                sb.setLength(0);
            }
        }
    }
}
