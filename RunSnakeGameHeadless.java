import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class RunSnakeGameHeadless {
    public static void main(String[] args) {
        System.out.println("--- Snake Game Headless Runner ---");
        try {
            // Instantiate game without starting the Swing Timer
            SnakeGame game = new SnakeGame(false);

            // Set size explicitly as it might not be set in headless panel
            game.setSize(600, 600);

            int initialX = game.getHeadX();
            int initialY = game.getHeadY();
            int initialLen = game.getLen();

            System.out.println("Initial State:");
            System.out.println("  Head Position: (" + initialX + ", " + initialY + ")");
            System.out.println("  Snake Length: " + initialLen);
            System.out.println("  Score: " + 0); // initial score is 0

            // Simulate 5 ticks
            System.out.println("\nSimulating game execution (5 ticks)...");
            for (int i = 1; i <= 5; i++) {
                game.actionPerformed(null);
                System.out.println("  Tick " + i + ": Head (" + game.getHeadX() + ", " + game.getHeadY() + ")");
            }

            // Verification
            int expectedX = initialX + (20 * 5); // 20 is ts (tile size), moving right 5 times
            if (game.getHeadX() == expectedX && game.getHeadY() == initialY) {
                System.out.println("\nSUCCESS: Snake moved correctly to the right.");
            } else {
                System.out.println("\nFAILURE: Snake did not move as expected.");
                System.out.println("Expected X: " + expectedX + ", Actual X: " + game.getHeadX());
            }

            // Generate Screenshot
            System.out.println("\nGenerating screenshot 'snake_game.png'...");
            BufferedImage bi = new BufferedImage(600, 600, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = bi.createGraphics();

            // Manually paint background since paintComponent calls super which might depend on peer
            g2d.setColor(java.awt.Color.black);
            g2d.fillRect(0, 0, 600, 600);

            game.paintComponent(g2d);
            g2d.dispose();

            ImageIO.write(bi, "png", new File("snake_game.png"));
            System.out.println("Screenshot saved.");

        } catch (Exception e) {
            System.err.println("An error occurred during headless execution:");
            e.printStackTrace();
        }
    }
}
