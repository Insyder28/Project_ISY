import java.util.ArrayList;
import java.util.List;

public class ServerEvent {
    private List<ServerEventListener> listeners = new ArrayList<ServerEventListener>();

    public void addListener(ServerEventListener toAdd) {
        listeners.add(toAdd);
    }

    public void call(String[] args) {
        for (ServerEventListener el : listeners)
            el.onEvent(args);
    }

    public void call() {
        call(null);
    }
}