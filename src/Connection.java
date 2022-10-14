import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Connection {
    private Socket socket;
    private InputStreamReader in;
    private networkHandler networkHandler;
    private PrintWriter out;

    // Server events
    public final Event onGameEvent = new Event();
    public final Event onMatchEvent = new Event();
    public final Event onYourTurnEvent = new Event();
    public final Event onMoveEvent = new Event();
    public final Event onChallengeEvent = new Event();
    public final Event onWinEvent = new Event();
    public final Event onLossEvent = new Event();
    public final Event onDrawEvent = new Event();

    // Methods
    public void connect(String hostName, int portNumber) {
        try {
            socket = new Socket(hostName, portNumber);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new InputStreamReader(socket.getInputStream());

            if (socket == null) return;

            networkHandler = new networkHandler(in, this::handleSvrEvent);
            new Thread(networkHandler).start();

        } catch (IOException ignored) { }
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

    public String command(String command, boolean returnsData) {
        StringBuffer serverResponseBuffer = new StringBuffer();
        networkHandler.bufferNextMessage(serverResponseBuffer, returnsData);
        out.println(command);

        synchronized (serverResponseBuffer) {
            try {
                serverResponseBuffer.wait();
            } catch (InterruptedException ignored) { }
        }

        return serverResponseBuffer.toString();
    }

    private void handleSvrEvent(String event) {
        String[] a = event.split(" ", 2);
        String type = a[0];
        String args = a[1];

        switch (type) {
            case "GAME": onGameEvent.call(args);
            case "MATCH": onMatchEvent.call(args);
            case "YOURTURN": onYourTurnEvent.call(args);
            case "MOVE": onMoveEvent.call(args);
            case "CHALLENGE": onChallengeEvent.call(args);
            case "WIN": onWinEvent.call(args);
            case "LOSS": onLossEvent.call(args);
            case "DRAW": onDrawEvent.call(args);
        }
    }
}

class networkHandler implements Runnable {
    private final InputStreamReader in;
    private final EventListener svrEventListener;

    private StringBuffer messageBuffer;
    private volatile boolean bufferMessage;
    private volatile boolean isDataMessage;

    // Constructor
    networkHandler(InputStreamReader in, EventListener svrEventListener) {
        this.in = in;
        this.svrEventListener = svrEventListener;
    }

    public void bufferNextMessage(StringBuffer buffer) {
        bufferNextMessage(buffer, false);
    }
    public void bufferNextMessage(StringBuffer buffer, boolean isDataMessage) {
        synchronized (buffer) {
            this.messageBuffer = buffer;
        }
        this.bufferMessage = true;
        this.isDataMessage = isDataMessage;
    }

    // Runnable method
    @Override
    public void run() {
        StringBuilder sb = new StringBuilder();
        int data;

        while (true) {
            try { data = in.read(); }        // Read one integer from data stream.
            catch (IOException e) { break; } // Exit loop when there is an IOException. (e.g. Data stream has been closed.)

            if (data == -1) break;           // Exit loop if end of data stream has been reached.

            char c = (char)data;             // Cast data (int) to char.

            if (c != '\n') {                 // Check if end of line character has been reached.
                sb.append(c);                // Append char to StringBuilder.
                continue;
            }

            // End of line has been reached
            String message = sb.toString();
            handleMessage(message);
            sb.setLength(0);
        }
    }

    private void handleMessage(String message) {
        if (bufferMessage) {
            if (isDataMessage) {
                if (message.startsWith("ERR")) {
                    isDataMessage = false;
                    bufferMessage = false;

                    synchronized (messageBuffer) {
                        messageBuffer.append(message);
                        messageBuffer.notify();
                    }
                }
                else if (message.startsWith("OK"))
                    isDataMessage = false;
            }
            else if (message.startsWith("OK") || message.startsWith("SVR") || message.startsWith("ERR")) {
                bufferMessage = false;

                synchronized (messageBuffer) {
                    messageBuffer.append(message);
                    messageBuffer.notify();
                }
            }
        }
        else if (message.startsWith("SVR ")) {
            String event = message.replace("SVR ", "");
            svrEventListener.onEvent(event);
        }
    }
}
