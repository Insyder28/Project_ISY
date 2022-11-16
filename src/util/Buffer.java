package util;

/**
 * A thread save buffer.
 */
public class Buffer<T> {
    T object;
    private volatile boolean notified = false;

    /**
     * Set a message and call {@link #notify()} on object in a thread safe manner.
     * @param object the message
     */
    synchronized public void set(T object) {
        this.object = object;
        this.notified = true;
        this.notify();
    }

    /**
     * @return current object stored in buffer.
     */
    @SuppressWarnings("unused")
    synchronized public T get() {
        return object;
    }

    //TODO: put wait on private object.

    /**
     * Calls {@link #wait()} on object in a thread safe manner.
     * @see #set(T)
     */
    synchronized public T await() {
        try { this.wait(); }
        catch (InterruptedException ignored) { }
        return object;
    }

    /**
     * Calls {@link #wait()} on object in a thread safe manner.
     * @param timeOutDelay the time after witch the waiting times out.
     * @throws TimedOutException Gets thrown when waiting longer than the timeOutDelay.
     */
    synchronized public T await(int timeOutDelay) throws TimedOutException{
        if (timeOutDelay <= 0) {
            return await();
        }

        this.notified = false;

        try { this.wait(timeOutDelay); }
        catch (InterruptedException ignored) { }

        if (!notified) throw new TimedOutException();
        return object;
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
