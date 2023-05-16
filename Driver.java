/**
 * Driver class to test bots.
 */
public class Driver {
    public static void main(String[] args) {
        int trials = 100;
        int wins = 0;
        for (int i = 0; i < trials; i++) {
            Minesweeper minesweeper = new Minesweeper(Difficulty.EXPERT);
            Bot bot = new Bot(new ProbabilisticStrategy(minesweeper));
            Status status = bot.runGame();
            if (status == Status.WIN) {
                wins++;
            }
            System.out.println(i);
        }
        System.out.println("Win Percentage: " + (double) wins / trials);
    }
}
