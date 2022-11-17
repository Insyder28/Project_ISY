package gui;

public interface GUIEventListener {
    void onStartLocalGame();
    void onConnect(String ip) throws ActionFailedException;
    void onLogin(String name) throws ActionFailedException;
    void onDisconnect();
}
