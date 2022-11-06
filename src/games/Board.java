package games;

public class Board {
    public final Icon[][] data;
    public final int width;
    public final int height;

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

    public void set(int pos, Icon icon) {
        data[pos / height][pos % height] = icon;
    }
}
