/**
 * Given a composite systems comprised of randomly distributed insulating and metallic materials:
 * what fraction of the materials need to be metallic so that the composite system is an electrical
 * conductor? Given a porous landscape with water on the surface (or oil below), under what conditions
 * will the water be able to drain through to the bottom (or the oil to gush through to the surface)?
 * Scientists have defined an abstract process known as percolation to model such situations.
 */

package AlgorithmCourse;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * Build a N*N sized WeightedQuickUnionUF grid to simulate a simple percolation system.
 * The system is considered percolate when there is a connection of open sites from the top
 * row to bottom row.
 * <p>
 * Connections are managed by union-find WeightedQuickUnionUF data structure.
 * Whether a site is opened or not is kept in an array.
 * <p>
 * A second WeightedQuickUnionUF is maintained to avoid backwash problem.
 */

public class Percolation {
    private WeightedQuickUnionUF grid;
    private WeightedQuickUnionUF full;
    private int N;
    private int virtualTop;
    private int virtualBottom;
    private int openSitesCount;
    private boolean[] openSites;

    /**
     * Initialise an N * N grid plus two extra virtual sites. This two sites will act as virtual
     * roots in the top and bottom of the grid.
     * <p>
     * Also initialise another N * N grid plus one top virtual site. This grid is kept to compare
     * with the first grid for fullness and backwash issue.
     *
     * @param N grid dimension
     */
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

    /**
     * When given the column and row coordinates of the 2D array, convert them to
     * an corresponding index of a 1D array with continuously ascending indexes.
     *
     * @param i site row index
     * @param j site column index
     * @return
     */
    private int getSingleArrayIndex(int i, int j) {
        return (N * (i - 1) + j) - 1;
    }

    /**
     * Check if the given pair of row and column indexes are valid.
     * A pair is valid when both row and column indexes are greater then 0 and
     * smaller then the dimension of the grid.
     *
     * @param i site row index
     * @param j site column index
     * @return
     */
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

    /**
     * When a site's coordinates are given in from StandardInput, mark it as opened (if it is not
     * already is).
     * <p>
     * Once a site is opened, check if whether there is an adjacent open site (from top, right, bottom, left)
     * to union with these sites.
     *
     * @param i site row index
     * @param j site column index
     */
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

    /**
     * Check if a site is open via openSites array
     *
     * @param i
     * @param j
     * @return
     */
    public boolean isOpen(int i, int j) {
        return openSites[getSingleArrayIndex(i, j)];

    }

    /**
     * Check if a site is full.
     * A site is considered full when it is connected to the virtual top
     * with a connection of open sites.
     * This is check against the 'full' QuickWeightedUnionUF grid which is not
     * connected to the virtual bottom to avoid backwash issue.
     *
     * @param i
     * @param j
     * @return
     */
    public boolean isFull(int i, int j) {
        isPositionValid(i, j);
        int site = getSingleArrayIndex(i, j);
        return full.connected(site, virtualTop);
    }

    public int numberOfOpenSites() {
        return openSitesCount;

    }

    /**
     * Check if the system percolates.
     * It percolates when the virtualTop is connected with virtualBottom
     *
     * @return
     */
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