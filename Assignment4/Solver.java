/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.LinkedList;

public class Solver {

    private class GameTreeNode implements Comparable<GameTreeNode> {
        private final GameTreeNode prev;
        private final Board board;
        private final int moves;
        private final int priority;

        public GameTreeNode(Board currNode, int moves) {
            this.board = currNode;
            this.prev = null;
            this.moves = moves;
            this.priority = currNode.manhattan() + this.moves;
        }

        public GameTreeNode(Board currNode, GameTreeNode prevNode) {
            this.board = currNode;
            this.prev = prevNode;
            this.moves = prevNode.getMoves() + 1;
            this.priority = currNode.manhattan() + this.moves;
        }

        public int getPriority() {
            return this.priority;
        }

        public Board getBoard() {
            return this.board;
        }

        public GameTreeNode getPrevNode() {
            return this.prev;
        }

        public int getMoves() {
            return this.moves;
        }

        public int compareTo(GameTreeNode otherNode) {
            return Integer.compare(this.getPriority(), otherNode.getPriority());
        }

    }

    private boolean isSolvable;
    private int numberOfMoves;
    private GameTreeNode resultNode;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }
        MinPQ<GameTreeNode> gameTree = new MinPQ<>();
        GameTreeNode root = new GameTreeNode(initial, 0);
        gameTree.insert(root);
        while (!gameTree.isEmpty()) {
            GameTreeNode node = gameTree.delMin();
            if (node.getBoard().isGoal()) {
                this.isSolvable = true;
                this.numberOfMoves = node.getMoves();
                this.resultNode = node;
                break;
            }
            else if (node.getBoard().manhattan() == 2 && node.getBoard().twin().isGoal()) {
                this.isSolvable = false;
                break;
            }
            for (Board board : node.getBoard().neighbors()) {
                if (node.getPrevNode() == null || !board.equals(node.getPrevNode().getBoard())) {
                    GameTreeNode tempNode = new GameTreeNode(board, node);
                    gameTree.insert(tempNode);
                }
            }
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return this.isSolvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return isSolvable() ? this.numberOfMoves : -1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) {
            return null;
        }
        LinkedList<Board> result = new LinkedList<>();
        GameTreeNode node = this.resultNode;
        while (node != null) {
            result.addFirst(node.getBoard());
            node = node.getPrevNode();
        }
        return result;
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);
        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        StdOut.println(solver.moves());
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
