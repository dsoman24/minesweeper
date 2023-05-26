package src.main.java.bot.strategy.probabilistic;
import java.util.Random;

import src.main.java.bot.strategy.BotStrategy;
import src.main.java.minesweeper.Tile;


/**
 * Probabilistic minesweeper strategy.
 * Algorithm inspired by: https://codereview.stackexchange.com/questions/54737/analyzing-minesweeper-probabilities
 */
public class ProbabilisticStrategy extends BotStrategy {

    private Random random;
    // ruleManager is not initialized until tileToClear is called
    private Solver solver;

    public ProbabilisticStrategy(Random random) {
        this.random = random;
    }

    public ProbabilisticStrategy() {
        this(new Random());
    }

    @Override
    public Tile tileToClear() {
        solver = new Solver(minesweeper);
        Tile tile = solver.tileToClear(random);
        return tile;
    }

    @Override
    public String name() {
        return "PROBABILISTIC";
    }
}
