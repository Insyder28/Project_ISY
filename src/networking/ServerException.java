package networking;

/**
 * Gets thrown when there is a server error/exception.
 */
public class ServerException extends Exception {
    public ServerException(String errorMessage) {
        super(errorMessage);
    }
    public ServerException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }
}
