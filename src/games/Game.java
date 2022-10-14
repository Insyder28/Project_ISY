package games;

public class Game {
    int sizeX, sizeY;
    char[][] board;

    /**
     * Constructor for Game object
     *
     * @param sizeX Width of the board
     * @param sizeY Height of the board
     */
    public Game(int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;


    }


    /**
     * Creates a board of specified size
     */
    public void createBoard() {
        board = new char[sizeX][sizeY];
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                board[x][y] = ' ';
            }
        }
    }

    /**
     * Shows the contents of a board.
     */
    public void showBoard() {
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                System.out.print(board[x][y]);
                System.out.print(" ");
            }
            System.out.print("\n");
        }
    }
}
