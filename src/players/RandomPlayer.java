package players;

import games.Board;
import games.Icon;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomPlayer implements Player {
    private Icon icon;
    private int delay;

    public RandomPlayer() {
        this(0);
    }

    public RandomPlayer(int delay) {
        this.delay = delay;
    }

    @Override
    public Icon getIcon() {
        return icon;
    }

    @Override
    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    @Override
    public int move(Board board) {
        List<Integer> positions = new ArrayList<>();

        int pos = 0;
        for (Icon[] row : board.data) {
            for (Icon col : row) {
                if (col == Icon.NO_ICON) positions.add(pos);
                pos++;
            }
        }

        if (delay > 0) {
            try { Thread.sleep(delay); }
            catch (InterruptedException ignored) { }
        }

        Random rnd = new Random();
        return positions.get(rnd.nextInt(0, positions.size()));
    }
}
