
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener {
    int w = 600, h = 600, ts = 20;
    int dx = 0, dy = 0, len = 3;
    int[] x = new int[1000];
    int[] y = new int[1000];
    int fx, fy;
    Timer t;
    Random r = new Random();
    boolean run = true;

    SnakeGame() {
        setPreferredSize(new Dimension(w, h));
        setBackground(Color.black);
        setFocusable(true);
        addKeyListener(new KL());
        spawnFood();
        t = new Timer(100, this);
        t.start();
    }

    void spawnFood() {
        fx = r.nextInt(w/ts)*ts;
        fy = r.nextInt(h/ts)*ts;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (!run) {
            g.setColor(Color.red);
            g.drawString("Game Over!", w/2 - 30, h/2);
            return;
        }

        g.setColor(Color.green);
        for (int i = 0; i < len; i++) g.fillRect(x[i], y[i], ts, ts);

        g.setColor(Color.red);
        g.fillRect(fx, fy, ts, ts);
    }

    public void actionPerformed(ActionEvent e) {
        if (!run) return;

        for (int i = len - 1; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        x[0] += dx;
        y[0] += dy;

        if (x[0] == fx && y[0] == fy) {
            len++;
            spawnFood();
        }

        if (x[0] < 0 || x[0] >= w || y[0] < 0 || y[0] >= h) run = false;

        for (int i = 1; i < len; i++) 
            if (x[0] == x[i] && y[0] == y[i]) run = false;

        repaint();
    }

    class KL extends KeyAdapter {
        public void keyPressed(KeyEvent k) {
            int c = k.getKeyCode();
            if (c == KeyEvent.VK_UP && dy == 0) { dx = 0; dy = -ts; }
            if (c == KeyEvent.VK_DOWN && dy == 0) { dx = 0; dy = ts; }
            if (c == KeyEvent.VK_LEFT && dx == 0) { dx = -ts; dy = 0; }
            if (c == KeyEvent.VK_RIGHT && dx == 0) { dx = ts; dy = 0; }
        }
    }

    public static void main(String[] a) {
        JFrame f = new JFrame("Snake");
        SnakeGame g = new SnakeGame();
        f.add(g);
        f.pack();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
}
