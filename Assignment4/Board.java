/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.LinkedList;
import java.util.List;

public class Board {

    private final int[][] tiles;
    private final int dim;
    private int zeroRow;
    private int zeroCol;
    private int hammingDistance = -1;
    private int manhattanDistance = -1;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.dim = tiles.length;
        this.tiles = new int[dim][dim];
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                this.tiles[i][j] = tiles[i][j];
                if (tiles[i][j] == 0) {
                    zeroRow = i;
                    zeroCol = j;
                }
            }
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(dimension());
        result.append("\n");
        for (int i = 0; i < dim; i++) {
            result.append(" ");
            for (int j = 0; j < dim; j++) {
                result.append(tiles[i][j]);
                result.append(" ");
            }
            result.append("\n");
        }
        return result.toString();
    }

    // board dimension n
    public int dimension() {
        return dim;
    }

    // number of tiles out of place
    public int hamming() {
        if (hammingDistance != -1) {
            return hammingDistance;
        }
        int distance = 0;
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (tiles[i][j] != 0) {
                    int number = tiles[i][j] - 1;
                    int row = number / dim;
                    int col = number % dim;
                    int temp = Math.abs(row - i) + Math.abs(col - j);
                    if (temp > 0) {
                        distance++;
                    }
                }
            }
        }
        hammingDistance = distance;
        return hammingDistance;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        if (manhattanDistance != -1) {
            return manhattanDistance;
        }
        int distance = 0;
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                int number = tiles[i][j];
                if (number != 0) {
                    number--;
                    int row = number / dim;
                    int col = number % dim;
                    distance += Math.abs(row - i) + Math.abs(col - j);
                }
            }
        }
        manhattanDistance = distance;
        return manhattanDistance;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) {
            return true;
        }
        if (y == null) {
            return false;
        }
        if (this.getClass() != y.getClass()) {
            return false;
        }
        Board that = (Board) y;
        if (that.dimension() != this.dim) {
            return false;
        }
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (that.tiles[i][j] != this.tiles[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        List<Board> neighbors = new LinkedList<>();
        if (zeroRow - 1 >= 0) {
            int[][] newTiles = newTiles(zeroRow - 1, zeroCol, zeroRow, zeroCol);
            neighbors.add(new Board(newTiles));
        }
        if (zeroRow + 1 < dim) {
            int[][] newTiles = newTiles(zeroRow + 1, zeroCol, zeroRow, zeroCol);
            neighbors.add(new Board(newTiles));
        }
        if (zeroCol - 1 >= 0) {
            int[][] newTiles = newTiles(zeroRow, zeroCol - 1, zeroRow, zeroCol);
            neighbors.add(new Board(newTiles));
        }
        if (zeroCol + 1 < dim) {
            int[][] newTiles = newTiles(zeroRow, zeroCol + 1, zeroRow, zeroCol);
            neighbors.add(new Board(newTiles));
        }
        return neighbors;
    }


    private int[][] newTiles(int row1, int col1, int row2, int col2) {
        int[][] newTiles = new int[dim][dim];
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                newTiles[i][j] = tiles[i][j];
            }
        }
        int tmp = newTiles[row2][col2];
        newTiles[row2][col2] = newTiles[row1][col1];
        newTiles[row1][col1] = tmp;
        return newTiles;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        if (zeroRow == 0) {
            return new Board(newTiles(1, 1, 1, 0));
        }
        else {
            return new Board(newTiles(0, 1, 0, 0));
        }
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        // int[][] tiles = new int[][] { { 8, 1, 3 }, { 4, 0, 2 }, { 7, 6, 5 } };
        // Board testBoard = new Board(tiles);
        // StdOut.println(testBoard.toString());
        // StdOut.println(testBoard.hamming());
        // StdOut.println(testBoard.manhattan());
        // StdOut.println(testBoard.isGoal());
        // Board testGoal = new Board(new int[][] { { 1, 2 }, { 3, 0 } });
        // StdOut.println(testGoal.isGoal());
        // StdOut.println(testBoard.dimension());
        // int[][] tiles2 = new int[][] { { 1, 0, 3 }, { 4, 2, 5 }, { 7, 8, 6 } };
        // Board testNeighbor = new Board(tiles2);
        // for (Board board : testNeighbor.neighbors()) {
        //     StdOut.println(board.toString());
        // }
        // StdOut.println(testNeighbor.twin());
    }
}
