package src.bot;

import src.minesweeper.Difficulty;
import src.minesweeper.Minesweeper;
import src.minesweeper.Status;
import src.bot.strategy.probabilistic.ProbabilisticStrategy;

/**
 * Driver class to test bots.
 */
public class Driver {
    public static void main(String[] args) {
        Difficulty difficulty = Difficulty.customDifficulty(5, 5, 0.8);
        Minesweeper minesweeper = new Minesweeper(difficulty, false);
        System.out.println(difficulty.toString());
        minesweeper.clear(1, 1);
        System.out.println(minesweeper);
        System.out.println(minesweeper.status());
    }

}
