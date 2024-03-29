package players;

import games.data.Board;
import games.data.GameType;
import games.data.Icon;
import gui.GUI;
import main.GameController;

/**
 * A player that lets a human decide its moves.
 * @author Erwin Veenhoven
 */
public class HumanPlayer implements Player {
    private Icon icon = Icon.NO_ICON;
    private final GUI gui = GameController.getInstance().getGUI();

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
    public int move(Board board) throws InterruptedException{
        //Scanner input = new Scanner(System.in);

        while (true) {
//            System.out.print("\nPlayer '" + icon.getChar() + "', enter your move (column[1-3] row[1-3]): ");
            int move = 0;
            if (gui.getSelectedGameType() == GameType.TICTACTOE) {
                move = gui.getTicTacToeGUI().getMove();
            } else if (gui.getSelectedGameType() == GameType.OTHELLO) {
                move = gui.getOthelloGUI().getMove();
            }
            if (!validateMove(move, board)) {
                //System.out.println("Invalid Move");
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
