package networking;

import events.Event;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;


/**
 * Represents a connection with a game server.
 * @author Erwin Veenhoven
 */
public class GameSocket {
    private Socket socket;
    private ServerStreamReader serverStreamReader;
    private PrintWriter out;

    // Server events
    /**
     * Gets called when a match is found.
     */
    public final Event onMatchEvent = new Event();
    /**
     * Gets called when it is the players turn in a game.
     */
    public final Event onYourTurnEvent = new Event();
    /**
     * Gets called when receiving result of a move.
     */
    public final Event onMoveEvent = new Event();
    /**
     * Gets called when receiving a challenge (invite) of another player.
     */
    public final Event onChallengeEvent = new Event();
    /**
     * Gets called when a challenge (invite) of a player got cancelled. Can happen if a player:
     * <ul>
     *     <li>Accepted another challenge</li>
     *     <li>Started another match</li>
     *     <li>Lost connection</li>
     * </ul>
     */
    public final Event onChallengeCancelledEvent = new Event();
    /**
     * Gets called when player won current match.
     */
    public final Event onWinEvent = new Event();
    /**
     * Gets called when player lost current match.
     */
    public final Event onLossEvent = new Event();
    /**
     * Gets called when player has drawn current match.
     */
    public final Event onDrawEvent = new Event();

    // Methods
    //TODO: Handle other exceptions

    @SuppressWarnings("unused")
    public void connect(String hostName, int portNumber) throws FailedToConnectException {
        connect(hostName, portNumber, false);
    }

    public void connect(String hostName, int portNumber, boolean autoDisconnect) throws FailedToConnectException {
        try {
            socket = new Socket(hostName, portNumber);
            out = new PrintWriter(socket.getOutputStream(), true);
            serverStreamReader = new ServerStreamReader(socket.getInputStream(), this::handleSvrEvent, autoDisconnect);

            if (socket == null) throw new FailedToConnectException();
        }
        catch (IOException e) {
            throw new FailedToConnectException(e);
        }
    }

    /**
     * Checks if the {@link GameSocket} still connected to a server.
     * @return true when connected to a server
     */
    @SuppressWarnings({"unused", "BooleanMethodIsAlwaysInverted"})
    public boolean isConnected() {
        if (socket == null) return false;
        if (socket.isClosed()) return false;
        return socket.isConnected();
    }

    /**
     * Disconnects the {@link GameSocket} from the server.
     */
    @SuppressWarnings("unused")
    public void disconnect() {
        if (!isConnected()) return;

        out.println("disconnect");

        try {
            socket.close();
            serverStreamReader.close();
            out.close();
        } catch (IOException ignored) { }
    }

    /**
     * Logs player in on server.
     * @param playerName name of the player
     * @throws DuplicateNameException gets thrown when name is already in use by somebody else
     */
    @SuppressWarnings("unused")
    public void login(String playerName) throws DuplicateNameException {
        try {
            command("login " + playerName, false);
        }
        catch (ServerException e) {
            if (e.getMessage().equals("duplicate name exists")) throw new DuplicateNameException(e);
        }
    }

    /**
     * @return available games on the server
     */
    @SuppressWarnings("unused")
    public String[] getGameList() {
        String response = command("get gamelist", true);
        return toArray(response.split(" ", 3)[2]);
    }

    /**
     * @return currently logged in players on the server
     */
    @SuppressWarnings("unused")
    public String[] getPlayerList() {
        String response = command("get playerlist", true);
        return toArray(response.split(" ", 3)[2]);
    }

    /**
     * Sign up for playing a specific game with someone.
     * @param gameType the game to sign up for
     * @throws ServerException gets thrown when a server error occurs while trying to subscribe to a game
     */
    @SuppressWarnings("unused")
    public void subscribe(String gameType) throws ServerException {
        command("subscribe " + gameType, false);
    }

    /**
     * Submits a game move to the server.
     * @param pos the board position from top left to bottom right
     * @throws ServerException gets thrown when a server error occurs while trying to make a move
     */
    @SuppressWarnings("unused")
    public void move(int pos) throws ServerException {
        command("move " + pos, false);
    }

    /**
     * Create a challenge (invite) to play with another player.
     * @param playerName name of player to challenge
     * @param gameType the game to play
     * @throws ServerException gets thrown when a server error occurs while trying to challenge another player
     */
    @SuppressWarnings("unused")
    public void challenge(String playerName, String gameType) throws ServerException {
        String name;
        if (playerName.contains(" ")) name = '"' + playerName + '"';
        else name = playerName;

        command("challenge " + name + " " + gameType, false);
    }

    /**
     * Accept a challenge (invite) from a player.
     * @param challengeId ID of the challenge
     * @throws ServerException gets thrown when a server error occurs while trying to accept a challenge
     */
    @SuppressWarnings("unused")
    public void challengeAccept(int challengeId) throws ServerException {
        command("challenge accept " + challengeId, false);
    }

    /**
     * Forfeit the current match.
     * @throws ServerException gets thrown when a server error occurs while trying to forfeit
     */
    @SuppressWarnings("unused")
    public void forfeit() throws ServerException{
        command("forfeit", false);
    }

    /**
     * Send message to other players.
     * @param text text to send
     * @throws ServerException gets thrown when a server error occurs while trying to send a message
     */
    @SuppressWarnings("unused")
    public void message(String text) throws ServerException {
        if (text.contains(" "))
            command("message \"" + text + "\"", false);
        else
            command("message " + text, false);
    }


    private String[] toArray(String s) {
        return s.substring(1, s.length() - 2).replace("\"", "").split(", ");
    }

    private String command(String command, boolean returnsData) throws ServerException {
        checkConnection();

        MessageBuffer responseBuffer = new MessageBuffer();
        serverStreamReader.bufferNextResponse(responseBuffer, returnsData);
        out.println(command);

        responseBuffer.awaitMessage();

        String response = responseBuffer.getMessage();
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
    }

    /**
     * Gets thrown when {@link GameSocket} failed to connect.
     */
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
        public ServerException(String errorMessage) {
            super(errorMessage);
        }
    }

    /**
     * Gets thrown when there is a duplicate name.
     */
    public static class DuplicateNameException extends Exception {
        public  DuplicateNameException(Throwable cause) {
            super("duplicate name exists", cause);
        }
    }
}
