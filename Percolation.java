import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[] matrix;
    private WeightedQuickUnionUF tree;
    private WeightedQuickUnionUF water;
    private int n;
    private int openings;
    private int topIndex;
    private int bottomIndex;

    public Percolation(int n) {

        if (n < 1) {
            throw new IllegalArgumentException("N has to be greater than 0");
        }

        this.matrix = new boolean[n * n];
        this.tree = new WeightedQuickUnionUF((n * n) + 2);
        this.water = new WeightedQuickUnionUF((n * n) + 1);

        this.topIndex = (n * n) + 1;
        this.bottomIndex = n * n;
        for (int i = 0; i < n * n; i++)
            matrix[i] = false;

        this.n = n;
        this.openings = 0;
    }

    /* Check if they are connected */
    private boolean connected(int p, int q) {
        return tree.find(p) == tree.find(q);
    }

    /* Get the coordinates of the psuedo 2d array */
    private int flatt(int row, int col) {
        return (row - 1) * this.n + col - 1;

    }

    /* Validate the boundary limits */
    private void checkBounds(int row, int col) {
        if (row < 1 || row > this.n || col < 1 || col > this.n) {
            throw new IllegalArgumentException("Values Are Out Of Bounds");
        }
    }

    public void open(int row, int col) {

        checkBounds(row, col);

        int index = flatt(row, col);
        if (!isOpen(row, col)) {
            this.matrix[index] = true;
            this.openings++;

            // Check Above
            if (row > 1 && isOpen(row - 1, col)) {
                tree.union(index, index - n);
                water.union(index, index - n);
            }

            // Check Below
            if (row < n && isOpen(row + 1, col)) {
                tree.union(index, index + n);
                water.union(index, index + n);
            }

            // Check Left
            if (col > 1 && isOpen(row, col - 1)) {
                tree.union(index, index - 1);
                water.union(index, index - 1);
            }

            // Check Right
            if (col < n && isOpen(row, col + 1)) {
                tree.union(index, index + 1);
                water.union(index, index + 1);
            }

            /* Connects top to reservoir */
            if (row == 1) {
                tree.union(index, this.topIndex);
                water.union(index, this.topIndex - 1);
            }

            /* Connects bottom to sink */
            if (row == this.n) {
                tree.union(index, this.bottomIndex);
            }
        }

    }


    public boolean isOpen(int row, int col) {
        checkBounds(row, col);
        return matrix[flatt(row, col)];
    }


    public boolean isFull(int row, int col) {
        checkBounds(row, col);
        return water.find(this.topIndex - 1) == water.find(flatt(row, col));
    }

    public boolean percolates() {
        return connected(this.bottomIndex, this.topIndex);

    }

    public int numberOfOpenSites() {
        return this.openings;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int row, col;
        Percolation perc = new Percolation(n);
        while (!in.isEmpty()) {
            row = in.readInt();
            col = in.readInt();
            perc.open(row, col);
        }
    }
}
