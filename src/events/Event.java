package events;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an event.
 */
public class Event {
    private final List<EventListener> listeners = new ArrayList<>();

    /**
     * Adds an {@link EventListener} to the event.
     * @param toAdd the listener to add
     */
    @SuppressWarnings("unused")
    public void addListener(EventListener toAdd) {
        listeners.add(toAdd);
    }

    /**
     * Calls the event.
     * @param args args that get passed to all the added {@link EventListener} objects.
     */
    public void call(String args) {
        for (EventListener el : listeners)
            el.onEvent(args);
    }

    /**
     * Calls the event.
     */
    @SuppressWarnings("unused")
    public void call() {
        call(null);
    }
}