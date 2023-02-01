package main;

import games.TicTacToe.TicTacToeOnline;
import networking.GameSocket;
import players.PlayerType;
import util.Mapper;

import java.util.Map;

public class Main {
    private static GameSocket gameSocket;

    public static void main(String[] args) {
        try {
            gameSocket = new GameSocket("localhost", 7789);

            gameSocket.login(args[0]);

            gameSocket.onMatchEvent.addListener(Main::foundMatch);

            gameSocket.subscribe("tic-tac-toe");

            //gameSocket.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void foundMatch(String info) {
        Map<String, String> data = Mapper.messageToMap(info);

        TicTacToeOnline ticTacToe = new TicTacToeOnline(gameSocket, data.get("PLAYERTOMOVE"), data.get("OPPONENT"),"you", PlayerType.CONSOLE);

        gameSocket.onYourTurnEvent.addListener(ticTacToe::onYourTurn);
        gameSocket.onMoveEvent.addListener(ticTacToe::onMove);
        gameSocket.onLossEvent.addListener(ticTacToe::onLoss);
        gameSocket.onWinEvent.addListener(ticTacToe::onWin);
        gameSocket.onDrawEvent.addListener(ticTacToe::onDraw);
    }
}
