package events;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an event.
 */
public class Event {
    private final List<EventListener> listeners = new ArrayList<>();
    private final List<EventListener> toRemove = new ArrayList<>();

    private boolean canRemove = true;
    private boolean waitingForRemoval = false;

    /**
     * Adds an {@link EventListener} to the event.
     * @param listener the listener to add
     */
    @SuppressWarnings("unused")
    public void addListener(EventListener listener) {
        listeners.add(listener);
    }

    /**
     * Removes an {@link EventListener} from the event.
     * @param listener the listener to remove
     */
    @SuppressWarnings("unused")
    public void removeListener(EventListener listener) {
        if (canRemove) listeners.remove(listener);
        else {
            toRemove.add(listener);
            waitingForRemoval = true;
        }
    }

    /**
     * Calls the event.
     * @param args args that get passed to all the added {@link EventListener} objects.
     */
    public void call(String args) {
        canRemove = false;
        for (EventListener el : listeners) {
            new Thread(() -> el.onEvent(args)).start();
        }
        canRemove = true;

        if (waitingForRemoval)
            for (EventListener listener : toRemove)
                listeners.remove(listener);
    }

    /**
     * Calls the event.
     */
    @SuppressWarnings("unused")
    public void call() {
        call(null);
    }
}