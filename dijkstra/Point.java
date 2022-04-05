public class Point {

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

    public Point(int x, int y, Value val){
        this.x = x;
        this.y = y;
        this.value = val;
        this.state = State.UNEXPLORED;
        if (val == Value.START) this.dist = 0;
        else this.dist = Integer.MAX_VALUE;
    }

    public int getX(){ return this.x; }
    public int getY(){ return this.y; }
    public Value getValue(){ return this.value; }
    public State getState(){ return this.state; }
    public int getDist(){ return this.dist; }

    public void setValue(Value new_val){ this.value = new_val; }
    public void setState(State new_state){ this.state = new_state; }
    public void setDist(int d){ this.dist = d; }

}