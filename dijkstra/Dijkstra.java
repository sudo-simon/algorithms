import java.io.*;
import javax.swing.*;
import java.util.*;
import java.lang.Thread;

public class Dijkstra {

    private static GridFrame gridFrame;
    //private static GridFrame.DrawCanvas canvas;
    private static GridStruct gridStruct;
    private static Point[][] grid;

    private Comparator<Point> pointComp = new Comparator<Point>() {
        public int compare(Point p1, Point p2) {
            if (p1.getDist() < p2.getDist()) return -1;
            if (p1.getDist() == p2.getDist()) return 0;
            else return 1;
        };
    };
    private PriorityQueue<Point> PQ;
    private LinkedList<Point> shortestPath;

    //private static void printErr(){ System.out.println("Passare gli argomenti: righe (int) colonne (int) walls_file (path, optional)"); }

    public static void main(String[] args){

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run(){
                gridFrame = new GridFrame();
                //canvas = gridFrame.getCanvas();

                gridStruct = gridFrame.getGridStruct();
                grid = gridStruct.getGrid();
            }
        });

        try {
            Thread.sleep(4000);
            Point p = grid[10][10];
            p.setValue(Point.Value.START);
            gridFrame.updatePointValue(p);
        } catch(InterruptedException e){
            e.printStackTrace();
            return;
        }

    }

    /*    if (args.length < 2){
            printErr();
            return;
        }

        int rows, cols;
        //File wallsPath;

        try{
            rows = Integer.parseInt(args[0]);
            cols = Integer.parseInt(args[1]);
        } catch (NumberFormatException e){
            e.printStackTrace();
            return;
        }

        if (args.length > 2){
            System.out.println("Gestione del percorso del file contenente i muri (generatore random?)");
        }

        Grid gridObj = new Grid(rows,cols);
        gridObj.draw();
        return;

    }
    */

}

