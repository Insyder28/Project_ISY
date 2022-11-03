package networking;

/**
 * Gets thrown when there is a duplicate name.
 */
public class DuplicateNameException extends ServerException {
    public  DuplicateNameException(Throwable cause) {
        super("duplicate name exists", cause);
    }
}