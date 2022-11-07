package networking;

/**
 * Gets thrown when the server times out. (e.g. Server reaction to command took too long.)
 */
public class ServerTimedOutException extends ServerException {
    public ServerTimedOutException(Throwable cause) {
        super("server timed out", cause);
    }
}
