package games;

public class Othello extends Game {
    public Othello() {
        super(8, 8);
    }

    /**
     * createBoard creates the board inside the 2d array, filling it with the required characters.
     */
    @Override
    public void createBoard() {
        board = new char[sizeX][sizeY];
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                board[x][y] = ' ';
            }
        }
        board[3][3] = 'B';
        board[4][3] = 'W';
        board[3][4] = 'W';
        board[4][4] = 'B';
    }
}
