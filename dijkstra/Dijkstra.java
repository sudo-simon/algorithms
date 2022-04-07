package dijkstra;

import javax.swing.*;
import java.util.*;
import java.lang.Thread;

public class Dijkstra {

    private static GridFrame gridFrame;
    private static GridStruct gridStruct;
    private static Node[][] grid;


    //AUX for sleep()
    public static boolean delayed = false;
    public static long DELAY_MS = 0;
    public static void sleep(long millis){
        try{
            Thread.sleep(millis);
            //TimeUnit.MILLISECONDS.sleep(millis);
        } catch(InterruptedException e){
            e.printStackTrace();
            System.exit(-1);
        }
    }

    // Dijkstra's support data structures
    private static NodeComparator nodeComp = new NodeComparator();
    private static PriorityQueue<Node> pq;
    private static LinkedList<Node> shortestPath;

    // The core Dijkstra's algorithm
    public static void algorithm(){

        Node start = gridStruct.getStart();
        Node finish = gridStruct.getFinish();
        if (start == null || finish == null) return;

        // PQ initialization with D[s]
        pq = new PriorityQueue<Node>(nodeComp);
        for (int r=0; r<gridStruct.getRows(); ++r){
            for (int c=0; c<gridStruct.getCols(); ++c){
                Node n = grid[r][c];
                if (n.getValue() != Node.Value.WALL)
                    pq.add(n);
            }
        }

        while (!pq.isEmpty()){
            Node n = pq.poll();

            // Ho raggiunto FINISH
            if (n == finish) break;

            if (n != start){
                n.setState(Node.State.EXPLORING);
                gridFrame.updateNodeState(n);
            }

            //? DELAY
            if (delayed) sleep(DELAY_MS);

            for (Node a : n.getAdj()){
                if (pq.contains(a)){
                    // Rilassamento
                    int d = n.getDist();
                    if (d+1 < a.getDist()){
                        pq.remove(a);
                        a.setDist(d+1);
                        pq.add(a);
                    }
                }
            }
        }

        reverseWalk(start,finish);
    }

    private static void reverseWalk(Node start, Node finish){
        shortestPath = new LinkedList<Node>();
        if (finish.getDist() == Node.MAX_DIST) {
            return;
        }
        
        int tot_dist = finish.getDist();
        Node walker = finish;
        while(tot_dist > 0){
            for (Node a : walker.getAdj()){
                if (walker.getDist() == a.getDist()+1){
                    if (tot_dist > 1) shortestPath.add(a);
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

    // Function to get the path to delete
    public static LinkedList<Node> getCurrentPath(){ return shortestPath; }




    private static void printHelp(){
        System.out.println(
            "\n"+
            "Program to compute the shortest path from a start point to a finish one using Dijkstra's algorithm\n"+
            "\n"+
            "Usage:\n"+
            "   ~ RIGHT CLICK to cycle between setting the starting point and the finish point\n"+
            "   ~ LEFT CLICK to draw walls the algorithm won't be able to walk through\n"+
            "   ~ ENTER to run the algorithm\n"+
            "   ~ CANC to delete all the drawn walls\n"+
            "\n"+
            "Parameters:\n"+
            "   -fs (--fullscreen): to set the window to fullscreen\n"+
            "   -d (--delay) <INT>: to have a <INT> milliseconds delay between operations for a clearer visualization\n"+
            "       NOTE: YET TO BE OPTIMIZED! Few milliseconds will result in big delays so use very small numbers (1~5)\n"+
            "   -s (--size) <INT>: to set the size of the single grid squares to <INT>\n"+
            "       (smaller square sizes will result in more total squares to travel, and vice versa)\n"
        );
    }

    public static void main(String[] args){

        boolean tmp_fullscreen = false;
        int tmp_node_size = 10;

        if (args.length == 1 && (args[0].equals("--help") || args[0].equals("-h"))){
            printHelp();
            return;
        }

        for (int i=0; i<args.length; ++i){
            String arg = args[i];
            if (arg.equals("--fullscreen") || arg.equals("-fs"))
                tmp_fullscreen = true;
            if (arg.equals("--delay") || arg.equals("-d")){
                try{
                    DELAY_MS = Long.parseLong(args[i+1]);
                    delayed = true;
                } catch(NumberFormatException e){
                    e.printStackTrace();
                    return;
                } catch(IndexOutOfBoundsException e){
                    e.printStackTrace();
                    return;
                }
            }
            if (arg.equals("--size") || arg.equals("-s")){
                try{
                    tmp_node_size = Integer.parseInt(args[i+1]);
                } catch(NumberFormatException e){
                    e.printStackTrace();
                    return;
                } catch(IndexOutOfBoundsException e){
                    e.printStackTrace();
                    return;
                }
            }
        }

        final boolean fullscreen = tmp_fullscreen;
        final int node_size = tmp_node_size;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run(){
                gridFrame = new GridFrame(fullscreen,node_size);
                gridStruct = gridFrame.getGridStruct();
                grid = gridStruct.getGrid();
            }
        });

    }

}

