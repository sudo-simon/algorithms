import java.io.*;
import javax.swing.*;
import java.util.*;
import java.lang.Thread;

public class Dijkstra {

    private static GridFrame gridFrame;
    private static GridStruct gridStruct;
    private static Node[][] grid;



    private static Comparator<Node> nodeComp = new Comparator<Node>() {
        public int compare(Node n1, Node n2) {
            if (n1.getDist() < n2.getDist()) return -1;
            if (n1.getDist() == n2.getDist()) return 0;
            else return 1;
        };
    };
    private static PriorityQueue<Node> PQ;
    private static LinkedList<Node> shortestPath;

    // The core Dijkstra's algorithm
    public static void algorithm(){
        PQ = new PriorityQueue<Node>(nodeComp);
        //TODO here
        reverseWalk();
    }

    private static void reverseWalk(){
        shortestPath = new LinkedList<Node>();
        //TODO here
    }

    // AUX func
    public static void run(){
        algorithm();
        gridFrame.drawShortestPath(shortestPath);
    }




    //private static void printErr(){ System.out.println("Passare gli argomenti: righe (int) colonne (int) walls_file (path, optional)"); }

    public static void main(String[] args){

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run(){
                gridFrame = new GridFrame();
                gridStruct = gridFrame.getGridStruct();
                grid = gridStruct.getGrid();
            }
        });

        try {
            Thread.sleep(2000);
            Node n1 = grid[10][10];
            Node n2 = grid[40][40];
            gridStruct.setStart(10,10);
            gridFrame.updateNodeValue(n1);
            gridStruct.setFinish(40,40);
            gridFrame.updateNodeValue(n2);

            Thread.sleep(2000);
            Node prev_start = gridStruct.resetStart();
            Node prev_finish = gridStruct.resetFinish();
            gridFrame.updateNodeValue(prev_start);
            gridFrame.updateNodeValue(prev_finish);
        } catch(InterruptedException e){
            e.printStackTrace();
            return;
        }

    }

}

