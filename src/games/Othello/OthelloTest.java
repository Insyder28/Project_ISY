package games.Othello;

import games.data.Icon;
import players.OthelloAIMT;
import players.Player;
import players.RandomPlayer;

class OthelloTest {

    @org.junit.jupiter.api.Test
    void main() {
        int o = 0;
        int x = 0;
        long longestTime = 0;

        Player xPlayer = new OthelloAIMT();
        Player oPlayer = new RandomPlayer();
        for (int counter = 0; counter < 100; counter++) {
            Othello game = new Othello();
            Icon result = game.startGame(xPlayer, oPlayer);
            if (result == Icon.NOUGHT) o++;
            if (result == Icon.CROSS) x++;
            if (game.getLongestTime() > longestTime){
                longestTime = game.getLongestTime();
            }
        }
        System.out.println("O: " + o + " X: " + x);
        System.out.println("Longest time: " + longestTime);
    }
}