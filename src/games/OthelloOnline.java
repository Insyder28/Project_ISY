package games;

import networking.GameSocket;

public class OthelloOnline implements OnlineGame {
    private final GameSocket gameSocket;

    public OthelloOnline(GameSocket gameSocket) {
        this.gameSocket = gameSocket;
    }

    @Override
    public void onMatch(String args) {

    }

    @Override
    public void onYourTurn(String args) {

    }

    @Override
    public void onMove(String args) {

    }

    @Override
    public void onLoss(String args) {

    }

    @Override
    public void onWin(String args) {

    }

    @Override
    public void onDraw(String args) {

    }
}
