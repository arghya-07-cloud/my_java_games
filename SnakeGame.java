import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener {

    int w = 600, h = 600, ts = 20;        // window, tile size
    int len = 3;                         // snake length
    int dx = ts, dy = 0;                 // movement
    int[] x = new int[1000];
    int[] y = new int[1000];
    int fx, fy;                          // food position
    int score = 0;                       // score
    boolean run = true;

    Timer t;
    Random r = new Random();

    // default constructor - starts timer
    public SnakeGame() {
        this(true);
    }

    // constructor that can disable timer (useful for tests)
    public SnakeGame(boolean startTimer) {
        setPreferredSize(new Dimension(w, h));
        setBackground(Color.black);
        setFocusable(true);
        addKeyListener(new KL());

        initPositions();
        spawnFood();

        if (startTimer) {
            t = new Timer(120, this);        // lower = faster
            t.start();
        } else {
            // create timer object for API usage but don't start
            t = new Timer(120, this);
        }
    }

    // Initialize snake positions with head centered and body trailing to the left
    void initPositions() {
        int cx = (w / 2 / ts) * ts;
        int cy = (h / 2 / ts) * ts;
        x[0] = cx;
        y[0] = cy;
        for (int i = 1; i < len; i++) {
            x[i] = x[i - 1] - ts;
            y[i] = y[i - 1];
        }
    }

    // spawn food ensuring it does not overlap the snake body
    void spawnFood() {
        int attempts = 0;
        while (true) {
            fx = r.nextInt(w / ts) * ts;
            fy = r.nextInt(h / ts) * ts;
            boolean onSnake = false;
            for (int i = 0; i < len; i++) {
                if (x[i] == fx && y[i] == fy) {
                    onSnake = true;
                    break;
                }
            }
            attempts++;
            if (!onSnake) break;
            if (attempts > 1000) break; // safety to avoid infinite loop in pathological cases
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Score (guarded behind run to avoid font issues in some headless envs)
        if (run) {
            g.setColor(Color.white);
            g.drawString("Score: " + score, 10, 20);
        }

        if (!run) {
            g.setColor(Color.red);
            g.drawString("GAME OVER! Press R to Restart", w / 2 - 80, h / 2);
            return;
        }

        // Draw snake
        g.setColor(Color.green);
        for (int i = 0; i < len; i++)
            g.fillRect(x[i], y[i], ts, ts);

        // Draw food
        g.setColor(Color.red);
        g.fillRect(fx, fy, ts, ts);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!run) return;

        // move body
        for (int i = len - 1; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        // move head
        x[0] += dx;
        y[0] += dy;

        // eat food
        if (x[0] == fx && y[0] == fy) {
            if (len < x.length) {
                len++;
            }
            score++;
            spawnFood();

            // speed up but cap minimum delay
            int d = t.getDelay();
            if (d > 40) {
                t.setDelay(Math.max(40, d - 5));
            }
        }

        // hit wall
        if (x[0] < 0 || x[0] >= w || y[0] < 0 || y[0] >= h) {
            run = false;
        }

        // hit own body
        for (int i = 1; i < len; i++) {
            if (x[0] == x[i] && y[0] == y[i]) {
                run = false;
                break;
            }
        }

        repaint();
    }

    class KL extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent k) {
            int c = k.getKeyCode();

            if (c == KeyEvent.VK_UP && dy == 0) { dx = 0; dy = -ts; }
            if (c == KeyEvent.VK_DOWN && dy == 0) { dx = 0; dy = ts; }
            if (c == KeyEvent.VK_LEFT && dx == 0) { dx = -ts; dy = 0; }
            if (c == KeyEvent.VK_RIGHT && dx == 0) { dx = ts; dy = 0; }

            if (c == KeyEvent.VK_R && !run) restart();
        }
    }

    void restart() {
        len = 3;
        dx = ts;
        dy = 0;
        score = 0;
        run = true;
        t.setDelay(120);
        initPositions();
        spawnFood();
        repaint();
    }

    // Accessors useful for tests
    public int getHeadX() { return x[0]; }
    public int getHeadY() { return y[0]; }
    public int getLen() { return len; }
    public int getFoodX() { return fx; }
    public int getFoodY() { return fy; }
    public int getDelay() { return t.getDelay(); }

    public static void main(String[] a) {
        JFrame f = new JFrame("Snake Game");
        SnakeGame g = new SnakeGame();
        f.add(g);
        f.pack();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
}