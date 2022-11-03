import networking.GameSocket;
import networking.ServerException;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try (GameSocket gameSocket = new GameSocket("localhost", 7789)) {

            initialise(gameSocket);
            gameSocket.login("Erwin");

            String[] games = gameSocket.getGameList();
            for(var game : games) System.out.println(game);
        }
        catch (ServerException | IOException e) {
            throw new RuntimeException(e);
        }

    }




    public static void initialise(GameSocket gameSocket) {
        gameSocket.onChallengeCancelledEvent.addListener((arguments) -> System.out.println("OnChallengeCancelled: " + arguments));

        gameSocket.onChallengeEvent.addListener((arguments) -> System.out.println("OnChallenge: " + arguments));

        gameSocket.onMatchEvent.addListener((arguments) -> System.out.println("OnMatch: " + arguments));

        gameSocket.onDrawEvent.addListener((arguments) -> System.out.println("OnDraw: " + arguments));

        gameSocket.onLossEvent.addListener((arguments) -> System.out.println("OnLoss: " + arguments));

        gameSocket.onMoveEvent.addListener((arguments) -> System.out.println("OnMove: " + arguments));

        gameSocket.onWinEvent.addListener((arguments) -> System.out.println("OnWin: " + arguments));

        gameSocket.onYourTurnEvent.addListener((arguments) -> {
            System.out.println("OnYourTurn: " + arguments);
            try { gameSocket.move(4); }
            catch (Exception ignored) { }
        });
    }
}