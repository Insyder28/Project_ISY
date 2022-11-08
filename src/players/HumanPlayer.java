package players;

import games.Board;
import games.Icon;
import gui.GUI;

/**
 * A player that lets a human decide its moves.
 */
public class HumanPlayer implements Player {
    private Icon icon = Icon.NO_ICON;
    private final GUI gui;

    public HumanPlayer(GUI gui) {
        this.gui = gui;
    }

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
    public int move(Board board) {
        //Scanner input = new Scanner(System.in);

        while (true) {
//            System.out.print("\nPlayer '" + icon.getChar() + "', enter your move (column[1-3] row[1-3]): ");
            int move = gui.getMove();

            if (!validateMove(move, board)) {
                //System.out.println("Invalid Move");
                continue;
            }

            return move;
        }
    }

    private boolean validateMove(int move, Board board) {

        int row = move / board.height;
        int col = move % board.width;

        if (move < 0 || move > 8) return false;
        return board.data[row][col] == Icon.NO_ICON;
    }
}
