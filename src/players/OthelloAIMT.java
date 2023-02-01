package players;

import games.data.Board;
import games.data.Icon;

import java.util.*;
import java.util.concurrent.*;

public class OthelloAIMT implements Player{

    protected int ROWS;
    protected int COLS;
    protected int TIMEOUT = 9000;
    protected  Icon myIcon;
    protected Icon oppIcon;
    protected Icon[][] cells;
    protected Board board;
    static final int MAX_TH = 50;
    static final int MAX_DEPTH = 7;

    public Icon getIcon() {
        return myIcon;
    }

    @Override
    public void setIcon(Icon icon) {
        myIcon = icon;
        oppIcon = (myIcon == Icon.CROSS) ? Icon.NOUGHT : Icon.CROSS;
    }

    @Override
    public int move(Board board) throws InterruptedException, ExecutionException {
        ROWS = board.height;
        COLS = board.width;
        cells = new Icon[ROWS][COLS];
        this.board = board;
        for (int i = 0; i < ROWS; i++) {
            cells[i] = board.data[i].clone();
        }

        List<int[]> moves = generateMoves(this.board, myIcon);
        if (moves.isEmpty()) {
            System.out.println(this.board);
            System.out.println(board);
            System.out.println("No moves for me");
            System.out.println(moves.size());
            return -9;
        }

        ExecutorService pl = Executors.newFixedThreadPool(MAX_TH);
        Collection<Callable<int[]>> tasks = new ArrayList<>();
        createWorkers(moves, tasks);
        List<Future<int[]>> results = pl.invokeAll(tasks, TIMEOUT, TimeUnit.MILLISECONDS);
        pl.shutdown();

        int[][] resultPool = new int[moves.size()][];
        for(int i = 0; i < results.size(); i++) {
            try {
                resultPool[i] = results.get(i).get();
            } catch (InterruptedException | ExecutionException | CancellationException e) {
                System.out.println("Thread took too long");
                resultPool[i] = new int[]{i, Integer.MIN_VALUE};
            }
        }
        System.out.println(Arrays.deepToString(resultPool));
        Arrays.sort(resultPool, Comparator.comparingInt(o -> o[0]));
        System.out.println(Arrays.deepToString(resultPool));

        int highest = 0;
        int highscore = Integer.MIN_VALUE;
        for (int i = 0; i < moves.size(); i++) {
            if (resultPool[i][1] > highscore) {
                highest = i;
                highscore = resultPool[i][1];
            }
        }

        return moves.get(highest)[0] * ROWS + moves.get(highest)[1];
    }

    private void createWorkers(List<int[]> moves, Collection<Callable<int[]>> tasks) {
        for (int i = 0; i < moves.size(); i++) {
            Icon[][] cells2 = new Icon[ROWS][COLS];
            for (int row = 0; row < ROWS; row++) {
                System.arraycopy(cells[row], 0, cells2[row], 0, COLS);
            }
            int[] move = moves.get(i);
            makeMove(move[0], move[1], myIcon, cells2);
            WorkerThread worker = new WorkerThread(i, MAX_DEPTH, ROWS, COLS, cells2, oppIcon, myIcon);
            tasks.add(worker);
            System.out.println("Worker " + i + " added");
        }
    }


    void makeMove(int row, int col, Icon icon, Icon[][] cells) {
        String[] directions = {"UpLeft", "Up", "UpRight", "Left", "Right", "BottomLeft", "Bottom", "BottomRight"};

        for (String dir : directions) {
            if (checkDirection(dir, row, col, icon)) {
                flipDirection(dir, row, col, icon, cells);
            }
        }
    }

    void flipDirection(String dir, int row, int col, Icon icon, Icon[][] cells) {
        int deltaRow = 0, deltaCol = 0;
        if ("UpLeft".equals(dir)) {
            deltaRow = -1;
            deltaCol = -1;
        } else if ("Up".equals(dir)) {
            deltaRow = -1;
        } else if ("UpRight".equals(dir)) {
            deltaRow = -1;
            deltaCol = 1;
        } else if ("Left".equals(dir)) {
            deltaCol = -1;
        } else if ("Right".equals(dir)) {
            deltaCol = 1;
        } else if ("BottomLeft".equals(dir)) {
            deltaRow = 1;
            deltaCol = -1;
        } else if ("Bottom".equals(dir)) {
            deltaRow = 1;
        } else if ("BottomRight".equals(dir)) {
            deltaRow = 1;
            deltaCol = 1;
        }
        cells[row][col] = icon;
        row += deltaRow;
        col += deltaCol;

        while (cells[row][col] != icon) {
            cells[row][col] = icon;
            row += deltaRow;
            col += deltaCol;
        }
    }

    private List<int[]> generateMoves(Board board, Icon icon) {
        List<int[]> nextMoves = new ArrayList<>(); // allocate List

        for (int row = 0; row < board.height; row++) {
            for (int col = 0; col < board.width; col++) {
                if (validateMove(row, col, icon)) {
                    nextMoves.add(new int[] {row, col});
                }
            }
        }
        return nextMoves;
    }

    boolean validateMove(int row, int col, Icon icon) {
        String[] directions = {"UpLeft", "Up", "UpRight", "Left", "Right", "BottomLeft", "Bottom", "BottomRight"};

        if (board.data[row][col] != Icon.NO_ICON) {
            return false;
        }

        for (String dir : directions) {
            if (checkDirection(dir, row, col, icon)) {
                return true;
            }
        }

        return false;
    }

    boolean checkDirection(String dir, int row, int col, Icon icon) {
        Icon oppIcon = icon == Icon.CROSS ? Icon.NOUGHT : Icon.CROSS;

        int deltaRow = 0, deltaCol = 0;
        if ("UpLeft".equals(dir)) {
            deltaRow = -1;
            deltaCol = -1;
        } else if ("Up".equals(dir)) {
            deltaRow = -1;
        } else if ("UpRight".equals(dir)) {
            deltaRow = -1;
            deltaCol = 1;
        } else if ("Left".equals(dir)) {
            deltaCol = -1;
        } else if ("Right".equals(dir)) {
            deltaCol = 1;
        } else if ("BottomLeft".equals(dir)) {
            deltaRow = 1;
            deltaCol = -1;
        } else if ("Bottom".equals(dir)) {
            deltaRow = 1;
        } else if ("BottomRight".equals(dir)) {
            deltaRow = 1;
            deltaCol = 1;
        }
        if (row + deltaRow < 0 || col + deltaCol < 0 || row + deltaRow >= board.height || col + deltaCol >= board.width) {
            return false; // out of bounds
        }
        if (board.data[row + deltaRow][col + deltaCol] == oppIcon) {
            while ((row >= 0) && (row < board.height) && (col >= 0) && (col < board.width)) {
                row += deltaRow;
                col += deltaCol;
                if (row < 0 || col < 0 || row >= board.height || col >= board.width) {
                    return false; // out of bounds, so edge of board reached
                }
                if (board.data[row][col] == Icon.NO_ICON) {
                    return false;  // There is a gap
                }
                if (board.data[row][col] == icon) {
                    return true;  // There is a piece of our own after opponents piece(s)
                }
            }
        }
        return false;  // Edge of board or no oppIcon
    }

    public static class WorkerThread implements Callable<int[]> {
        private final int id;
        private final int depth;
        private final int ROWS;
        private final int COLS;
        private final Icon[][] cells;
        private final Icon[][][] pastBoards = new Icon[10][8][8];
        private final Icon oppIcon;
        private final Icon myIcon;

        public WorkerThread(int id, int depth, int ROWS, int COLS, Icon[][] cells, Icon oppIcon, Icon myIcon) {
            this.id = id;
            this.depth = depth;
            this.ROWS = ROWS;
            this.COLS = COLS;
            this.cells = cells;
            this.oppIcon = oppIcon;
            this.myIcon = myIcon;
        }
        @Override
        public int[] call() {
            int[] results = minimax(depth, oppIcon, Integer.MIN_VALUE, Integer.MAX_VALUE);
            System.out.println(Thread.currentThread().getName() + " has finished");
            return new int[]{id, results[0]}; // return worker id + score
        }

        private int[] minimax(int depth, Icon player, int alpha, int beta) {
            List<int[]> nextMoves = generateMoves(player);

            int currentScore;
            int bestRow = -1;
            int bestCol = -1;

            if (nextMoves.isEmpty() || depth == 0) {
                currentScore = evaluate();
                return new int[] {currentScore, bestRow, bestCol};
            }else {
                for (int row = 0; row < ROWS; row++) {  // Save current board to array of boards
                    if (COLS >= 0) System.arraycopy(cells[row], 0, pastBoards[depth][row], 0, COLS);
                }
                for (int[] move : nextMoves) {
                    makeMove(move[0], move[1], player);
                    if (player == myIcon) {
                        currentScore = minimax(depth - 1, oppIcon, alpha, beta)[0];
                        if (currentScore > alpha) {
                            alpha = currentScore;
                            bestRow = move[0];
                            bestCol = move[1];
                        }
                    } else {
                        currentScore = minimax(depth - 1, myIcon, alpha, beta)[0];
                        if (currentScore < beta) {
                            beta = currentScore;
                            bestRow = move[0];
                            bestCol = move[1];
                        }
                    }
                    // revert moves made in
                    for (int row = 0; row < ROWS; row++) {
                        if (COLS >= 0) System.arraycopy(pastBoards[depth][row], 0, cells[row], 0, COLS);
                    }
                    if (alpha >= beta) break;
                }
            }
            return new int[] {(player == myIcon) ? alpha : beta, bestRow, bestCol};
        }

        void makeMove(int row, int col, Icon icon) {
            String[] directions = {"UpLeft", "Up", "UpRight", "Left", "Right", "BottomLeft", "Bottom", "BottomRight"};

            for (String dir : directions) {
                if (checkDirection(dir, row, col, icon)) {
                    flipDirection(dir, row, col, icon);
                }
            }
        }

        private int evaluatemap() {
            int score = 0;
            int[][] map = new int[][]{{4, -3, 2, 2, 2, 2, -3, 4}, {-3, -4, -1, -1, -1, -1, -4, -3}, {2, -1, 1, 0, 0, 1, -1, 2}, {2, -1, 0, 1, 1, 0, -1, 2}, {2, -1, 0, 1, 1, 0, -1, 2}, {2, -1, 1, 0, 0, 1, -1, 2}, {-3, -4, -1, -1, -1, -1, -4, -3}, {4, -3, 2, 2, 2, 2, -3, 4}};
            for(int col = 0; col < COLS; col++) {
                for (int row = 0; row<ROWS; row++) {
                    if (cells[row][col] == myIcon) {
                        score += map[row][col];
                    } if (cells[row][col] == oppIcon) {
                        score -= map[row][col];
                    }
                }
            }
            return score;
        }

        private int evaluate() {
            if (generateMoves(Icon.NOUGHT).isEmpty() && generateMoves(Icon.CROSS).isEmpty()) {
                if (countDiscs() < 0)
                    return Integer.MIN_VALUE;
                if (countDiscs() > 0)
                    return Integer.MAX_VALUE;
                if (countDiscs() == 0)
                    return 0;
            }
            int score = 0;
            score += countDiscs();  // each disk has a weight of 1, which isn't much, because disc count is trivial

            score += generateMoves(myIcon).size() * 100;  // Number of valid moves is more important, thus a weight of 100
            score -= generateMoves(oppIcon).size() * 100;

            score += countCorners(myIcon) * 1000;  // Corners have a weight of 1000 because they can never be taken back
            score -= countCorners(oppIcon) * 1000;

            return score;

        }

        private int countCorners(Icon icon) {
            int count = 0;
            if (cells[0][0] == icon) {
                count++;
            } if (cells[0][COLS-1] == icon) {
                count++;
            } if (cells[ROWS-1][0] == icon) {
                count++;
            } if (cells[ROWS-1][COLS-1] == icon) {
                count++;
            }
            return count;
        }

        private int countDiscs() {
            int count = 0;
            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLS; col++) {
                    if (cells[row][col] == myIcon) {
                        count++;
                    } if (cells[row][col] == oppIcon) {
                        count--;
                    }
                }
            }
            return count;
        }

        private List<int[]> generateMoves(Icon icon) {
            List<int[]> nextMoves = new ArrayList<>(); // allocate List

            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLS; col++) {
                    if (validateMove(row, col, icon)) {
                        nextMoves.add(new int[] {row, col});
                    }
                }
            }
            return nextMoves;
        }

        boolean validateMove(int row, int col, Icon icon) {
            String[] directions = {"UpLeft", "Up", "UpRight", "Left", "Right", "BottomLeft", "Bottom", "BottomRight"};

            if (cells[row][col] != Icon.NO_ICON) {
                return false;
            }

            for (String dir : directions) {
                if (checkDirection(dir, row, col, icon)) {
                    return true;
                }
            }

            return false;
        }

        boolean checkDirection(String dir, int row, int col, Icon icon) {
            Icon oppIcon = icon == Icon.CROSS ? Icon.NOUGHT : Icon.CROSS;

            int deltaRow = 0, deltaCol = 0;
            if ("UpLeft".equals(dir)) {
                deltaRow = -1;
                deltaCol = -1;
            } else if ("Up".equals(dir)) {
                deltaRow = -1;
            } else if ("UpRight".equals(dir)) {
                deltaRow = -1;
                deltaCol = 1;
            } else if ("Left".equals(dir)) {
                deltaCol = -1;
            } else if ("Right".equals(dir)) {
                deltaCol = 1;
            } else if ("BottomLeft".equals(dir)) {
                deltaRow = 1;
                deltaCol = -1;
            } else if ("Bottom".equals(dir)) {
                deltaRow = 1;
            } else if ("BottomRight".equals(dir)) {
                deltaRow = 1;
                deltaCol = 1;
            }
            if (row + deltaRow < 0 || col + deltaCol < 0 || row + deltaRow >= ROWS || col + deltaCol >= COLS) {
                return false; // out of bounds
            }
            if (cells[row + deltaRow][col + deltaCol] == oppIcon) {
                while ((row >= 0) && (row < ROWS) && (col >= 0) && (col < COLS)) {
                    row += deltaRow;
                    col += deltaCol;
                    if (row < 0 || col < 0 || row >= ROWS || col >= COLS) {
                        return false; // out of bounds, so edge of board reached
                    }
                    if (cells[row][col] == Icon.NO_ICON) {
                        return false;  // There is a gap
                    }
                    if (cells[row][col] == icon) {
                        return true;  // There is a piece of our own after opponents piece(s)
                    }
                }
            }
            return false;  // Edge of board or no oppIcon
        }

        void flipDirection(String dir, int row, int col, Icon icon) {
            int deltaRow = 0, deltaCol = 0;
            if ("UpLeft".equals(dir)) {
                deltaRow = -1;
                deltaCol = -1;
            } else if ("Up".equals(dir)) {
                deltaRow = -1;
            } else if ("UpRight".equals(dir)) {
                deltaRow = -1;
                deltaCol = 1;
            } else if ("Left".equals(dir)) {
                deltaCol = -1;
            } else if ("Right".equals(dir)) {
                deltaCol = 1;
            } else if ("BottomLeft".equals(dir)) {
                deltaRow = 1;
                deltaCol = -1;
            } else if ("Bottom".equals(dir)) {
                deltaRow = 1;
            } else if ("BottomRight".equals(dir)) {
                deltaRow = 1;
                deltaCol = 1;
            }
            cells[row][col] = icon;
            row += deltaRow;
            col += deltaCol;

            while (cells[row][col] != icon) {
                cells[row][col] = icon;
                row += deltaRow;
                col += deltaCol;
            }
        }
    }
}
