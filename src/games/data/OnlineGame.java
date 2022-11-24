package games.data;

import networking.ServerTimedOutException;

import java.util.Map;

public interface OnlineGame {
    void onMatch(Map<String, String> data);

    void onYourTurn(Map<String, String> data) throws ServerTimedOutException;

    void onMove(Map<String, String> data);

    void onLoss(Map<String, String> data);

    void onWin(Map<String, String> data);

    void onDraw(Map<String, String> data);
}
