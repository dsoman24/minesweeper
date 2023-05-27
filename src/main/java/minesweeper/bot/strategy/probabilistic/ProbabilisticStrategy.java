package src.main.java.minesweeper.bot.strategy.probabilistic;
import java.util.Random;

import src.main.java.minesweeper.bot.strategy.BotStrategy;
import src.main.java.minesweeper.logic.Tileable;


/**
 * Probabilistic minesweeper strategy.
 * Algorithm inspired by: https://codereview.stackexchange.com/questions/54737/analyzing-minesweeper-probabilities
 */
public class ProbabilisticStrategy<T extends Tileable> extends BotStrategy<T> {

    private Random random;
    // ruleManager is not initialized until tileToClear is called
    private Solver<T> solver;

    public ProbabilisticStrategy(Random random) {
        this.random = random;
    }

    public ProbabilisticStrategy() {
        this(new Random());
    }

    @Override
    public T tileToClear() {
        solver = new Solver<>(tilingState);
        T tile = solver.tileToClear(random);
        return tile;
    }

    @Override
    public String name() {
        return "PROBABILISTIC";
    }
}
