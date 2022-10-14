package games;

public class TicTacToe extends Game {
    /**
     * Constructor for TicTacToe game class
     */
    public TicTacToe() {
        super(3, 3);
    }

    /**
     * isMoveAllowed returns true or false based on whether the move a player wishes to perform is allowed or not
     *
     * @param move An integer representing the move a player would like to make
     * @return Boolean
     */
    public boolean isMoveAllowed(int move) {
        int y = move/sizeX;
        int x = move%sizeX;
        return board[x][y] == ' ';
    }

    /**
     * getMovesLeft returns true or false based on whether there are available moves left on the board.
     *
     * @return Boolean
     */
    public boolean getMovesLeft() {
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                if (board[x][y] == ' ') {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Performs the move a player wishes to make.
     *
     * @param move The move a player wishes to make
     * @param player The player whose turn it is
     */
    public void move(int move, char player) {
        if(isMoveAllowed(move)) {
            int y = move/sizeX;
            int x = move%sizeX;
            board[x][y] = player;
        } else {
            System.out.println("Move not allowed!");

        }
    }

    /**
     * hasWon returns a boolean value based on whether the player has won the game or not.
     *
     * @param player The player whose turn it is
     * @return Boolean
     */
    public boolean hasWon(char player) {
        for(int x = 0; x<sizeX; x++) {
            if (board[x][0] == player && board[x][1] == player && board[x][2] == player) {
                return true;
            }
        }
        for(int y = 0; y<sizeX; y++) {
            if (board[0][y] == player && board[1][y] == player && board[2][y] == player) {
                return true;
            }
        }
        if(board[0][0] == player && board[1][1] == player && board[2][2] == player) {
            return true;
        } else return board[0][2] == player && board[1][1] == player && board[2][0] == player;
    }
}
