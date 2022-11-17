package util;

//TODO: put wait on private object

/**
 * A thread save buffer.
 */
public class Buffer<T> {
    T object;
    private volatile boolean isSet = false;
    private volatile boolean interrupted = false;

    /**
     * Set a message and call {@link #notify()} on object in a thread safe manner.
     * @param object the message
     */
    synchronized public void set(T object) {
        this.object = object;
        this.isSet = true;
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
    synchronized public T await() throws InterruptedException {
        try { this.wait(); }
        catch (InterruptedException ignored) { }

        if (interrupted) throw new InterruptedException("Interrupted await");

        return object;
    }

    /**
     * Calls {@link #wait()} on object in a thread safe manner.
     * @param timeOutDelay the time after witch the waiting times out.
     * @throws TimedOutException Gets thrown when waiting longer than the timeOutDelay.
     */
    synchronized public T await(int timeOutDelay) throws TimedOutException, InterruptedException {
        if (timeOutDelay <= 0) {
            return await();
        }

        this.isSet = false;

        try { this.wait(timeOutDelay); }
        catch (InterruptedException ignored) { }

        if (!isSet) throw new TimedOutException();
        if (interrupted) throw new InterruptedException("Interrupted await");
        return object;
    }

    /**
     * Interrupts await.
     */
    synchronized public void interrupt() {
        this.interrupted = true;
        this.notify();
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
