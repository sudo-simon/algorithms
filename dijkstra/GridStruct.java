package dijkstra;

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

                // Nodo sopra
                if (r!=0)
                    n.addAdj(this.grid[r-1][c]);
                // Nodo sotto
                if (r!=rows-1)
                    n.addAdj(this.grid[r+1][c]);
                // Nodo a sinistra
                if (c!=0)
                    n.addAdj(this.grid[r][c-1]);
                // Nodo a destra
                if (c!=cols-1)
                    n.addAdj(this.grid[r][c+1]);
                
                // Decomment to allow diagonal traveling
                /*
                // Nodo in alto a sinistra
                if (r!=0 && c!=0)
                    n.addAdj(this.grid[r-1][c-1]);
                // Nodo in alto a destra
                if (r!=0 && c!=cols-1)
                    n.addAdj(this.grid[r-1][c+1]);
                // Nodo in basso a sinistra
                if (r!=rows-1 && c!=0)
                    n.addAdj(this.grid[r+1][c-1]);
                // Nodo in basso a destra
                if (r!=rows-1 && c!=cols-1)
                    n.addAdj(this.grid[r+1][c+1]);
                */
                
            }
        }
        this.currentWalls = new ArrayList<Node>();
    }

    public Node[][] getGrid(){ return this.grid; }

    public Node getNode(int x, int y){ return this.grid[x][y]; }

    public int getRows(){ return this.rows; }

    public int getCols(){ return this.cols; }

    public Node getStart(){ return this.currentStart; }

    public Node getFinish(){ return this.currentFinish; }

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
        if (! currentWalls.contains(n))
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

    public void resetAll(){
        for (int r=0; r<this.rows; ++r){
            for (int c=0; c<this.cols; ++c){
                Node n = this.grid[r][c];
                n.setState(Node.State.UNEXPLORED);
                if (n.getValue() != Node.Value.START)
                    n.setDist(Node.MAX_DIST);
                else
                    n.setDist(0);
            }
        }
    }
    
}