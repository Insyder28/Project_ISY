import java.io.IOException;
import java.io.InputStream;

/**
 * Wrapper for {@link java.io.InputStream} for handling server input.
 * @author Erwin Veenhoven
 */
class ServerStreamReader implements Runnable {
    private final InputStream in;
    private final EventListener svrEventListener;

    private MessageBuffer messageBuffer;
    private boolean bufferMessage;
    private boolean isDataMessage;
    private boolean receivedOk;

    // Constructor
    @SuppressWarnings("unused")
    ServerStreamReader(InputStream in, EventListener svrEventListener) {
        this.in = in;
        this.svrEventListener = svrEventListener;
    }

    @SuppressWarnings("unused")
    public void bufferNextMessage(MessageBuffer buffer) {
        bufferNextMessage(buffer, false);
    }

    public void bufferNextMessage(MessageBuffer buffer, boolean isDataMessage) {
        this.messageBuffer = buffer;

        this.bufferMessage = true;
        this.isDataMessage = isDataMessage;
        this.receivedOk = false;
    }

    // Runnable method
    @Override
    public void run() {
        StringBuilder sb = new StringBuilder();
        int data;

        while (true) {
            try {
                data = in.read();
            }        // Read one integer from data stream.
            catch (IOException e) {
                break;
            } // Exit loop when there is an IOException. (e.g. Data stream has been closed.)

            if (data == -1) break;           // Exit loop if end of data stream has been reached.

            char c = (char) data;             // Cast data (int) to char.

            if (c != '\n') {                 // Check if end of line character has been reached.
                sb.append(c);                // Append char to StringBuilder.
                continue;
            }

            // End of line has been reached
            String message = sb.toString();
            handleMessage(message);
            sb.setLength(0);
        }
    }

    private void handleMessage(String message) {
        if (bufferMessage) {
            if (isDataMessage) {
                if (message.startsWith("ERR")) {
                    isDataMessage = false;
                    bufferMessage = false;

                    messageBuffer.setMessage(message);
                } else if (message.startsWith("OK"))
                    isDataMessage = false;
                receivedOk = true;
            } else if (message.startsWith("OK") || message.startsWith("ERR") || (message.startsWith("SVR") && receivedOk)) {
                bufferMessage = false;

                messageBuffer.setMessage(message);
            }
        } else if (message.startsWith("SVR GAME ")) {
            String event = message.replace("SVR ", "");
            svrEventListener.onEvent(event);
        }
    }
}
