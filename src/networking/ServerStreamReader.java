package networking;

import events.EventListener;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Wrapper for {@link InputStream} for handling server input.
 * @author Erwin Veenhoven
 */
public class ServerStreamReader implements Closeable {
    private final InputStream in;
    private final EventListener svrEventListener;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private MessageBuffer messageBuffer;
    private volatile boolean bufferResponse;
    private volatile boolean isDataResponse;
    private boolean receivedOk;


    /**
     * Creates a new {@link ServerStreamReader} from an existing {@link InputStream}.
     * @param in The input stream from the server.
     * @param svrEventListener Will get called when there is a server event.
     */
    @SuppressWarnings("unused")
    ServerStreamReader(InputStream in, EventListener svrEventListener) {
        this.in = in;
        this.svrEventListener = svrEventListener;

        executor.execute(this::handleInputStreamLoop);
    }

    /**
     * Tell the {@link ServerStreamReader} to buffer the next response to a command.
     * @param buffer The buffer to store the response in.
     */
    @SuppressWarnings("unused")
    public void bufferNextResponse(MessageBuffer buffer) {
        bufferNextResponse(buffer, false);
    }

    /**
     * Tell the {@link ServerStreamReader} to buffer the next response to a command.
     * @param buffer The buffer to store the response in.
     * @param isDataResponse Set to true when the response contains data.
     */
    public void bufferNextResponse(MessageBuffer buffer, boolean isDataResponse) {
        this.messageBuffer = buffer;

        this.bufferResponse = true;
        this.isDataResponse = isDataResponse;
        this.receivedOk = false;
    }

    /**
     * Closes the {@link ServerStreamReader} and the underlying {@link InputStream}.
     */
    public void close() {
        try { in.close(); }
        catch (Exception ignored) { }

        executor.shutdownNow();
    }

    private void handleInputStreamLoop() {
        StringBuilder sb = new StringBuilder();
        int data;

        while (true) {
            try { data = in.read(); }        // Read one integer from data stream.
            catch (IOException e) { break; } // Exit loop when there is an IOException. (e.g. Data stream has been closed.)

            if (data == -1) break;           // Exit loop if end of data stream has been reached.

            char c = (char) data;            // Cast data (int) to char.

            if (c != '\n') {                 // Check if end of line character has been reached.
                sb.append(c);                // Append char to StringBuilder.
                continue;
            }

            // End of line has been reached
            String message = sb.toString();
            handleMessage(message);
            sb.setLength(0);                 // Reset StringBuilder
        }

        executor.shutdown();
    }

    private void handleMessage(String message) {
        if (message.startsWith("SVR GAME ")) {
            String event = message.replace("SVR ", "");
            svrEventListener.onEvent(event);
        }

        else if (bufferResponse) {
            if (isDataResponse) {
                if (message.startsWith("ERR")) {
                    isDataResponse = false;
                    bufferResponse = false;
                    messageBuffer.setMessage(message);
                }
                else if (message.startsWith("OK")) {
                    isDataResponse = false;
                    receivedOk = true;
                }
            }
            else if (message.startsWith("OK") || message.startsWith("ERR") || (message.startsWith("SVR") && receivedOk)) {
                messageBuffer.setMessage(message);
                bufferResponse = false;
            }
        }
    }
}
