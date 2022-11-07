package players;

import games.Board;
import games.Icon;

/**
 * Interface that represents a player.
 */
public interface Player {
    Icon getIcon();
    void setIcon(Icon icon);
    int move(Board board);
}
