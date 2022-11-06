package networking;

/**
 * Gets thrown when there is a server runtime error/exception.
 */
public class ServerRuntimeException extends RuntimeException {
    public ServerRuntimeException(Throwable cause) {
        super(cause);
    }

    public ServerRuntimeException(String message) {
        super(message);
    }
}