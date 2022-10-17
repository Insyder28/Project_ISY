public class MessageBuffer {
    String message;

    synchronized public void setMessage(String message) {
        this.message = message;
        this.notify();
    }

    synchronized public String getMessage() {
        return message;
    }

    synchronized public void awaitMessage() {
        try { this.wait(); } catch (InterruptedException ignored) { }
    }
}
