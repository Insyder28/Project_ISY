package players;

import games.data.Board;
import games.data.Icon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class OthelloMCTS implements Player {
    private static final int WIN_SCORE = 10;
    private Icon myIcon, oppIcon;
    private final int level;
    private int opponent;

    public OthelloMCTS() {
        this.level = 90;
    }

    private int getMillisForCurrentLevel() {
        return this.level * 100;
    }

    @Override
    public int move(Board board) {
        long start = System.currentTimeMillis();
        long end = start + getMillisForCurrentLevel();

        Tree tree = new Tree();
        Node rootNode = tree.getRoot();
        rootNode.getState().setBoard(board);
        rootNode.getState().setPlayer(myIcon);

        if (oppIcon == Icon.NOUGHT) {
            opponent = 2;
        } else {
            opponent = 1;
        }

        while (System.currentTimeMillis() < end) {
            // Phase 1 - Selection
            Node promisingNode = selectPromisingNode(rootNode);
            // Phase 2 - Expansion
            if (promisingNode.getState().getBoard().checkStatus() == Board.IN_PROGRESS)
                expandNode(promisingNode);

            // Phase 3 - Simulation
            Node nodeToExplore = promisingNode;
            if (promisingNode.getChildArray().size() > 0) {
                nodeToExplore = promisingNode.getRandomChildNode();
            }
            int playoutResult = simulateRandomPlayout(nodeToExplore);
            // Phase 4 - Update
            backPropogation(nodeToExplore, playoutResult);
        }
        try {
            Node winnerNode = rootNode.getChildWithMaxScore();
            tree.setRoot(winnerNode);
            int winMove = winnerNode.getState().getMove();
            System.out.println(winMove + " with " + winnerNode.getState().getVisitCount() + " visits");
            return winMove;
        } catch (Exception e) {
            return -9;
        }

    }

    private Node selectPromisingNode(Node rootNode) {
        Node node = rootNode;
        while (node.getChildArray().size() != 0) {
            node = UCT.findBestNodeWithUCT(node);
        }
        return node;
    }

    private void expandNode(Node node) {
        List<State> possibleStates = node.getState().getAllPossibleStates();
        possibleStates.forEach(state -> {
            Node newNode = new Node(state);
            newNode.setParent(node);
            newNode.getState().setPlayer(node.getState().getOpponent());
            node.getChildArray().add(newNode);
        });
    }

    private void backPropogation(Node nodeToExplore, int playerNo) {
        Node tempNode = nodeToExplore;
        Icon player = null;
        while (tempNode != null) {
            tempNode.getState().incrementVisit();
            if (playerNo == 1) 
                player = Icon.CROSS;
            if (playerNo == 2)
                player = Icon.NOUGHT;
            if (tempNode.getState().getPlayer() == player)
                tempNode.getState().addScore(WIN_SCORE);
            tempNode = tempNode.getParent();
        }
    }

    private int simulateRandomPlayout(Node node) {
        Node tempNode = new Node(node);
        State tempState = tempNode.getState();
        int boardStatus = tempState.getBoard().checkStatus();

        if (boardStatus == opponent) {
            tempNode.getParent().getState().setWinScore(Integer.MIN_VALUE);
            return boardStatus;
        }
        while (boardStatus == Board.IN_PROGRESS) {
            tempState.togglePlayer();
            tempState.randomPlay();
            boardStatus = tempState.getBoard().checkStatus();
        }

        return boardStatus;
    }

    @Override
    public Icon getIcon() {
        return myIcon;
    }

    @Override
    public void setIcon(Icon icon) {
        this.myIcon = icon;
        this.oppIcon = (icon == Icon.CROSS) ? Icon.NOUGHT : Icon.CROSS;
    }

}

class UCT {

    public static double uctValue(int totalVisit, double nodeWinScore, int nodeVisit) {
        if (nodeVisit == 0) {
            return Integer.MAX_VALUE;
        }
        return (nodeWinScore / (double) nodeVisit) + 1.41 * Math.sqrt(Math.log(totalVisit) / (double) nodeVisit);
    }

    static Node findBestNodeWithUCT(Node node) {
        int parentVisit = node.getState().getVisitCount();
        return Collections.max(
                node.getChildArray(),
                Comparator.comparing(c -> uctValue(parentVisit, c.getState().getWinScore(), c.getState().getVisitCount())));
    }
}

class Node {
    State state;
    Node parent;
    List<Node> childArray;

    public Node() {
        this.state = new State();
        childArray = new ArrayList<>();
    }

    public Node(State state) {
        this.state = state;
        childArray = new ArrayList<>();
    }

    public Node(Node node) {
        this.childArray = new ArrayList<>();
        this.state = new State(node.getState());
        if (node.getParent() != null)
            this.parent = node.getParent();
        List<Node> childArray = node.getChildArray();
        for (Node child : childArray) {
            this.childArray.add(new Node(child));
        }
    }

    public State getState() {
        return state;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public List<Node> getChildArray() {
        return childArray;
    }

    public Node getRandomChildNode() {
        int noOfPossibleMoves = this.childArray.size();
        int selectRandom = (int) (Math.random() * noOfPossibleMoves);
        return this.childArray.get(selectRandom);
    }

    public Node getChildWithMaxScore() {
        return Collections.max(this.childArray, Comparator.comparing(c -> c.getState().getVisitCount()));
    }
}

class Tree {
    Node root;

    public Tree() {
        root = new Node();
    }

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }

}

class State {
    private Board board;
    private int move;
    private Icon player;
    private int visitCount;
    private double winScore;

    public State() {
        board = new Board(8, 8);
    }

    public State(State state) {
        board = new Board(8, 8);
        for (int row = 0; row < 8; row++) {
            System.arraycopy(state.getBoard().data[row], 0, board.data[row], 0, 8);
        }
        this.player = state.getPlayer();
        this.visitCount = state.getVisitCount();
        this.winScore = state.getWinScore();
    }

    public State(Board board2) {
        this.board = new Board(8, 8);
        for (int row = 0; row < 8; row++) {
            System.arraycopy(board2.data[row], 0, board.data[row], 0, 8);
        }
    }

    public int getMove() {
        return move;
    }

    Board getBoard() {
        return board;
    }

    void setBoard(Board board2) {
        board = new Board(8, 8);
        for (int row = 0; row < 8; row++) {
            System.arraycopy(board2.data[row], 0, this.board.data[row], 0, 8);
        }
    }

    public Icon getPlayer() {
        return player;
    }

    public void setPlayer(Icon player) {
        this.player = player;
    }

    Icon getOpponent() {
        return (player == Icon.CROSS) ? Icon.NOUGHT : Icon.CROSS;
    }

    public int getVisitCount() {
        return visitCount;
    }

    public double getWinScore() {
        return winScore;
    }

    public void setWinScore(double winScore) {
        this.winScore = winScore;
    }

    public List<State> getAllPossibleStates() {
        List<State> possibleStates = new ArrayList<>();
        List<int[]> availableMoves = board.generateMoves(player);
        availableMoves.forEach(p -> {
            State newState = new State(this.board);
            newState.move = p[0] * board.height + p[1];
            newState.setPlayer(getOpponent());
            newState.getBoard().makeMove(p[0], p[1], newState.getPlayer());
            possibleStates.add(newState);
        });
        return possibleStates;
    }

    void incrementVisit() {
        this.visitCount++;
    }

    void addScore(double score) {
        if (this.winScore != Integer.MIN_VALUE)
            this.winScore += score;
    }

    void randomPlay() {
        List<int[]> availableMoves = this.board.generateMoves(player);
        if (availableMoves.isEmpty())
            return;
        int totalPossibilities = availableMoves.size();
        int selectRandom = (int) (Math.random() * totalPossibilities);
        this.board.makeMove(availableMoves.get(selectRandom)[0], availableMoves.get(selectRandom)[1], this.player);
    }

    void togglePlayer() {
        this.player = (player == Icon.CROSS) ? Icon.NOUGHT : Icon.CROSS;
    }
}
