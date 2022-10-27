package players;

import games.Board;
import games.Icon;

import java.util.Scanner;

public class HumanPlayer implements Player {
    private Icon icon = Icon.NO_ICON;

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public Icon getIcon() {
        return icon;
    }

    public int move(Board board) {
        Scanner input = new Scanner(System.in);

        System.out.println(board);
        System.out.print("\nPlayer '" + icon + "', enter your move (column[1-3] row[1-3]): ");
        int col = input.nextInt() - 1;   // [0-2]
        int row = input.nextInt() - 1;

        return row * board.width + col;
    }
}
