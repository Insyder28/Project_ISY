package players;

import games.Board;
import games.Icon;

public interface Player {

    Icon getIcon();
    void setIcon(Icon icon);
    int move(Board board);
}
