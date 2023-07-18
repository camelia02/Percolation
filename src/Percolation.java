import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    
    private int n;
    private int size;
    private int virtualFloor;
    private int virtualCeiling;
    private int openSites;
    private boolean grid[][];
    private WeightedQuickUnionUF unionFind;


    public Percolation(int N){
        this.n = N;
        this.size = N * N;
        this.grid = new boolean[N][N];
        virtualFloor = size+1;
        virtualCeiling = size;

        unionFind = new WeightedQuickUnionUF((N*N) + 2);

        while(N-- > 0){
            unionFind.union(N,virtualCeiling);
            unionFind.union(N, virtualFloor);
        }

        this.openSites = 0;
    }

    // opens the site (row, col) if it is not open already
    //Rows and columns are indexed from 1 to N, where N is the size of the grid in both dimensions.
    public void open(int row, int col){
        indexOutOfBounds(row, col);

        //open grid
        grid[row - 1][col - 1] = true;

        int siteIndex = convertTo1D(row, col);

        //check if site to the left is open
        if(col > 1 && isOpen(row, col - 1)) unionFind.union(siteIndex, convertTo1D(row, col - 1)); 

        //check if site to the right is open
        if(col < this.n && isOpen(row, col + 1)) unionFind.union(siteIndex, convertTo1D(row, col + 1)); 

        //check if site on top is open
        if(row > 1 && isOpen(row - 1, col)) unionFind.union(siteIndex, convertTo1D(row - 1, col));

        //check if site below is open
        if(row < this.n && isOpen(row + 1, col)) unionFind.union(siteIndex, convertTo1D(row + 1, col)); 

        //connect with virtual top if site is on top row
        if(row == 1) unionFind.union(this.virtualCeiling, siteIndex); 

        //connect with virtual flood if site is on bottom row
        if(row == this.n) unionFind.union(this.virtualFloor, siteIndex); 

        this.openSites++;
    }

    // is the site (row, col) open?
    //Open = true, Block = false
    public boolean isOpen(int row, int col){
        indexOutOfBounds(row, col);
        return this.grid[row - 1][col - 1];
    }

    // is the site (row, col) full?
    //A full site is an open site that can be connected to an open site in the top row via a chain of neighboring (left, right, up, down) open sites. 
    public boolean isFull(int row, int col){
        indexOutOfBounds(row, col);
        return isOpen(row, col) && unionFind.find(virtualCeiling) == unionFind.find(convertTo1D(row, col)); 
    }

    // returns the number of open sites
    public int numberOfOpenSites(){
        return this.openSites;
    }

    // does the system percolate?
    public boolean percolates(){
        return unionFind.find(virtualCeiling) == unionFind.find(virtualFloor);
    }

    private void indexOutOfBounds(int i, int j){
        if(i < 0 || j < 0 || i > this.n - 1 || j > this.n - 1){
            throw new IndexOutOfBoundsException("Index out of bounds!");
        }
    }

    private int convertTo1D(int row, int col) {
        return ((row - 1) * size) + (col - 1);
    }
    
}
    

