/* *****************************************************************************
 *  Name:              Zijie Xia
 *  Last modified:     January 5, 2022
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private static final double CONFIDENCE_95 = 1.96;
    private final double[] result; // Store the result of each trials
    private final int trialNumber;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException("The argument is outside its prescribed range");
        trialNumber = trials;
        result = new double[trials];
        while (trials > 0) {
            Percolation trial = new Percolation(n);
            while (!trial.percolates()) {
                int openRow = StdRandom.uniform(n) + 1;
                int openCol = StdRandom.uniform(n) + 1;
                if (!trial.isOpen(openRow, openCol)) {
                    trial.open(openRow, openCol);
                }
            }
            result[trials - 1] = (double) trial.numberOfOpenSites() / (n * n);
            trials--;
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(result);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(result);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - (CONFIDENCE_95 * stddev()) / Math.sqrt(trialNumber);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + (CONFIDENCE_95 * stddev()) / Math.sqrt(trialNumber);
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats testClient = new PercolationStats(n, trials);
        StdOut.println("mean = " + testClient.mean());
        StdOut.println("stddev = " + testClient.stddev());
        StdOut.println("95% confidence interval = [" + testClient.confidenceLo() + ", " + testClient
                .confidenceHi() + "]");
    }

}
