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
    public static void algorithm(){     //TODO: aggiungere Thread.sleep() per visualizzare meglio l'algoritmo

        Node start = gridStruct.getStart();
        Node finish = gridStruct.getFinish();
        if (start == null || finish == null) return;

        // PQ initialization with D[s]
        PQ = new PriorityQueue<Node>(nodeComp);
        for (int r=0; r<gridStruct.getRows(); ++r){
            for (int c=0; c<gridStruct.getCols(); ++c){
                Node n = grid[r][c];
                if (n.getValue() != Node.Value.WALL)
                    PQ.add(n);
            }
        }

        while (! PQ.isEmpty()){
            Node n = PQ.poll();
            n.setState(Node.State.EXPLORING);
            gridFrame.updateNodeState(n);
            for (Node a : n.getAdj()){
                if (PQ.contains(a)){
                    a.setState(Node.State.EXPLORING);
                    gridFrame.updateNodeState(a);
                    // Rilassamento
                    int d = n.getDist();
                    if (d + 1 < a.getDist())
                        a.setDist(d+1);
                }
            }
        }

        reverseWalk(start,finish);
    }

    private static void reverseWalk(Node start, Node finish){
        shortestPath = new LinkedList<Node>();
        if (finish.getDist() == Integer.MAX_VALUE){
            gridFrame.pathNotFound();
            return;
        }
        //TODO here (spTree)
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

