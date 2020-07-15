/*
* The QuickUnionUF class represents a union-find data type
* also known as disjoint-sets data type.

* It supports find and union operations along with a count 
* operation that returns the number of sets.

* The union-find data type models a collection of sets 
* containing n elements with each element in exactly one set.

* The elements are named 0 to n-1.

* Initially, there are n sets with each element in its own 
* set. The root of the set (where all elements root to) 
* is the distinguished element. Here is the summary of the 
* operations:

* Find operation returns the root of the set. The find 
* operation returns same value for two elements if and
* only if they belong to the same set.

* Union operation merges the set containing element p 
* with the set containing element q. That is, if p and q
* are in different sets, replace these two sets with a new
* set that is the union of the two.

* Count returns the total number of sets.

*******************************************************************/

/*
* This implementation use QUICK UNION
* The constructor takes n time, where n is the number of sites
* The union and find takes n times in the worst case
* The count operation takes constant 1 time

*******************************************************************/

public class WeightedQuickUnionUF {

    private int[] parent;
    private int count;
    private size[]

    public QuickUnionUF(int n) {
        parent = new int[n];
        count = n;

        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }
    }

    public int count() {
        return count;
    }

    public int find(int p) {
        while (p != parent[p]) 
            p = parent[p];       
        return p;
    }

    public boolean connected(int p, int q) {
        return find(p) == find(q);
    }

    public void union(int p, int q) {
        int rootP = find(p);
        int rootQ = find(q);

        if (rootP == rootQ) return;
        parent[rootP] = rootQ;
        count--;
    }

    public static void main(String[] args) {
        int n = StdIn.readInt();
        QuickUnionUF uf = new QuickUnionUF(n);

        while(!StdIn.isEmpty()) {
            int p = StdIn.readInt();
            int q = StdIn.readInt();
            if(uf.connected(p, q)) return;
            uf.union(p, q);
            StdOut.println(p + " " + q);
        }

        StdOut.println(uf.count() + " components");
    }
    
}