package players;

import games.Board;
import games.Icon;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hindrik Kroes
 */
public class AIPlayer implements Player {
    protected Icon myIcon;
    protected Icon oppIcon;
    protected int ROWS;
    protected int COLS;
    protected Icon[][] cells;

    /** Getter for myIcon variable
     *
     * @return Icon myIcon
     */
    @Override
    public Icon getIcon() {
        return myIcon;
    }

    /** Setter for myIcon, automatically sets oppIcon as well
     *
     * @param icon Icon to be set for AI
     */
    @Override
    public void setIcon(Icon icon) {
        this.myIcon = icon;
        oppIcon = (myIcon == Icon.CROSS) ? Icon.NOUGHT : Icon.CROSS;
    }

    /** Move function that initializes the board used by the minimax function and calls the minimax function
     *
     * @param board The board on which the AI plays
     * @return The best move the AI can make
     */
    @Override
    public int move(Board board) {
        ROWS = board.height;
        COLS = board.width;
        cells = board.data;

        int[] results = minimax(9, myIcon);
        //System.out.println("AI wants to play col: " + results[2] + " row: " + results[1]);
        return (results[1]) * ROWS + results[2];
    }

    /** Recursively searches a tree down to a specific depth, and returns a list of integers containing the best score
     * which is to be evaluated by the minimax function on the previous node, as well as the row and column that
     * obtained that best score
     *
     * @param depth The depth of the search tree
     * @param player The player whose turn it is, either myIcon or oppIcon
     * @return List containing bestScore, bestRow, and bestCol
     */
    private int[] minimax(int depth, Icon player) {
        List<int[]> nextMoves = generateMoves();

        int bestScore = (player == myIcon) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int currentScore;
        int bestRow = -1;
        int bestCol = -1;

        if (nextMoves.isEmpty() || depth == 0) {
            bestScore = evaluate();
        } else {
            for (int[] move : nextMoves) {
                cells[move[0]][move[1]] = player;
                if (player == myIcon) {
                    currentScore = minimax(depth - 1, oppIcon)[0];
                    if (currentScore > bestScore) {
                        bestScore = currentScore;
                        bestRow = move[0];
                        bestCol = move[1];
                    }
                } else {
                    currentScore = minimax(depth - 1, myIcon)[0];
                    if (currentScore < bestScore) {
                        bestScore = currentScore;
                        bestRow = move[0];
                        bestCol = move[1];
                    }
                }
                cells[move[0]][move[1]] = Icon.NO_ICON;
            }
        }
        return new int[] {bestScore, bestRow, bestCol};
    }

    /** Generates a list of available moves, that the minimax function needs to recursively evaluate
     *
     * @return List of int[], containing all available row-column combinations
     */
    private List<int[]> generateMoves() {
        List<int[]> nextMoves = new ArrayList<>(); // allocate List

        // If gameover, i.e., no next move
        if (hasWon(myIcon) || hasWon(oppIcon)) {
            return nextMoves;   // return empty list
        }

        // Search for empty cells and add to the List
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                if (cells[row][col] == Icon.NO_ICON) {
                    nextMoves.add(new int[] {row, col});
                }
            }
        }
        return nextMoves;
    }

//    Niet werkende Heuristic evaluation code

//    private static int getLongestRun(Icon[] array, Icon icon) {
//        int count = 1;
//        int max = 1;
//
//        for (int i = 1; i < array.length; i++) {
//            if (array[i] == array[i - 1] && array[i] == icon) {
//                count++;
//            } else {
//                count = 1;
//            }
//            if (count > max) {
//                max = count;
//            }
//        } return max;
//    }
//
//    private Icon[][] DiagsDR(Icon[][] array) {
//        Icon[][] temp = new Icon[COLS+ROWS][COLS+ROWS];
//        for (int x = 0; x < COLS-2; x++) {
//            for (int i = 0; i < COLS && i < ROWS; i++) {
//                temp[x][i] = array[i][i];
//            }
//        }
//        for (int y = 0; y < ROWS-2; y++) {
//            for (int i = 0; i < COLS && i < ROWS; i++) {
//                temp[y+COLS-2][i] = array[i][i];
//            }
//        } return temp;
//    }
//
//    private Icon[][] DiagsDL(Icon[][] array) {
//        Icon[][] temp = new Icon[COLS+ROWS][COLS+ROWS];
//        for (int x = COLS-1; x > 1; x--) {
//            for (int i = 0; i >= 0 && i < ROWS; i++) {
//                temp[x][i] = array[i][x];
//            }
//        }
//        for (int y = 0; y < ROWS-2; y++) {
//            for (int i = 0; i < COLS && i < ROWS; i++) {
//                temp[y+COLS-2][i] = array[i][ROWS-1-i];
//            }
//        } return temp;
//    }
//
//    private Icon[][] flip(Icon[][] cells) {
//        Icon[][] temp = new Icon[COLS][ROWS];
//        for (int x = 0; x < COLS; x++) {
//            for (int y = 0; y < ROWS; y++) {
//                temp[x][y] = cells[y][x];
//            }
//        } return temp;
//    }
//
//    private int evaluate() {
//        int score = 0;
//
//        //Add score for each row
//        score += getScore(cells);
//
//        //Add score for each column
//        //Flip 2d array of cells first so that rows become columns and vice versa
//        score += getScore(flip(cells));
//
//        //Add score for down-right diagonals
//        score += getScore(DiagsDR(cells));
//
//        //Add score for down-left diagonals
//        score += getScore(DiagsDL(cells));
//
//        return score;
//    }
//
//    private int getScore(Icon[][] cells) {
//        // Works for all rows in a 2d array
//        int score = 0;
//        int longestRun;
//        for (Icon[] x : cells) {
//            longestRun = getLongestRun(x, myIcon);
//
//            System.out.println("Longest run for AI in this line: " + longestRun);
//            switch (longestRun) {
//                case 0:
//                case 1:
//                    score += 1;
//                case 2:
//                    score += 10;
//                case 3:
//                    score += 100;
//            }
//            longestRun = getLongestRun(x, oppIcon);
//            System.out.println("Longest run for Human in this line: " + longestRun);
//            switch (longestRun) {
//                case 0:
//                case 1:
//                    score -= 1;
//                case 2:
//                    score -= 10;
//                case 3:
//                    score -= 100;
//            }
//        }
//        return score;
//    }

    /** Evaluation function that the minimax function calls to get a score for the current board
     * Hardcoded at the moment, the more complex function that works for any board size is not functional at the moment
     *
     * @return The score of the current board
     */
    private int evaluate() {
        int score = 0;
        // Evaluate score for each of the 8 lines (3 rows, 3 columns, 2 diagonals)
        score += evaluateLine(0, 0, 0, 1, 0, 2);  // row 0
        score += evaluateLine(1, 0, 1, 1, 1, 2);  // row 1
        score += evaluateLine(2, 0, 2, 1, 2, 2);  // row 2
        score += evaluateLine(0, 0, 1, 0, 2, 0);  // col 0
        score += evaluateLine(0, 1, 1, 1, 2, 1);  // col 1
        score += evaluateLine(0, 2, 1, 2, 2, 2);  // col 2
        score += evaluateLine(0, 0, 1, 1, 2, 2);  // diagonal
        score += evaluateLine(0, 2, 1, 1, 2, 0);  // alternate diagonal
        return score;
    }

    /** The heuristic evaluation function for the given line of 3 cells
     @return +100, +10, +1 for 3-, 2-, 1-in-a-line for computer.
     -100, -10, -1 for 3-, 2-, 1-in-a-line for opponent.
     0 otherwise */
    private int evaluateLine(int row1, int col1, int row2, int col2, int row3, int col3) {
        int score = 0;

        // First cell
        if (cells[row1][col1] == myIcon) {
            score = 1;
        } else if (cells[row1][col1] == oppIcon) {
            score = -1;
        }

        // Second cell
        if (cells[row2][col2] == myIcon) {
            if (score == 1) {   // cell1 is mySeed
                score = 10;
            } else if (score == -1) {  // cell1 is oppSeed
                return 0;
            } else {  // cell1 is empty
                score = 1;
            }
        } else if (cells[row2][col2] == oppIcon) {
            if (score == -1) { // cell1 is oppSeed
                score = -10;
            } else if (score == 1) { // cell1 is mySeed
                return 0;
            } else {  // cell1 is empty
                score = -1;
            }
        }

        // Third cell
        if (cells[row3][col3] == myIcon) {
            if (score > 0) {  // cell1 and/or cell2 is mySeed
                score *= 10;
            } else if (score < 0) {  // cell1 and/or cell2 is oppSeed
                return 0;
            } else {  // cell1 and cell2 are empty
                score = 1;
            }
        } else if (cells[row3][col3] == oppIcon) {
            if (score < 0) {  // cell1 and/or cell2 is oppSeed
                score *= 10;
            } else if (score > 1) {  // cell1 and/or cell2 is mySeed
                return 0;
            } else {  // cell1 and cell2 are empty
                score = -1;
            }
        }
        return score;
    }

    /** Checks whether the player has won
     * @param icon The player to check
     * @return True or False, depending on whether the player has won
     */
    private boolean hasWon(Icon icon) {
        //Check all columns for 3 in a row of icon
        for (int row = 0; row < ROWS-2; row++) {
            for (int col = 0; col < COLS; col++) {
                if (cells[row][col] == icon
                        && cells[row][col] == cells[row + 1][col]
                        && cells[row][col] == cells[row + 2][col]) {
                    return true;
                }
            }
        }
        //Check all rows for 3 in a row of icon
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS-2; col++) {
                if (cells[row][col] == icon
                        && cells[row][col] == cells[row][col + 1]
                        && cells[row][col] == cells[row][col + 2]) {
                    return true;
                }
            }
        }
        //Check diagonals down-right
        for (int row = 0; row < ROWS-2; row++) {
            for (int col = 0; col < COLS-2; col++) {
                if (cells[row][col] == icon
                        && cells[row][col] == cells[row + 1][col + 1]
                        && cells[row][col] == cells[row + 2][col + 2]) {
                    return true;
                }
            }
        }
        //Check diagonals down-left
        for (int row = 0; row < ROWS-2; row++) {
            for (int col = 2; col < COLS; col++) {
                if (cells[row][col] == icon
                        && cells[row][col] == cells[row + 1][col - 1]
                        && cells[row][col] == cells[row + 2][col - 2]) {
                    return true;
                }
            }
        }
        return false;
    }
}
