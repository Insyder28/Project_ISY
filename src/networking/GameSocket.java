package networking;

import threading.MessageBuffer;
import events.Event;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;


/**
 * Represents a connection with a game server.
 * @author Erwin Veenhoven
 */
public class GameSocket implements Closeable {
    private final Socket socket;
    private final ServerStreamReader serverStreamReader;
    private final PrintWriter out;

    private int serverTimeOutDelay = 1000;
    private boolean loggedIn = false;
    private String playerName;

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

    // Constructors

    /**
     * Creates a new {@link GameSocket} object and connects to a server.
     * @param hostName the host name, or null for the loopback address.
     * @param portNumber the port number.
     * @throws IOException if an I/O error occurs when creating the object.
     */
    @SuppressWarnings("unused")
    public GameSocket(String hostName, int portNumber) throws IOException {
        socket = new Socket(hostName, portNumber);
        out = new PrintWriter(socket.getOutputStream(), true);
        serverStreamReader = new ServerStreamReader(socket.getInputStream(), this::handleSvrEvent);
    }

    // Getters and Setters

    /**
     * <p>Setter for ServerTimeOutDelay.</p>
     * <p>Note: When the delay is 0 it will never time out.</p>
     * @param serverTimeOutDelay the delay until server times out after command.
     * @see #getServerTimeOutDelay()
     */
    @SuppressWarnings("unused")
    public void setServerTimeOutDelay(int serverTimeOutDelay) {
        this.serverTimeOutDelay = serverTimeOutDelay;
    }

    /**
     * Getter for serverTimeOutDelay.
     * @return the current serverTimeOutDelay.
     * @see #setServerTimeOutDelay(int)
     */
    @SuppressWarnings("unused")
    public int getServerTimeOutDelay() {
        return serverTimeOutDelay;
    }

    /**
     * Returns login status of player.
     * @return true if player is logged in.
     */
    @SuppressWarnings("unused")
    public boolean isLoggedIn() {
        return loggedIn;
    }

    /**
     * Returns the Name with witch player has logged in.
     * @return Name with witch player has logged in.
     */
    public String getPlayerName() {
        return playerName;
    }

    // Methods

    /**
     * Disconnects the {@link GameSocket} from the server.
     */
    @SuppressWarnings("unused")
    public void close() {
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
     * @throws ServerException if a server exception/error occurs.
     * @throws ServerTimedOutException if the server times out.
     * @throws DuplicateNameException if name is already in use by somebody else.
     */
    @SuppressWarnings("unused")
    public void login(String playerName) throws ServerException {
        try {
            command("login " + playerName, false);
            this.playerName = playerName;
            loggedIn = true;
        }
        catch (ServerException e) {
            if (e.getMessage().equals("duplicate name exists")) throw new DuplicateNameException(e);
            else throw e;
        }
    }

    /**
     * Retrieves all available games on the server.
     * @return available games on the server
     * @throws ServerTimedOutException if the server times out.
     */
    @SuppressWarnings("unused")
    public String[] getGameList() throws ServerTimedOutException {
        try {
            String response = command("get gamelist", true);
            return toArray(response.split(" ", 3)[2]);
        }
        catch (ServerTimedOutException e) {
            throw e;
        }
        catch (ServerException e) {
            throw new ServerRuntimeException(e);
        }
    }

    /**
     * Retrieves all currently logged in players on the server.
     * @return currently logged in players on the server.
     * @throws ServerTimedOutException if the server times out.
     */
    @SuppressWarnings("unused")
    public String[] getPlayerList() throws ServerTimedOutException {
        try {
            String response = command("get playerlist", true);
            return toArray(response.split(" ", 3)[2]);
        }
        catch (ServerTimedOutException e) {
            throw e;
        }
        catch (ServerException e) {
            throw new ServerRuntimeException(e);
        }
    }

    /**
     * Sign up for playing a specific game with someone.
     * @param gameType the game to sign up for
     * @throws ServerTimedOutException if the server times out.
     */
    @SuppressWarnings("unused")
    public void subscribe(String gameType) throws ServerTimedOutException {
        try {
            command("subscribe " + gameType, false);
        }
        catch (ServerTimedOutException e) {
            throw e;
        }
        catch (ServerException e) {
            throw new ServerRuntimeException(e);
        }
    }

    /**
     * Submits a game move to the server.
     * @param pos the board position from top left to bottom right
     * @throws ServerTimedOutException if the server times out.
     */
    @SuppressWarnings("unused")
    public void move(int pos) throws ServerTimedOutException {
        try {
            command("move " + pos, false);
        }
        catch (ServerTimedOutException e) {
            throw e;
        }
        catch (ServerException e) {
            throw new ServerRuntimeException(e);
        }
    }

    /**
     * Create a challenge (invite) to play with another player.
     * @param playerName name of player to challenge
     * @param gameType the game to play
     * @throws ServerTimedOutException if the server times out.
     */
    @SuppressWarnings("unused")
    public void challenge(String playerName, String gameType) throws ServerTimedOutException {
        try {
            command("challenge " + playerName + " " + gameType, false);
        }
        catch (ServerTimedOutException e) {
            throw e;
        }
        catch (ServerException e) {
            throw new ServerRuntimeException(e);
        }
    }

    /**
     * Accept a challenge (invite) from a player.
     * @param challengeId ID of the challenge
     * @throws ServerTimedOutException if the server times out.
     */
    @SuppressWarnings("unused")
    public void challengeAccept(int challengeId) throws ServerTimedOutException {
        try {
            command("challenge accept " + challengeId, false);
        }
        catch (ServerTimedOutException e) {
            throw e;
        }
        catch (ServerException e) {
            throw new ServerRuntimeException(e);
        }
    }

    /**
     * Forfeit the current match.
     * @throws ServerTimedOutException if the server times out.
     */
    @SuppressWarnings("unused")
    public void forfeit() throws ServerTimedOutException {
        try {
            command("forfeit", false);
        }
        catch (ServerTimedOutException e) {
            throw e;
        }
        catch (ServerException e) {
            throw new ServerRuntimeException(e);
        }
    }

    /**
     * Send message to other players.
     * @param text text to send
     * @throws ServerException gets thrown when a server error occurs while trying to send a message
     * @throws ServerTimedOutException if the server times out.
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
        MessageBuffer<String> responseBuffer = new MessageBuffer<>();
        serverStreamReader.bufferNextResponse(responseBuffer, returnsData);
        out.println(command);

        String response;
        try {
            response = responseBuffer.awaitMessage(serverTimeOutDelay);
        }
        catch (MessageBuffer.TimedOutException e) {
            throw new ServerTimedOutException(e);
        }

        if (response.startsWith("ERR ")) throw new ServerException(response.replace("ERR ", ""));
        return response;
    }

    private void handleSvrEvent(String message) {
        String[] a = message.split(" ", 3);
        String type = a[1];
        String args = a[2];

        switch (type) {
            case "MATCH" -> onMatchEvent.call(args);
            case "YOURTURN" -> onYourTurnEvent.call(args);
            case "MOVE" -> onMoveEvent.call(args);
            case "WIN" -> onWinEvent.call(args);
            case "LOSS" -> onLossEvent.call(args);
            case "DRAW" -> onDrawEvent.call(args);
            case "CHALLENGE" -> {
                if (args.startsWith("CANCELLED")) onChallengeCancelledEvent.call(args.split(" ", 2)[1]);
                else onChallengeEvent.call(args);
            }
        }
    }
}
