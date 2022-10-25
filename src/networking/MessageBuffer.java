package networking;

/**
 * A thread save buffer for messages.
 */
public class MessageBuffer {
    String message;
    private volatile boolean interruptedWait = false;

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
     * Calls {@link #wait()} on object in a thread safe manner.
     * @see #setMessage(String)
     */
    synchronized public void awaitMessage() {
        try { this.wait(); }
        catch (InterruptedException ignored) { }
    }

    /**
     * Calls {@link #wait()} on object in a thread safe manner.
     * @param timeOutDelay the time after witch the waiting times out.
     * @throws TimedOutException Gets thrown when waiting longer than the timeOutDelay.
     */
    synchronized public void awaitMessage(int timeOutDelay) throws TimedOutException{
        if (timeOutDelay <= 0) {
            awaitMessage();
            return;
        }

        try {
            Thread timer = new Thread(() -> {
                try {
                    Thread.sleep(timeOutDelay);
                    this.interruptedWait = true;
                    synchronized (this) {
                        this.notify();
                    }
                }
                catch (InterruptedException ignored) { }
            });

            timer.start();
            this.wait();
            if (interruptedWait) throw new TimedOutException();
            else timer.interrupt();
        }
        catch (InterruptedException ignored) { }
    }

    public static class TimedOutException extends Exception {
        public TimedOutException() {
            super("awaitMessage timed out");
        }
    }
}
