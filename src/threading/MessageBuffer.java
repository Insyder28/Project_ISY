package threading;

/**
 * A thread save buffer for messages.
 */
public class MessageBuffer<T> {
    T message;
    private volatile boolean notified = false;

    /**
     * Set a message and call {@link #notify()} on object in a thread safe manner.
     * @param message the message
     */
    synchronized public void setMessage(T message) {
        this.message = message;
        this.notified = true;
        this.notify();
    }

    /**
     * @return current message stored in buffer.
     */
    @SuppressWarnings("unused")
    synchronized public T getMessage() {
        return message;
    }

    /**
     * Calls {@link #wait()} on object in a thread safe manner.
     * @see #setMessage(T)
     */
    synchronized public T awaitMessage() {
        try { this.wait(); }
        catch (InterruptedException ignored) { }
        return message;
    }

    /**
     * Calls {@link #wait()} on object in a thread safe manner.
     * @param timeOutDelay the time after witch the waiting times out.
     * @throws TimedOutException Gets thrown when waiting longer than the timeOutDelay.
     */
    synchronized public T awaitMessage(int timeOutDelay) throws TimedOutException{
        if (timeOutDelay <= 0) {
            return awaitMessage();
        }

        this.notified = false;

        try { this.wait(timeOutDelay); }
        catch (InterruptedException ignored) { }

        if (!notified) throw new TimedOutException();
        return message;
    }

    /**
     * Gets thrown when a response times out.
     */
    public static class TimedOutException extends Exception {
        public TimedOutException() {
            super("awaitMessage timed out");
        }
    }
}
