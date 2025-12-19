public class RunSnakeGameHeadless {
    public static void main(String[] args) {
        System.out.println("--- Snake Game Headless Runner ---");
        try {
            // Instantiate game without starting the Swing Timer
            SnakeGame game = new SnakeGame(false);

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

        } catch (Exception e) {
            System.err.println("An error occurred during headless execution:");
            e.printStackTrace();
        }
    }
}
