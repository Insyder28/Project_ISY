package games;

import networking.GameSocket;

import java.util.Map;

public class TicTacToeOnline implements OnlineGame {
    private final GameSocket gameSocket;

    public TicTacToeOnline(GameSocket gameSocket) {
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
