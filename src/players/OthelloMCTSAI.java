package players;

import games.data.Icon;
import games.data.Board;

import java.util.ArrayList;
import java.util.List;

public class OthelloMCTSAI implements Player {

    private class Node {
        private Node parent;
        private int[] move;
        private Icon[][] state;
        private List<Node> children;
        private int visits;
        private int wins;

        public Node(Node parent, int[] move) {
            this.parent = parent;
            this.move = move;
            this.children = new ArrayList<>();
            this.visits = 0;
            this.wins = 0;

            if (parent == null) {
                this.state = new Icon[ROWS][COLS];
                for (int i = 0; i < ROWS; i++) {
                    state[i] = cells[i].clone();
                }
            } else {
                this.state = parent.state.clone();
                makeMove(move[0], move[1], myIcon, state);
            }
        }

        public boolean isLeaf() {
            return children.isEmpty();
        }

        public boolean isTerminal() {
            return generateMoves(myIcon).isEmpty() && generateMoves(oppIcon).isEmpty();
        }

        public void expand() {
            List<int[]> moves = generateMoves(myIcon);

            for (int[] move : moves) {
                Node child = new Node(this, move);
                children.add(child);
            }
        }

        public Node select() {
            Node selected = null;
            double maxScore = Double.NEGATIVE_INFINITY;

            if (children.isEmpty()) {
                return null;
            }

            for (Node child : children) {
                double score = child.wins / (double) child.visits +
                        Math.sqrt(2 * Math.log(visits) / (double) child.visits);

                if (score > maxScore) {
                    selected = child;
                    maxScore = score;
                }
            }

            return selected;
        }

        public Node rollout() {
            Node current = this;

            while (!current.isTerminal()) {
                if (current.isLeaf()) {
                    current.expand();
                }

                current = current.select();
            }

            return current;
        }

        public void update() {
            Node current = this;

            while (current != null) {
                current.visits++;
                current.wins += evaluateBoard(current.state, myIcon);
                current = current.parent;
            }
        }
    }

    private int ROWS;
    private int COLS;
    private Icon myIcon;
    private Icon oppIcon;
    private Icon[][] cells;


    @Override
    public void setIcon(Icon icon) {
        this.myIcon = icon;
        this.oppIcon = (icon == Icon.CROSS) ? Icon.NOUGHT : Icon.CROSS;
    }

    @Override
    public Icon getIcon() {
        return myIcon;
    }

    @Override
    public int move(Board board) {
        // Initialize the search tree with the current game state.
        this.ROWS = board.height;
        this.COLS = board.width;
        this.cells = new Icon[ROWS][COLS];
        for (int i = 0; i < ROWS; i++) {
            cells[i] = board.data[i].clone();
        }

        Node root = new Node(null, null);
        root.expand();
        int iterations = 0;
        long start = System.currentTimeMillis();
        long end = start + 15000;

        while (System.currentTimeMillis() < end) {
            // Select a leaf node in the search tree.
            Node leaf = root.select();

            // If the leaf is null, return -1 to indicate that there are no valid moves.
            if (leaf == null) {
                return -1;
            }

            // If the leaf is a leaf node, expand it by generating all possible children.
            if (leaf.isLeaf()) {
                leaf.expand();
            }

            // Roll out the search tree from the leaf node to a terminal node.
            Node node = leaf.rollout();

            // Update the search tree with the results of the rollout.
            node.update();

            // Increment the number of iterations performed.
            iterations++;
        }

        // Select the child node with the most visits as the best move.
        Node selected = null;
        int maxVisits = Integer.MIN_VALUE;

        for (Node child : root.children) {
            if (child.visits > maxVisits) {
                selected = child;
                maxVisits = child.visits;
            }
        }

        // Return the move corresponding to the selected child node.
        return getMoveIndex(selected.move[0], selected.move[1]);
    }

    private int getMoveIndex(int row, int col) {
        return row * COLS + col;
    }


    private List<int[]> generateMoves(Icon icon) {
        List<int[]> nextMoves = new ArrayList<>(); // allocate List

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (validateMove(row, col, icon)) {
                    nextMoves.add(new int[]{row, col});
                }
            }
        }
        return nextMoves;
    }

    private int evaluateBoard(Icon[][] board, Icon icon) {
        int score = 0;

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (board[i][j] == icon) {
                    score++;
                } else if (board[i][j] == oppIcon) {
                    score--;
                }
            }
        }

        return score;
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

    private void makeMove(int row, int col, Icon icon, Icon[][] cells) {
        String[] directions = {"UpLeft", "Up", "UpRight", "Left", "Right", "BottomLeft", "Bottom", "BottomRight"};

        for (String dir : directions) {
            if (checkDirection(dir, row, col, icon, cells)) {
                flipDirection(dir, row, col, icon, cells);
            }
        }

        cells[row][col] = icon;
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
    boolean checkDirection(String dir, int row, int col, Icon icon, Icon[][] cells) {
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
}

