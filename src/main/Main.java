package main;

import games.TicTacToe.TicTacToe;
import players.HumanConsolePlayer;

public class Main {
    public static void main(String[] args) {
        TicTacToe ticTacToe = new TicTacToe();
        ticTacToe.startGame(new HumanConsolePlayer(), new HumanConsolePlayer());
    }
}
