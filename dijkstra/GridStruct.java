import java.util.*;

public class GridStruct {

    private Node[][] grid;
    private int rows, cols;
    private Node currentStart;
    private Node currentFinish;
    private ArrayList<Node> currentWalls;

    public GridStruct(int rows, int cols){
        this.rows = rows;
        this.cols = cols;
        this.grid = new Node[rows][cols];
        for (int r=0; r<rows; ++r){
            for (int c=0; c<cols; ++c){
                this.grid[r][c] = new Node(r,c,Node.Value.NORMAL);
            }
        }
        // Aggiungo ad ogni nodo i suoi vicini
        for (int r=0; r<rows; ++r){
            for (int c=0; c<cols; ++c){

                Node n = this.grid[r][c];
                ArrayList<Node> adj = new ArrayList<Node>();

                // Nodo sopra
                if (r!=0)
                    adj.add(this.grid[r-1][c]);
                // Nodo sotto
                if (r!=rows-1)
                    adj.add(this.grid[r+1][c]);
                // Nodo a sinistra
                if (c!=0)
                    adj.add(this.grid[r][c-1]);
                // Nodo a destra
                if (c!=cols-1)
                    adj.add(this.grid[r][c+1]);
                // Nodo in alto a sinistra
                if (r!=0 && c!=0)
                    adj.add(this.grid[r-1][c-1]);
                // Nodo in alto a destra
                if (r!=0 && c!=cols-1)
                    adj.add(this.grid[r-1][c+1]);
                // Nodo in basso a sinistra
                if (r!=rows-1 && c!=0)
                    adj.add(this.grid[r+1][c-1]);
                // Nodo in basso a destra
                if (r!=rows-1 && c!=cols-1)
                    adj.add(this.grid[r+1][c+1]);

                for (Node a : adj)
                    n.addAdj(a);
                
            }
        }
        this.currentWalls = new ArrayList<Node>();
    }

    public Node[][] getGrid(){ return this.grid; }

    public Node getNode(int x, int y){ return this.grid[x][y]; }

    public int getRows(){ return this.rows; }

    public int getCols(){ return this.cols; }

    public void setStart(int x, int y){
        Node n = grid[x][y];
        n.setStart();
        currentStart = n;
    }

    public void setFinish(int x, int y){
        Node n = grid[x][y];
        n.setFinish();
        currentFinish = n;
    }

    public void setWall(int x, int y){
        Node n = grid[x][y];
        n.setWall();
        currentWalls.add(n);
    }


    public Node resetStart(){
        if (currentStart == null) return null;
        Node ret = currentStart;
        currentStart.setNormal();
        currentStart = null;
        return ret;
    }

    public Node resetFinish(){
        if (currentFinish == null) return null;
        Node ret = currentFinish;
        currentFinish.setNormal();
        currentFinish = null;
        return ret;
    }

    public ArrayList<Node> resetWalls(){
        ArrayList<Node> ret = new ArrayList<Node>();
        for (Node w : currentWalls){
            ret.add(w);
        }
        for (Node n : currentWalls){
            n.setNormal();
        }
        currentWalls.clear();
        return ret;
    }

    public void resetAllDist(){
        for (int r=0; r<this.rows; ++r){
            for (int c=0; c<this.cols; ++c){
                Node n = this.grid[r][c];
                if (n.getValue() != Node.Value.START) n.setDist(Integer.MAX_VALUE);
                else n.setDist(0);
            }
        }
    }
    
}