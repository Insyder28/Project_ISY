package util;

import java.util.concurrent.atomic.AtomicBoolean;

public class Trigger {
    private final AtomicBoolean bool;

    public Trigger(boolean initialState) {
        bool = new AtomicBoolean(initialState);
    }

    public void await() {
        synchronized (bool) {
            while (!bool.get()) {
                try {
                    bool.wait();
                }
                catch (InterruptedException ignored) { }
            }
        }
    }

    public void set(boolean value) {
        synchronized (bool) {
            bool.set(value);
            if (value) bool.notify();
        }
    }
}
