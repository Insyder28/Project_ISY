package games.data;

import java.util.ArrayList;
import java.util.List;

/**
 * A board class for playing games.
 */
public class Board {
    public final Icon[][] data;
    public final int width;
    public final int height;
    public static final int IN_PROGRESS = -1;

    /**
     * Constructor for board class.
     * @param width The width of the board.
     * @param height The height of the board.
     */
    public Board(int width, int height) {
        this.data = new Icon[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                data[i][j] = Icon.NO_ICON;
            }
        }

        this.width = width;
        this.height = height;
    }

    /**
     * ToString method for board.
     * @return The board formatted in a String.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < height; i++) {
            sb.append(' ');
            for (int j = 0; j < width; j++) {
                sb.append(data[i][j].getChar());
                if (j < width - 1) sb.append(" | ");
            }
            if (i < height - 1) sb.append("\n");
        }

        return sb.toString();
    }

    /**
     * Set an {@link Icon} on a specific place on the board.
     * @param pos The position on the board (0-8).
     * @param icon The icon to place on the board.
     */
    public void set(int pos, Icon icon) {
        data[pos / height][pos % height] = icon;
    }

    boolean validateMove(int row, int col, Icon icon) {
        String[] directions = {"UpLeft", "Up", "UpRight", "Left", "Right", "BottomLeft", "Bottom", "BottomRight"};

        if (data[row][col] != Icon.NO_ICON) {
            return false;
        }

        for (String dir : directions) {
            if (checkDirection(dir, row, col, icon)) {
                return true;
            }
        }

        return false;
    }

    boolean checkDirection(String dir, int row, int col, Icon icon) {
        Icon oppIcon = icon == Icon.CROSS ? Icon.NOUGHT : Icon.CROSS;

        int deltaRow = 0, deltaCol = 0;
        if ("UpLeft".equals(dir)) {
            deltaRow = -1;
            deltaCol = -1;
        } else if ("Up".equals(dir)) {
            deltaRow = -1;
        } else if ("UpRight".equals(dir)) {
            deltaRow = -1;
            deltaCol = 1;
        } else if ("Left".equals(dir)) {
            deltaCol = -1;
        } else if ("Right".equals(dir)) {
            deltaCol = 1;
        } else if ("BottomLeft".equals(dir)) {
            deltaRow = 1;
            deltaCol = -1;
        } else if ("Bottom".equals(dir)) {
            deltaRow = 1;
        } else if ("BottomRight".equals(dir)) {
            deltaRow = 1;
            deltaCol = 1;
        }
        if (row + deltaRow < 0 || col + deltaCol < 0 || row + deltaRow >= height || col + deltaCol >= width) {
            return false; // out of bounds
        }
        if (data[row + deltaRow][col + deltaCol] == oppIcon) {
            while ((row >= 0) && (row < height) && (col >= 0) && (col < width)) {
                row += deltaRow;
                col += deltaCol;
                if (row < 0 || col < 0 || row >= height || col >= width) {
                    return false; // out of bounds, so edge of board reached
                }
                if (data[row][col] == Icon.NO_ICON) {
                    return false;  // There is a gap
                }
                if (data[row][col] == icon) {
                    return true;  // There is a piece of our own after opponents piece(s)
                }
            }
        }
        return false;  // Edge of board or no oppIcon
    }

    public void makeMove(int row, int col, Icon icon) {
        String[] directions = {"UpLeft", "Up", "UpRight", "Left", "Right", "BottomLeft", "Bottom", "BottomRight"};

        for (String dir : directions) {
            if (checkDirection(dir, row, col, icon)) {
                flipDirection(dir, row, col, icon);
            }
        }

        data[row][col] = icon;
    }

    public int checkStatus() {
        int result = 0;
        if (generateMoves(Icon.CROSS).isEmpty() && generateMoves(Icon.NOUGHT).isEmpty()) {
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (data[i][j] == Icon.CROSS)
                        result++;
                    if (data[i][j] == Icon.NOUGHT)
                        result--;
                }
            }
            if (result < 0)
                return 2;
            if (result > 0)
                return 1;
            return 0;
        }
        else
            return IN_PROGRESS;
    }

    void flipDirection(String dir, int row, int col, Icon icon) {
        int deltaRow = 0, deltaCol = 0;
        if ("UpLeft".equals(dir)) {
            deltaRow = -1;
            deltaCol = -1;
        } else if ("Up".equals(dir)) {
            deltaRow = -1;
        } else if ("UpRight".equals(dir)) {
            deltaRow = -1;
            deltaCol = 1;
        } else if ("Left".equals(dir)) {
            deltaCol = -1;
        } else if ("Right".equals(dir)) {
            deltaCol = 1;
        } else if ("BottomLeft".equals(dir)) {
            deltaRow = 1;
            deltaCol = -1;
        } else if ("Bottom".equals(dir)) {
            deltaRow = 1;
        } else if ("BottomRight".equals(dir)) {
            deltaRow = 1;
            deltaCol = 1;
        }
        data[row][col] = icon;
        row += deltaRow;
        col += deltaCol;

        while (data[row][col] != icon) {
            data[row][col] = icon;
            row += deltaRow;
            col += deltaCol;
        }
    }

    public List<int[]> generateMoves(Icon icon) {
        List<int[]> nextMoves = new ArrayList<>(); // allocate List

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (validateMove(row, col, icon)) {
                    nextMoves.add(new int[]{row, col});
                }
            }
        }
        return nextMoves;
    }

    /**
     * Clears the board.
     */
    public void clear() {
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                data[i][j] = Icon.NO_ICON;
    }
}
