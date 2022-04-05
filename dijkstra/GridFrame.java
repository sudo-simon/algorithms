import java.awt.*;
import java.awt.event.*; // Mouse event handling
import javax.swing.*;
import java.util.*;

public class GridFrame {

    private static final int CANVAS_WIDTH = 1200;
    private static final int CANVAS_HEIGHT = 800;
    private static final Color BG_COLOR = Color.GRAY;
    private static final Color NORMAL_COLOR = Color.WHITE;
    private static final Color START_COLOR = Color.GREEN;
    private static final Color FINISH_COLOR = Color.RED;
    private static final Color WALL_COLOR = Color.WHITE;
    private static final Color PATH_COLOR = Color.GREEN;
    private static final Color EXPLORE_COLOR = Color.BLUE;

    private JFrame frame;
    private JPanel panel;
    private GridStruct gridStruct;

    private boolean showingPath; //TODO: usare negli event handler


    // Mouse handler
    private MouseAdapter drawer = new MouseAdapter() {
        Point prev;
        int rMouseClicks = 0;

        @Override
        public void mousePressed(MouseEvent e) {
            // Mouse sinistro del mouse: costruzione dei muri con drag
            if (SwingUtilities.isLeftMouseButton(e)) {
                prev = e.getPoint();
                int gridX = prev.x/10;
                int gridY = prev.y/10;
                Node n = gridStruct.getNode(gridX, gridY);
                gridStruct.setWall(gridX, gridY);
                updateNodeValue(n);
            }

            // Click destro del mouse: ciclo tra START e FINISH
            //if (SwingUtilities.isRightMouseButton(e))
        }

        // Mouse drag: WALL
        @Override
        public void mouseDragged(MouseEvent e) {
            if (prev != null) {
                Point next = e.getPoint();
                int gridX = next.x/10;
                int gridY = next.y/10;
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
                int gridX = mouseX/10;
                int gridY = mouseY/10;
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
                gridStruct.resetAllDist();
                Dijkstra.run();
                return;
            }
            // CANC
            if (k == 127){
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


    public GridFrame() {

        this.frame = new JFrame();
        this.panel = new JPanel() {
            @Override
            public void paintComponent(Graphics graphics){
                super.paintComponent(graphics);
                setBackground(BG_COLOR);
                graphics.setColor(NORMAL_COLOR);
                for (int y=0; y<CANVAS_HEIGHT; y+=10){
                    for (int x=0; x<CANVAS_WIDTH; x+=10){
                        graphics.drawRect(x, y, 10, 10);
                    }
                } 
            }
        };

        int rows = CANVAS_WIDTH/10;
        int cols = CANVAS_HEIGHT/10;
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

    public JPanel getPanel(){ return this.panel; } //! USELESS

    // Modifica del punto in NORMAL, START, FINISH o WALL
    public void updateNodeValue(Node n){

        int x = n.getX();
        int y = n.getY();
        int canvasX = x*10;
        int canvasY = y*10;
        Node.Value value = n.getValue();
        Graphics g = this.panel.getGraphics();

        g.setColor(BG_COLOR);
        g.fillRect(canvasX, canvasY, 10, 10);
        switch(value) {
            case NORMAL:
                g.setColor(NORMAL_COLOR);
                g.drawRect(canvasX, canvasY, 10, 10);
                break;
            case START:
                g.setColor(START_COLOR);
                g.fillRect(canvasX, canvasY, 10, 10);
                break;
            case FINISH:
                g.setColor(FINISH_COLOR);
                g.fillRect(canvasX, canvasY, 10, 10);
                break;
            case WALL:
                g.setColor(WALL_COLOR);
                g.fillRect(canvasX, canvasY, 10, 10);
                break;
            default:
                break;
        }

    }

    // Durante Dijkstra mostro visivamente la ricerca del percorso
    public void updateNodeState(Node n){

        int x = n.getX();
        int y = n.getY();
        int canvasX = x*10;
        int canvasY = y*10;
        Node.State state = n.getState();
        Graphics g = this.panel.getGraphics();

        g.setColor(BG_COLOR);
        g.fillRect(canvasX, canvasY, 10, 10);
        switch(state) {
            case UNEXPLORED:
                g.setColor(NORMAL_COLOR);
                g.drawRect(canvasX, canvasY, 10, 10);
                break;
            case EXPLORING:
                g.setColor(EXPLORE_COLOR);
                g.fillRect(canvasX, canvasY, 10, 10);
                break;
            case EXPLORED:
                g.setColor(NORMAL_COLOR);
                g.drawRect(canvasX, canvasY, 10, 10);
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

        // Resetto i valori dei punti che non sono START, FINISH o WALL
        for (int x=0; x<rows; ++x){
            for (int y=0; y<cols; ++y){
                Node n = grid[x][y];
                int canvasX = x*10;
                int canvasY = y*10;
                if (n.getValue() == Node.Value.NORMAL){
                    g.setColor(BG_COLOR);
                    g.fillRect(canvasX, canvasY, 10, 10);
                    g.setColor(NORMAL_COLOR);
                    g.drawRect(canvasX, canvasY, 10, 10);
                }
            }
        }

        // Disegno il percorso migliore tra START e FINISH
        g.setColor(PATH_COLOR);
        for (Node n : path){
            int canvasX = n.getX()*10;
            int canvasY = n.getY()*10;
            g.fillRect(canvasX, canvasY, 10, 10);
        }
        this.showingPath = true;

    }

    // Non esiste un percorso tra START e FINISH
    public void pathNotFound(){
        //TODO here
    }
    
}
