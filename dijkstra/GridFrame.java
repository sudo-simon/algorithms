//import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.*; // Mouse event handling
import javax.swing.*;
import java.util.*;

public class GridFrame {

    private static final int CANVAS_WIDTH = 1200;
    private static final int CANVAS_HEIGHT = 800;
    private JFrame frame;
    private JPanel panel;
    //private DrawCanvas canvas;
    private GridStruct gridStruct;
    //private Graphics graphics;

    protected class DrawCanvas extends JPanel {

        private Graphics g;

        @Override
        public void paintComponent(Graphics graphics){

            this.g = graphics;
            super.paintComponent(graphics);
            setBackground(Color.GRAY);
            graphics.setColor(Color.WHITE);
            for (int y=0; y<CANVAS_HEIGHT; y+=10){
                for (int x=0; x<CANVAS_WIDTH; x+=10){
                    graphics.drawRect(x, y, 10, 10);
                }
            }
            
        }

        // Modifica del punto in NORMAL, START, FINISH o WALL
        public void updatePointValue(Point p){

            int x = p.getX();
            int y = p.getY();
            int canvasX = x*10;
            int canvasY = y*10;
            Point.Value value = p.getValue();

            //clearRect(canvasX, canvasY, 10, 10);
            //getGraphics().clearRect(canvasX, canvasY, 10, 10);
            switch(value) {
                case NORMAL:
                    getGraphics().setColor(Color.WHITE);
                    getGraphics().drawRect(canvasX, canvasY, 10, 10);
                    break;
                case START:
                    getGraphics().setColor(Color.GREEN);
                    getGraphics().fillRect(canvasX, canvasY, 10, 10);
                    break;
                case FINISH:
                    getGraphics().setColor(Color.RED);
                    getGraphics().fillRect(canvasX, canvasY, 10, 10);
                    break;
                case WALL:
                    getGraphics().setColor(Color.WHITE);
                    getGraphics().fillRect(canvasX, canvasY, 10, 10);
                    break;
                default:
                    break;
            }

        }

        // Durante Dijkstra mostro visivamente la ricerca del percorso
        public void updatePointState(Point p){

            int x = p.getX();
            int y = p.getY();
            int canvasX = x*10;
            int canvasY = y*10;
            Point.State state = p.getState();

            getGraphics().clearRect(canvasX, canvasY, 10, 10);
            switch(state) {
                case UNEXPLORED:
                    getGraphics().setColor(Color.WHITE);
                    getGraphics().drawRect(canvasX, canvasY, 10, 10);
                    break;
                case EXPLORING:
                    getGraphics().setColor(Color.BLUE);
                    getGraphics().fillRect(canvasX, canvasY, 10, 10);
                    break;
                case EXPLORED:
                    getGraphics().setColor(Color.WHITE);
                    getGraphics().drawRect(canvasX, canvasY, 10, 10);
                    break;
                default:
                    break;
            }

        }

        // Mostro il percorso migliore trovato alla fine di Dijkstra
        public void drawShortestPath(LinkedList<Point> path){

            Point[][] grid = gridStruct.getGrid();
            int rows = gridStruct.getRows();
            int cols = gridStruct.getCols();

            // Resetto i valori dei punti che non sono START, FINISH o WALL
            getGraphics().setColor(Color.WHITE);
            for (int x=0; x<rows; ++x){
                for (int y=0; y<cols; ++y){
                    Point p = grid[x][y];
                    int canvasX = p.getX()*10;
                    int canvasY = p.getY()*10;
                    if (p.getValue() == Point.Value.NORMAL){
                        getGraphics().clearRect(canvasX, canvasY, 10, 10);
                        getGraphics().drawRect(canvasX, canvasY, 10, 10);
                    }
                }
            }

            // Disegno il percorso migliore tra START e FINISH
            getGraphics().setColor(Color.GREEN);
            for (Point p : path){
                int canvasX = p.getX()*10;
                int canvasY = p.getY()*10;
                getGraphics().clearRect(canvasX, canvasY, 10, 10);
                getGraphics().fillRect(canvasX, canvasY, 10, 10);
            }

        }

    }

    public GridFrame() {

        this.frame = new JFrame();
        this.panel = new JPanel() {
            @Override
            public void paintComponent(Graphics graphics){
                super.paintComponent(graphics);
                setBackground(Color.GRAY);
                graphics.setColor(Color.WHITE);
                for (int y=0; y<CANVAS_HEIGHT; y+=10){
                    for (int x=0; x<CANVAS_WIDTH; x+=10){
                        graphics.drawRect(x, y, 10, 10);
                    }
                } 
            }
        };
        //this.canvas = new DrawCanvas();

        int rows = CANVAS_WIDTH/10;
        int cols = CANVAS_HEIGHT/10;
        this.gridStruct = new GridStruct(rows, cols);
        //this.graphics = canvas.getGraphics();

        panel.setPreferredSize(new Dimension(CANVAS_WIDTH,CANVAS_HEIGHT));
        Container containerPane = frame.getContentPane();
        containerPane.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setTitle("Dijkstra's algorithm");
        frame.setResizable(false);
        frame.setVisible(true);

    }

    public GridStruct getGridStruct(){ return this.gridStruct; }
    //public DrawCanvas getCanvas(){ return this.canvas; }
    public JPanel getPanel(){ return this.panel; }

    // Modifica del punto in NORMAL, START, FINISH o WALL
    public void updatePointValue(Point p){

        int x = p.getX();
        int y = p.getY();
        int canvasX = x*10;
        int canvasY = y*10;
        Point.Value value = p.getValue();
        Graphics g = this.panel.getGraphics();

        //clearRect(canvasX, canvasY, 10, 10);
        //getGraphics().clearRect(canvasX, canvasY, 10, 10);
        switch(value) {
            case NORMAL:
                g.setColor(Color.WHITE);
                g.drawRect(canvasX, canvasY, 10, 10);
                break;
            case START:
                g.setColor(Color.GREEN);
                g.fillRect(canvasX, canvasY, 10, 10);
                break;
            case FINISH:
                g.setColor(Color.RED);
                g.fillRect(canvasX, canvasY, 10, 10);
                break;
            case WALL:
                g.setColor(Color.WHITE);
                g.fillRect(canvasX, canvasY, 10, 10);
                break;
            default:
                break;
        }

    }
    
}
