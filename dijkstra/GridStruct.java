//import java.util.*;

public class GridStruct {

    private Point[][] grid;
    private int rows, cols;

    public GridStruct(int rows, int cols){
        this.rows = rows;
        this.cols = cols;
        this.grid = new Point[rows][cols];
        for (int r=0; r<rows; ++r){
            for (int c=0; c<cols; ++c){
                this.grid[r][c] = new Point(r,c,Point.Value.NORMAL);
            }
        }
    }

    public Point[][] getGrid(){ return this.grid; }
    public Point getPoint(int x, int y){ return this.grid[x][y]; }
    public int getRows(){ return this.rows; }
    public int getCols(){ return this.cols; }
    
}