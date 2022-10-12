import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Connection {
    private Socket socket;
    private InputStreamReader in;
    private PrintWriter out;

    // Server events
    public final ServerEvent svrHelpEvent = new ServerEvent();
    public final ServerEvent svrGameEvent = new ServerEvent();
    public final ServerEvent svrMatchEvent = new ServerEvent();
    public final ServerEvent svrYourTurnEvent = new ServerEvent();
    public final ServerEvent svrMoveEvent = new ServerEvent();
    public final ServerEvent svrChallengeEvent = new ServerEvent();
    public final ServerEvent svrWinEvent = new ServerEvent();
    public final ServerEvent svrLossEvent = new ServerEvent();
    public final ServerEvent svrDrawEvent = new ServerEvent();


    // Methods
    public void connect(String hostName, int portNumber) {
        try {
            socket = new Socket(hostName, portNumber);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new InputStreamReader(socket.getInputStream());

            if (socket == null) return;

            ServerListener serverListener = new ServerListener(in);
            Thread serverListenerThread = new Thread(serverListener);
            serverListenerThread.start();

        } catch (IOException ignored) { }
    }

    //TODO: remove after testing
    public void testCommand(String command) {
        out.println(command);
    }

    public void disconnect() {
        out.println("disconnect");

        try {
            socket.close();
            in.close();
            out.close();
        } catch (IOException ignored) { }
    }

    public void login(String playerName) {
        out.println("login " + playerName);
    }


    class ServerListener implements Runnable {
        private final InputStreamReader in;

        // Constructors
        ServerListener(InputStreamReader in) {
            this.in = in;
        }

        // Methods
        @Override
        public void run() {
            StringBuilder sb = new StringBuilder();

            int data;

            while (true) {
                try { data = in.read(); }
                catch (IOException e) {
                    break;
                }

                if (data == -1) break;

                char c = (char)data;
                if (c != '\n')
                    sb.append(c);
                else {
                    String message = sb.toString();
                    handleSvrMessage(message);
                    sb.setLength(0);
                }
            }
        }

        private void handleSvrMessage(String message) {
            if (message.startsWith("SVR ")) {
                String[] event = message.replace("SVR ", "").split(" ", 2);
                String[] args = event[1].split(" ");

                switch (event[0]) {
                    case "HELP": svrHelpEvent.call(args);
                    case "GAME": svrGameEvent.call(args);
                    case "MATCH": svrMatchEvent.call(args);
                    case "YOURTURN": svrYourTurnEvent.call(args);
                    case "MOVE": svrMoveEvent.call(args);
                    case "CHALLENGE": svrChallengeEvent.call(args);
                    case "WIN": svrWinEvent.call(args);
                    case "LOSS": svrLossEvent.call(args);
                    case "DRAW": svrDrawEvent.call(args);
                }
            }
        }
    }
}
