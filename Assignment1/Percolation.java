/* *****************************************************************************
 *  Name:              Zijie Xia
 *  Last modified:     January 5, 2022
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    // creates n-by-n grid, with all sites initially blocked
    private static final byte OPENCODE = 0b100;
    private static final byte CONNECT_TOP = 0b10;
    private static final byte CONNECT_BOT = 0b1;
    private static final byte PERCOLATED = 0b111;
    private final byte[] status;
            // 1st bit open status, 2nd bit connect to top status, 3rd bit connect to bot status
    private final WeightedQuickUnionUF sites; // create a Union-Find data type to describe the sites
    private final int size; // size of the sites
    private boolean isPercolated = false; // percolated ? true : false
    private int openNumber = 0;


    public Percolation(int n) {
        size = n;
        if (n <= 0) {
            throw new IllegalArgumentException("The argument is outside its prescribed range");
        }
        status = new byte[n * n];
        sites = new WeightedQuickUnionUF(n * n);
    }

    // convert 2D index to 1D index
    private int convert2Dto1D(int row, int col) {
        return (row - 1) * size + col - 1;
    }

    // Union two open sites and update the status
    private void unionStatus(int index1, int index2) {
        int rootSite1 = sites.find(index1);
        int rootSite2 = sites.find(index2);
        sites.union(index1, index2);
        int newRootSite = sites.find(index1);
        status[newRootSite] |= status[rootSite1] | status[rootSite2];
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row > size || row <= 0 || col > size || col <= 0)
            throw new IllegalArgumentException("The value of rows/columns is out of range");
        if (isOpen(row, col)) return;
        int index = convert2Dto1D(row, col);
        openNumber++;
        status[index] |= OPENCODE;
        if (size == 1) {
            status[index] |= PERCOLATED;
            isPercolated = true;
            return;
        }
        if (row == 1) {
            status[index] |= CONNECT_TOP;
            if (isOpen(row + 1, col)) {
                int botIndex = convert2Dto1D(row + 1, col);
                unionStatus(index, botIndex);
            }
        }
        else if (row == size) {
            status[index] |= CONNECT_BOT;
            if (isOpen(row - 1, col)) {
                int topIndex = convert2Dto1D(row - 1, col);
                unionStatus(index, topIndex);
            }
        }
        else {
            if (isOpen(row - 1, col)) {
                int topIndex = convert2Dto1D(row - 1, col);
                unionStatus(index, topIndex);
            }
            if (isOpen(row + 1, col)) {
                int botIndex = convert2Dto1D(row + 1, col);
                unionStatus(index, botIndex);
            }
        }
        if (col == 1) {
            if (isOpen(row, col + 1)) {
                int rightIndex = convert2Dto1D(row, col + 1);
                unionStatus(index, rightIndex);
            }
        }
        else if (col == size) {
            if (isOpen(row, col - 1)) {
                int leftIndex = convert2Dto1D(row, col - 1);
                unionStatus(index, leftIndex);
            }
        }
        else {
            if (isOpen(row, col + 1)) {
                int rightIndex = convert2Dto1D(row, col + 1);
                unionStatus(index, rightIndex);
            }
            if (isOpen(row, col - 1)) {
                int leftIndex = convert2Dto1D(row, col - 1);
                unionStatus(index, leftIndex);
            }
        }
        int newRootSite = sites.find(index);
        isPercolated |= status[newRootSite] == PERCOLATED;
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row > size || row <= 0 || col > size || col <= 0)
            throw new IllegalArgumentException("The value of rows or columns is out of range");
        int index = convert2Dto1D(row, col);
        return (status[index] & OPENCODE) == OPENCODE;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row > size || row <= 0 || col > size || col <= 0)
            throw new IllegalArgumentException("The value of rows/columns is out of range");
        int index = convert2Dto1D(row, col);
        int rootSite = sites.find(index);
        return (status[rootSite] & CONNECT_TOP) == CONNECT_TOP;
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openNumber;
    }

    // does the system percolate?
    public boolean percolates() {
        return isPercolated;
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation testClient = new Percolation(3);
        testClient.open(1, 1);
        if (testClient.isOpen(1, 1)) StdOut.println("First Test Passed");
        else StdOut.println("First Test Failed");
        testClient.open(2, 1);
        if (testClient.isFull(1, 1)) StdOut.println("Second Test Passed");
        else StdOut.println("Second Test Failed");
        testClient.open(3, 1);
        if (testClient.percolates()) StdOut.println("Third Test Passed");
        else StdOut.println("Third Test Failed");
        if (testClient.numberOfOpenSites() == 3) StdOut.println("Fourth Test Passed");
        else StdOut.println("Fourth Test Failed");
    }
}
