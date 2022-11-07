package games;

/**
 * A board class for playing games.
 */
public class Board {
    public final Icon[][] data;
    public final int width;
    public final int height;

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

    /**
     * Clears the board.
     */
    public void clear() {
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                data[i][j] = Icon.NO_ICON;
    }
}
