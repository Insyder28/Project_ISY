package gui;

public class ActionFailedException extends Exception {
    public ActionFailedException(Throwable cause) {
        super(cause);
    }

    public ActionFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
