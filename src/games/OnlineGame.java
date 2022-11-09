package games;

import java.util.Map;

public interface OnlineGame {
    void onMatch(String args);

    void onYourTurn(String args);

    void onMove(String args);

    void onLoss(String args);

    void onWin(String args);

    void onDraw(String args);
}
