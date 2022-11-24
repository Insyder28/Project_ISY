package players;

import games.data.Board;
import games.data.Icon;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A player that does random moves.
 * @author Erwin Veenhoven
 */
public class RandomPlayer implements Player {
    private Icon icon;
    private final int delay;

    /**
     * Constructor for RandomPlayer.
     */
    public RandomPlayer() {
        this(0);
    }

    /**
     * Constructor for RandomPlayer.
     * @param delay The delay in milliseconds before the player returns its move.
     */
    public RandomPlayer(int delay) {
        this.delay = delay;
    }

    /**
     * Getter for player Icon.
     * @return The player icon.
     */
    @Override
    public Icon getIcon() {
        return icon;
    }

    /**
     * Setter for player icon.
     * @param icon The icon.
     */
    @Override
    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    /**
     * Calculates a random move.
     * @param board The board on witch the player has to do a move.
     * @return a random move.
     */
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
