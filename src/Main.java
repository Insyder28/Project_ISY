import games.TicTacToe;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int game = menu();
        switch (game) {
            case 1 -> {
                System.out.println("Othello is not implemented yet!");
            }
            case 2 -> tictactoe();
        }
    }

    private static void tictactoe() {
        Scanner input = new Scanner(System.in);
        TicTacToe test = new TicTacToe();
        test.createBoard();
        while(test.getMovesLeft()) {
            test.showBoard();
            ticmove(input, test, 'X');
            if (test.hasWon('X')) {
                test.showBoard();
                System.out.println("Player 'X' has won!");
                return;
            }
            test.showBoard();
            ticmove(input, test, 'O');
            if (test.hasWon('O')) {
                test.showBoard();
                System.out.println("Player 'O' has won!");
                return;
            }
        }
    }

    private static void ticmove(Scanner input, TicTacToe test, char player) {
        System.out.println("Select a move: (0-8)");
        int move = input.nextInt();
        if (test.isMoveAllowed(move)) {
            test.move(move, player);
        } else {
            System.out.println("Not available! Try again.");
            ticmove(input, test, player);
        }
    }

    private static int menu() {
        Scanner input = new Scanner(System.in);
        System.out.println("Please select a game:");
        System.out.println("1 Othello");
        System.out.println("2 TicTacToe");
        return input.nextInt();
    }
}