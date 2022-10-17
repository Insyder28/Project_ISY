package networking;

/**
 * A thread save buffer for messages.
 */
public class MessageBuffer {
    String message;

    /**
     * Set a message and call {@link #notify()} on object in a thread safe manner.
     * @param message the message
     */
    synchronized public void setMessage(String message) {
        this.message = message;
        this.notify();
    }

    /**
     * @return current message stored in buffer.
     */
    synchronized public String getMessage() {
        return message;
    }

    /**
     * calls {@link #wait()} on object in a thread safe manner.
     * @see #setMessage(String)
     */
    synchronized public void awaitMessage() {
        try { this.wait(); }
        catch (InterruptedException ignored) { }
    }
}
