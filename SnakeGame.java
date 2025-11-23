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

    SnakeGame() {
        setPreferredSize(new Dimension(w, h));
        setBackground(Color.black);
        setFocusable(true);
        addKeyListener(new KL());
        spawnFood();

        t = new Timer(120, this);        // lower = faster
        t.start();
    }

    void spawnFood() {
        fx = r.nextInt(w / ts) * ts;
        fy = r.nextInt(h / ts) * ts;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Score
        g.setColor(Color.white);
        g.drawString("Score: " + score, 10, 20);

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
            len++;
            score++;
            spawnFood();

            if (t.getDelay() > 50) {
                t.setDelay(t.getDelay() - 5);  // speed up
            }
        }

        // hit wall
        if (x[0] < 0 || x[0] >= w || y[0] < 0 || y[0] >= h)
            run = false;

        // hit own body
        for (int i = 1; i < len; i++)
            if (x[0] == x[i] && y[0] == y[i])
                run = false;

        repaint();
    }

    class KL extends KeyAdapter {
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
        repaint();
    }

    public static void main(String[] a) {
        JFrame f = new JFrame("Snake Game");
        SnakeGame g = new SnakeGame();
        f.add(g);
        f.pack();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
}
