package players;

import games.data.Board;
import games.data.GameType;
import games.data.Icon;

import java.util.Scanner;

/**
 * A player that lets a human decide its moves.
 * @author Erwin Veenhoven
 */
public class HumanConsolePlayer implements Player {
    private Icon icon = Icon.NO_ICON;

    /**
     * Setter for player icon.
     * @param icon The icon.
     */
    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    /**
     * Getter for player Icon.
     * @return The player icon.
     */
    public Icon getIcon() {
        return icon;
    }

    /**
     * Asks player to do a move.
     * @param board The board on witch the player has to do a move.
     * @return A move (1-8).
     */
    public int move(Board board){
        Scanner input = new Scanner(System.in);

        while (true) {
            System.out.print("\nPlayer '" + icon.getChar() + "', enter your move: ");
            int move = input.nextInt();

            if (!validateMove(move, board)) {
                System.out.println("Invalid Move");
                continue;
            }

            return move;
        }
    }

    private boolean validateMove(int move, Board board) {

        int row = move / board.width;
        int col = move % board.width;

        if (move < 0 || move > board.width*board.height) return false;
        return board.data[row][col] == Icon.NO_ICON;
    }
}
