package dijkstra;

import java.awt.*;
import java.awt.event.*; // Mouse event handling
import javax.swing.*;
import java.util.*;
//!import java.util.concurrent.*;

public class GridFrame {

    private final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    private static int CANVAS_WIDTH = 1200;
    private static int CANVAS_HEIGHT = 800;
    private static int NODE_SIZE = 10;
    private static final Color BG_COLOR = Color.GRAY;
    private static final Color NORMAL_COLOR = Color.WHITE;
    private static final Color START_COLOR = Color.GREEN;
    private static final Color FINISH_COLOR = Color.RED;
    private static final Color WALL_COLOR = Color.WHITE;
    private static final Color PATH_COLOR = Color.GREEN;
    private static final Color EXPLORE_COLOR = Color.BLUE;
    private static final Color ERR_COLOR = Color.RED;

    private JFrame frame;
    private JPanel panel;
    private GridStruct gridStruct;

    private boolean showingPath;

    // Structures to implement a more efficient waiting than Thread.sleep()
    //!private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    public final Object waiter = new Object();

    // Mouse handler
    private MouseAdapter drawer = new MouseAdapter() {
        Point prev;
        int rMouseClicks = 0;

        @Override
        public void mousePressed(MouseEvent e) {
            // Mouse sinistro del mouse: costruzione dei muri con drag
            if (SwingUtilities.isLeftMouseButton(e)) {
                if (showingPath) deleteShowingPath();
                
                prev = e.getPoint();
                int gridX = prev.x/NODE_SIZE;
                int gridY = prev.y/NODE_SIZE;
                Node n = gridStruct.getNode(gridX, gridY);
                gridStruct.setWall(gridX, gridY);
                updateNodeValue(n);
            }

            // Click destro del mouse: ciclo tra START e FINISH
            if (SwingUtilities.isRightMouseButton(e)){
                if (showingPath) deleteShowingPath();
            }
        }

        // Mouse drag: WALL
        @Override
        public void mouseDragged(MouseEvent e) {
            if (prev != null) {
                Point next = e.getPoint();
                int gridX = next.x/NODE_SIZE;
                int gridY = next.y/NODE_SIZE;
                Node n = gridStruct.getNode(gridX, gridY);
                gridStruct.setWall(gridX, gridY);
                updateNodeValue(n);
                prev = next;
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            // Click sinistro del mouse: resetto il punto prev a null
            if (SwingUtilities.isLeftMouseButton(e)) {
                prev = null;
            }
            // Click destro del mouse: ciclo tra START e FINISH
            if (SwingUtilities.isRightMouseButton(e)) {
                rMouseClicks++;
                int mouseX = e.getPoint().x;
                int mouseY = e.getPoint().y;
                int gridX = mouseX/NODE_SIZE;
                int gridY = mouseY/NODE_SIZE;
                Node n = gridStruct.getNode(gridX, gridY);
                if (rMouseClicks%2 == 1){
                    Node prev_start = gridStruct.resetStart();
                    gridStruct.setStart(gridX, gridY);
                    if (prev_start != null) updateNodeValue(prev_start);
                }
                else{
                    Node prev_finish = gridStruct.resetFinish();
                    gridStruct.setFinish(gridX, gridY);
                    if (prev_finish != null) updateNodeValue(prev_finish);
                }
                updateNodeValue(n);
            }
        }
    };


    // Classe privata usata solo come KeyListener
    private class KeyboardListener implements KeyListener {

        public void keyPressed(KeyEvent e){
            int k = e.getKeyCode();
            // ENTER
            if (k == 10){
                if (showingPath) deleteShowingPath();
                gridStruct.resetAll();
                Dijkstra.run();
                return;
            }
            // CANC
            if (k == 127){
                if (showingPath) deleteShowingPath();
                ArrayList<Node> prev_walls = gridStruct.resetWalls();
                for (Node w : prev_walls){
                    updateNodeValue(w);
                }
                return;
            }
        }

        public void keyTyped(KeyEvent e){
            return;
        }

        public void keyReleased(KeyEvent e){
            return;
        }

    }


    public GridFrame(boolean fullscreen, int node_size) {

        if (fullscreen){
            CANVAS_WIDTH = SCREEN_SIZE.width;
            CANVAS_HEIGHT = SCREEN_SIZE.height-70;
        }
        NODE_SIZE = node_size;

        this.frame = new JFrame();
        this.panel = new JPanel() {
            @Override
            public void paintComponent(Graphics graphics){
                super.paintComponent(graphics);
                setBackground(BG_COLOR);
                graphics.setColor(NORMAL_COLOR);
                for (int y=0; y<CANVAS_HEIGHT; y+=NODE_SIZE){
                    for (int x=0; x<CANVAS_WIDTH; x+=NODE_SIZE){
                        graphics.drawRect(x, y, NODE_SIZE, NODE_SIZE);
                    }
                } 
            }
        };

        int rows = CANVAS_WIDTH/NODE_SIZE;
        int cols = CANVAS_HEIGHT/NODE_SIZE;
        this.gridStruct = new GridStruct(rows, cols);
        this.showingPath = false;

        panel.setPreferredSize(new Dimension(CANVAS_WIDTH,CANVAS_HEIGHT));
        panel.addMouseListener(drawer);
        panel.addMouseMotionListener(drawer);
        Cursor cursor = Cursor.getDefaultCursor();
        panel.setCursor(cursor);

        KeyboardListener kListener = new KeyboardListener();
        frame.addKeyListener(kListener);

        Container containerPane = frame.getContentPane();
        containerPane.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setTitle("Dijkstra's algorithm");
        frame.setResizable(false);
        frame.setVisible(true);

    }

    public GridStruct getGridStruct(){ return this.gridStruct; }

    @Deprecated
    public JPanel getPanel(){ return this.panel; } //! USELESS

    // Modifica del punto in NORMAL, START, FINISH o WALL
    public void updateNodeValue(Node n){

        int x = n.getX();
        int y = n.getY();
        int canvasX = x*NODE_SIZE;
        int canvasY = y*NODE_SIZE;
        Node.Value value = n.getValue();
        Graphics g = this.panel.getGraphics();

        g.setColor(BG_COLOR);
        g.fillRect(canvasX, canvasY, NODE_SIZE, NODE_SIZE);
        switch(value) {
            case NORMAL:
                g.setColor(NORMAL_COLOR);
                g.drawRect(canvasX, canvasY, NODE_SIZE, NODE_SIZE);
                break;
            case START:
                g.setColor(START_COLOR);
                g.fillRect(canvasX, canvasY, NODE_SIZE, NODE_SIZE);
                break;
            case FINISH:
                g.setColor(FINISH_COLOR);
                g.fillRect(canvasX, canvasY, NODE_SIZE, NODE_SIZE);
                break;
            case WALL:
                g.setColor(WALL_COLOR);
                g.fillRect(canvasX, canvasY, NODE_SIZE, NODE_SIZE);
                break;
            default:
                break;
        }

    }

    // Durante Dijkstra mostro visivamente la ricerca del percorso
    public void updateNodeState(Node n){

        int x = n.getX();
        int y = n.getY();
        int canvasX = x*NODE_SIZE;
        int canvasY = y*NODE_SIZE;
        Node.State state = n.getState();
        Graphics g = this.panel.getGraphics();

        g.setColor(BG_COLOR);
        g.fillRect(canvasX, canvasY, NODE_SIZE, NODE_SIZE);
        switch(state) {
            case UNEXPLORED:
                g.setColor(NORMAL_COLOR);
                g.drawRect(canvasX, canvasY, NODE_SIZE, NODE_SIZE);
                break;
            case EXPLORING:
                g.setColor(EXPLORE_COLOR);
                g.fillRect(canvasX, canvasY, NODE_SIZE, NODE_SIZE);
                break;
            case EXPLORED:
                g.setColor(NORMAL_COLOR);
                g.drawRect(canvasX, canvasY, NODE_SIZE, NODE_SIZE);
                break;
            default:
                break;
        }

    }

    // Mostro il percorso migliore trovato alla fine di Dijkstra
    public void drawShortestPath(LinkedList<Node> path){

        Node[][] grid = gridStruct.getGrid();
        int rows = gridStruct.getRows();
        int cols = gridStruct.getCols();
        Graphics g = this.panel.getGraphics();

        boolean found = true;
        // Non esiste un percorso tra START e FINISH
        if (path.size() == 0){
            found = false;
            for (int x=0; x<rows; ++x){
                for (int y=0; y<cols; ++y){
                    Node n = grid[x][y];
                    int canvasX = x*NODE_SIZE;
                    int canvasY = y*NODE_SIZE;
                    if (n.getValue() == Node.Value.NORMAL){
                        g.setColor(ERR_COLOR);
                        g.fillRect(canvasX, canvasY, NODE_SIZE, NODE_SIZE);
                    }
                }
            }
            Dijkstra.sleep(700);
        }

        // Resetto i valori dei punti che non sono START, FINISH o WALL
        for (int x=0; x<rows; ++x){
            for (int y=0; y<cols; ++y){
                Node n = grid[x][y];
                int canvasX = x*NODE_SIZE;
                int canvasY = y*NODE_SIZE;
                if (n.getValue() == Node.Value.NORMAL){
                    g.setColor(BG_COLOR);
                    g.fillRect(canvasX, canvasY, NODE_SIZE, NODE_SIZE);
                    g.setColor(NORMAL_COLOR);
                    g.drawRect(canvasX, canvasY, NODE_SIZE, NODE_SIZE);
                }
            }
        }

        //! ROADBLOCK
        if (!found) return;

        // Disegno il percorso migliore tra START e FINISH
        g.setColor(PATH_COLOR);
        for (Node n : path){
            int canvasX = n.getX()*NODE_SIZE;
            int canvasY = n.getY()*NODE_SIZE;
            g.fillRect(canvasX, canvasY, NODE_SIZE, NODE_SIZE);
            //? DELAY
            if (Dijkstra.delayed) Dijkstra.sleep(Dijkstra.DELAY_MS);
        }
        this.showingPath = true;

    }

    private void deleteShowingPath(){
        LinkedList<Node> currentPath = Dijkstra.getCurrentPath();
        Graphics g = this.panel.getGraphics();
        for (Node n : currentPath){
            int canvasX = n.getX()*NODE_SIZE;
            int canvasY = n.getY()*NODE_SIZE;
            g.setColor(BG_COLOR);
            g.fillRect(canvasX, canvasY, NODE_SIZE, NODE_SIZE);
            g.setColor(NORMAL_COLOR);
            g.drawRect(canvasX, canvasY, NODE_SIZE, NODE_SIZE);
        }
        this.showingPath = false;
    }
    
}
