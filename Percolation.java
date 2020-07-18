import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private WeightedQuickUnionUF grid;
    private WeightedQuickUnionUF full;
    private int N;
    private int virtualTop;
    private int virtualBottom;
    private int openSitesCount;
    private boolean[] openSites;

    public Percolation(int N) {
        if (N <= 0) throw new IllegalArgumentException("N must be > 0");

        grid = new WeightedQuickUnionUF(N * N + 2);
        full = new WeightedQuickUnionUF(N * N + 1);
        this.N = N;
        virtualTop = getSingleArrayIndex(N, N) + 1;
        virtualBottom = getSingleArrayIndex(N, N) + 2;
        openSites = new boolean[N * N];
        openSitesCount = 0;
    }

    private int getSingleArrayIndex(int i, int j) {
        return (N * (i - 1) + j) - 1;
    }

    private boolean checkPositionInput(int i, int j) {
        return i > 0
                && j > 0
                && i <= N
                && j <= N;
    }

    private void isPositionValid(int i, int j) {
        if (!checkPositionInput(i, j))
            throw new IndexOutOfBoundsException("Inputs are out of bound");
    }

    public void open(int i, int j) {
        if (isOpen(i, j)) {
            return;
        }

        int site = getSingleArrayIndex(i, j);

        openSites[site] = true;
        openSitesCount += 1;

        // site is in top row, connect site to virtualTop in 'grid' and 'full'
        if (i == 1) {
            grid.union(virtualTop, site);
            full.union(virtualTop, site);
        }

        // site is in bottom row, connect site to virtualBottom in only 'grid' to prevent backwash
        if (i == N) {
            grid.union(virtualBottom, site);
        }

        // union with the site above the given site if that site is open
        if (checkPositionInput(i - 1, j) && isOpen(i - 1, j)) {
            grid.union(getSingleArrayIndex(i - 1, j), site);
            full.union(getSingleArrayIndex(i - 1, j), site);
        }

        // union with the site to the right of the given site if that site is open
        if (checkPositionInput(i, j + 1) && isOpen(i, j + 1)) {
            grid.union(getSingleArrayIndex(i, j + 1), site);
            full.union(getSingleArrayIndex(i, j + 1), site);
        }

        // union with the site to the bottom of the given site if that site is open
        if (checkPositionInput(i + 1, j) && isOpen(i + 1, j)) {
            grid.union(getSingleArrayIndex(i + 1, j), site);
            full.union(getSingleArrayIndex(i + 1, j), site);
        }

        // union with the site to the left of the given site if that site is open
        if (checkPositionInput(i, j - 1) && isOpen(i, j - 1)) {
            grid.union(getSingleArrayIndex(i, j - 1), site);
            full.union(getSingleArrayIndex(i, j - 1), site);
        }
    }

    public boolean isOpen(int i, int j) {
        return openSites[getSingleArrayIndex(i, j)];

    }

    public boolean isFull(int i, int j) {
        isPositionValid(i, j);
        int site = getSingleArrayIndex(i, j);
        return full.connected(site, virtualTop);
    }

    public int numberOfOpenSites() {
        return openSitesCount;

    }

    public boolean percolates() {
        return grid.connected(virtualTop, virtualBottom);

    }

    public static void main(String[] args) {
        int n = StdIn.readInt();
        Percolation percolation = new Percolation(n);

        while (!StdIn.isEmpty()) {
            int i = StdIn.readInt();
            int j = StdIn.readInt();

            percolation.open(i, j);
            if (percolation.percolates()) {
                StdOut.printf("%nSystem percolates %n");
            } else {
                StdOut.printf("%nSystem does not percolate %n");
            }
        }
    }
}