package games;

import gui.GUI;
import main.GameController;
import networking.GameSocket;
import players.Player;

import java.util.Map;

public class OthelloOnline implements OnlineGame {
    private final GameSocket gameSocket;
    private final GUI gui = GameController.getInstance().getGUI();
    private final Player player;

    public OthelloOnline(Player player, GameSocket gameSocket) {
        this.player = player;
        this.gameSocket = gameSocket;
    }

    @Override
    public void onMatch(Map<String, String> data) {

    }

    @Override
    public void onYourTurn(Map<String, String> data) {

    }

    @Override
    public void onMove(Map<String, String> data) {

    }

    @Override
    public void onLoss(Map<String, String> data) {

    }

    @Override
    public void onWin(Map<String, String> data) {

    }

    @Override
    public void onDraw(Map<String, String> data) {

    }
}
