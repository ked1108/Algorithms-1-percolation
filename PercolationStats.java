/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private int tests;
    private double[] fracts;

    private static final double CONFIDENCE_95 = 1.96;

    public PercolationStats(int n, int t) {
        if (n <= 0 || t <= 0) {
            throw new IllegalArgumentException("N or T is less than zero");
        }
        tests = t;
        fracts = new double[tests];
        for (int k = 0; k < tests; k++) {
            Percolation perc = new Percolation(n);
            int openings = 0;
            while (!perc.percolates()) {
                int i = StdRandom.uniformInt(1, n + 1);
                int j = StdRandom.uniformInt(1, n + 1);
                if (!perc.isOpen(i, j)) {
                    perc.open(i, j);
                    openings++;
                }
            }
            fracts[k] = (double) openings / (n * n);
        }
    }

    public double mean() {
        return StdStats.mean(fracts);
    }

    public double stddev() {
        return StdStats.stddev(fracts);
    }

    public double confidenceLo() {
        return mean() - ((CONFIDENCE_95 * stddev()) / Math.sqrt(tests));
    }

    public double confidenceHi() {
        return mean() + ((CONFIDENCE_95 * stddev()) / Math.sqrt(tests));
    }

    public static void main(String[] args) {
        if (args.length >= 2) {
            int n = Integer.parseInt(args[0]);
            int t = Integer.parseInt(args[1]);
            PercolationStats ps = new PercolationStats(n, t);
            String confidence = "[" + ps.confidenceLo() + ", " + ps.confidenceHi() + "]";
            StdOut.println("mean                    = " + ps.mean());
            StdOut.println("stddev                  = " + ps.stddev());
            StdOut.println("95% confidence interval = " + confidence);
        }

    }
}
