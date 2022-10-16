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
    public final Event onMatchEvent = new Event();
    public final Event onYourTurnEvent = new Event();
    public final Event onMoveEvent = new Event();
    public final Event onChallengeEvent = new Event();
    public final Event onChallengeCancelledEvent = new Event();
    public final Event onWinEvent = new Event();
    public final Event onLossEvent = new Event();
    public final Event onDrawEvent = new Event();

    // Methods
    //TODO: Handle other exceptions
    public void connect(String hostName, int portNumber) throws FailedToConnectException {
        try {
            socket = new Socket(hostName, portNumber);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new InputStreamReader(socket.getInputStream());

            if (socket == null) throw new FailedToConnectException();

            networkHandler = new networkHandler(in, this::handleSvrEvent);
            new Thread(networkHandler).start();

        } catch (IOException e) {
            throw new FailedToConnectException(e);
        }
    }

    public boolean isConnected() {
        if (socket == null) return false;
        else return socket.isConnected();
    }

    public void disconnect() {
        checkConnection();

        out.println("disconnect");

        try {
            socket.close();
            in.close();
            out.close();
        } catch (IOException ignored) { }
    }

    public void login(String playerName) throws DuplicateNameException {
        try {
            command("login " + playerName, false);
        }
        catch (ServerException e) {
            if (e.getMessage().equals("duplicate name exists")) throw new DuplicateNameException(e);
        }
    }

    public String[] getGameList() {
        String response = command("get gamelist", true);
        return toArray(response.split(" ", 3)[2]);
    }

    public String[] getPlayerList() {
        String response = command("get playerlist", true);
        return toArray(response.split(" ", 3)[2]);
    }

    public void subscribe(String gameType) throws ServerException {
        command("subscribe " + gameType, false);
    }

    public void move(int index) throws ServerException {
        command("move " + index, false);
    }

    public void challenge(String playerName, String gameType) throws ServerException {
        command("challenge " + playerName + " " + gameType, false);
    }

    public void challengeAccept(int challengeId) throws ServerException {
        command("challenge accept " + challengeId, false);
    }

    public void forfeit() throws ServerException{
        command("forfeit", false);
    }

    public void message(String s) throws ServerException {
        if (s.contains(" "))
            command("message \"" + s + "\"", false);
        else
            command("message " + s, false);
    }


    private String[] toArray(String s) {
        return s.substring(1, s.length() - 2).replace("\"", "").split(", ");
    }

    private String command(String command, boolean returnsData) throws ServerException {
        checkConnection();

        StringBuffer serverResponseBuffer = new StringBuffer();
        networkHandler.bufferNextMessage(serverResponseBuffer, returnsData);
        out.println(command);

        synchronized (serverResponseBuffer) {
            try {
                serverResponseBuffer.wait();
            } catch (InterruptedException ignored) { }
        }

        String response = serverResponseBuffer.toString();
        if (response.startsWith("ERR ")) throw new ServerException(response.replace("ERR ", ""));
        return response;
    }

    private void handleSvrEvent(String message) {
        String[] a = message.split(" ", 2);
        String type = a[0];
        String args = a[1];

        switch (type) {
            case "MATCH": onMatchEvent.call(args);
            case "YOURTURN": onYourTurnEvent.call(args);
            case "MOVE": onMoveEvent.call(args);
            case "WIN": onWinEvent.call(args);
            case "LOSS": onLossEvent.call(args);
            case "DRAW": onDrawEvent.call(args);
            case "CHALLENGE":
                if (args.startsWith("CANCELLED")) onChallengeCancelledEvent.call(args.split(" ", 2)[1]);
                else onChallengeEvent.call(args);
        }
    }

    // Error & Exception stuff
    private void checkConnection() throws NotConnectedException {
        if (!isConnected()) throw new NotConnectedException();
    }

    /**
     * Gets thrown when there is no connection when a connection is needed.
     */
    public static class NotConnectedException extends RuntimeException {
        public NotConnectedException() {
            super("Not connected to a server.");
        }
        public NotConnectedException(String errorMessage) {
            super(errorMessage);
        }
    }

    public static class FailedToConnectException extends Exception {
        public FailedToConnectException() {
            super("Can't connect to the server.");
        }
        public FailedToConnectException(Throwable cause) {
            super("Can't connect to the server.", cause);
        }
    }

    /**
     * Gets thrown when there is a server error/exception.
     */
    public static class ServerException extends RuntimeException {
        public ServerException() {
            super();
        }
        public ServerException(String errorMessage) {
            super(errorMessage);
        }
    }

    public static class DuplicateNameException extends Exception {
        public  DuplicateNameException(Throwable cause) {
            super("duplicate name exists", cause);
        }
    }
}

class networkHandler implements Runnable {
    private final InputStreamReader in;
    private final EventListener svrEventListener;

    private StringBuffer messageBuffer;
    private boolean bufferMessage;
    private boolean isDataMessage;
    private boolean receivedOk;

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
        this.receivedOk = false;
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
                    receivedOk = true;
            }
            else if (message.startsWith("OK") || message.startsWith("ERR") || (message.startsWith("SVR") && receivedOk)) {
                bufferMessage = false;

                synchronized (messageBuffer) {
                    messageBuffer.append(message);
                    messageBuffer.notify();
                }
            }
        }
        else if (message.startsWith("SVR GAME ")) {
            String event = message.replace("SVR ", "");
            svrEventListener.onEvent(event);
        }
    }
}
