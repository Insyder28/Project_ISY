import java.util.ArrayList;
import java.util.List;

public class Event {
    private List<EventListener> listeners = new ArrayList<EventListener>();

    public void addListener(EventListener toAdd) {
        listeners.add(toAdd);
    }

    public void call(String args) {
        for (EventListener el : listeners)
            el.onEvent(args);
    }

    public void call() {
        call(null);
    }
}