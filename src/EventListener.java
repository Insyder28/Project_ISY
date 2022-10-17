/**
 * An interface that represents an event listener.
 */
public interface EventListener {
    /**
     * Gets called when event gets called.
     * @param args args that get passed to listener.
     */
    void onEvent(String args);
}
