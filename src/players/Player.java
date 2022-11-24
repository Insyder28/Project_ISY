package players;

import games.data.Board;
import games.data.Icon;

/**
 * Interface that represents a player.
 */
public interface Player {
    Icon getIcon();
    void setIcon(Icon icon);
    int move(Board board);
}
