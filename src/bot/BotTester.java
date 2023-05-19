package src.bot;

import src.minesweeper.Difficulty;
import src.minesweeper.GameSummary;
import src.minesweeper.Minesweeper;
import src.bot.strategy.probabilistic.ProbabilisticStrategy;

/**
 * Driver class to test bots.
 */
public class BotTester {

    /** Number of trials per density */
    private int numTrials;
    private int rows;
    private int columns;

    public BotTester(int numTrials, int rows, int columns) {
        this.numTrials = numTrials;
        this.rows = rows;
        this.columns = columns;
    }

    public BotTester(int numTrials) {
        this(numTrials, 9, 9);
    }

    public static void main(String[] args) {
        BotTester tester = new BotTester(100);
        GameSummary summary = tester.playToCompletion(10);
        System.out.println(summary);
    }

    /**
     * Plays one game to completion with a given density.
     * @return a summary of the game
     */
    private GameSummary playToCompletion(double density) {
        Difficulty difficulty = Difficulty.customDifficulty(rows, columns, density);
        Minesweeper minesweeper = new Minesweeper(difficulty);
        Bot bot = new Bot(new ProbabilisticStrategy(minesweeper));
        bot.runGame();
        return minesweeper.getSummary();
    }

    private GameSummary playToCompletion(int numMines) {
        return playToCompletion((double) numMines / (rows * columns));
    }
}
