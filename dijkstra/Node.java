package dijkstra;

import java.util.*;
import java.lang.Comparable;

public class Node implements Comparable<Node> {

    public enum State {
        UNEXPLORED,
        EXPLORING,
        EXPLORED
    };

    public enum Value {
        START,
        FINISH,
        WALL,
        NORMAL
    };

    private int x, y;
    private Value value;
    private State state;
    private int dist;
    private ArrayList<Node> adjacent;   // Lista di adiacenze

    public Node(int x, int y, Value val){
        this.x = x;
        this.y = y;
        this.value = val;
        this.state = State.UNEXPLORED;
        if (val == Value.START) this.dist = 0;
        else this.dist = Integer.MAX_VALUE;
        this.adjacent = new ArrayList<Node>();
    }

    public int getX(){ return this.x; }

    public int getY(){ return this.y; }

    public Value getValue(){ return this.value; }

    public State getState(){ return this.state; }

    public int getDist(){ return this.dist; }

    public ArrayList<Node> getAdj(){ return this.adjacent; }



    //public void setValue(Value new_val){ this.value = new_val; }  //! DEPRECATED

    public void setState(State new_state){ this.state = new_state; }

    public void setDist(int d){ this.dist = d; }

    public void addAdj(Node n){ this.adjacent.add(n); }

    public void removeAdj(Node n){ this.adjacent.remove(n); }


    public void setStart(){
        this.value = Value.START;
        //this.dist = 0;
        for (Node n : this.adjacent){
            if (! n.getAdj().contains(this))
                n.addAdj(this);
        }
    }
    public void setFinish(){
        this.value = Value.FINISH;
        //this.dist = Integer.MAX_VALUE;
        for (Node n : this.adjacent){
            if (! n.getAdj().contains(this))
                n.addAdj(this);
        }
    }
    public void setWall(){
        this.value = Value.WALL;
        //this.dist = Integer.MAX_VALUE;
        for (Node n : this.adjacent){
            if (n.getAdj().contains(this))
                n.removeAdj(this);
        }
    }
    public void setNormal(){
        this.value = Value.NORMAL;
        //this.dist = Integer.MAX_VALUE;
        for (Node n : this.adjacent){
            if (! n.getAdj().contains(this))
                n.addAdj(this);
        }
    }

    public int compareTo(Node n2){
        if (this.dist < n2.dist) return -1;
        if (this.dist > n2.dist) return 1;
        else return 0;
    }

}