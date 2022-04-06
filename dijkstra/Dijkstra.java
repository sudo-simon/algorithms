package dijkstra;

import Structs.Heap;

import java.io.*;
import javax.swing.*;
import java.util.*;
import java.lang.Thread;

public class Dijkstra {

    private static GridFrame gridFrame;
    private static GridStruct gridStruct;
    private static Node[][] grid;


    //AUX for sleep()
    public static final long DELAY_MS = 0L;
    public static void sleep(long millis){
        try{
            Thread.sleep(millis);
        } catch(InterruptedException e){
            e.printStackTrace();
            System.exit(-1);
        }
    }


    /*private static Comparator<Node> nodeComp = new Comparator<Node>() {
        public int compare(Node n1, Node n2) {
            return n1.compareTo(n2);
        };
    };*/
    //!private static PriorityQueue<Node> PQ;
    private static Heap<Node> heap;
    private static LinkedList<Node> shortestPath;

    // The core Dijkstra's algorithm
    public static void algorithm(){

        Node start = gridStruct.getStart();
        Node finish = gridStruct.getFinish();
        if (start == null || finish == null) return;

        // PQ initialization with D[s]
        //!PQ = new PriorityQueue<Node>();
        heap = new Heap<Node>(Heap.Type.MINIMAL);
        for (int r=0; r<gridStruct.getRows(); ++r){
            for (int c=0; c<gridStruct.getCols(); ++c){
                Node n = grid[r][c];
                if (n.getValue() != Node.Value.WALL)
                    //!PQ.add(n);
                    heap.add(n);
            }
        }

        while (heap.size() > 0){
            Node n = heap.removeMin();
            n.setState(Node.State.EXPLORING);
            gridFrame.updateNodeState(n);
            //? DELAY
            if (DELAY_MS > 0) sleep(DELAY_MS);
            for (Node a : n.getAdj()){
                if (heap.contains(a)){
                    a.setState(Node.State.EXPLORING);
                    gridFrame.updateNodeState(a);
                    // Rilassamento
                    int d = n.getDist();
                    if (d + 1 < a.getDist())
                        a.setDist(d+1);
                    //? DELAY
                    if (DELAY_MS > 0) sleep(DELAY_MS);
                }
            }
        }

        reverseWalk(start,finish);
    }

    private static void reverseWalk(Node start, Node finish){
        shortestPath = new LinkedList<Node>();
        if (finish.getDist() == Integer.MAX_VALUE) return;

        int tot_dist = finish.getDist();
        Node walker = finish;
        while(tot_dist > 0){
            for (Node a : walker.getAdj()){
                if (walker.getDist() == a.getDist()+1){
                    shortestPath.add(a);
                    --tot_dist;
                    walker = a;
                    break;
                }
            }
        }
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

    }

}

