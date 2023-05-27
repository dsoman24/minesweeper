package src.main.java.minesweeper.bot.strategy.probabilistic;
import java.util.Random;

import src.main.java.minesweeper.bot.strategy.BotStrategy;
import src.main.java.minesweeper.logic.MinesweeperTileable;


/**
 * Probabilistic minesweeper strategy.
 * Algorithm inspired by: https://codereview.stackexchange.com/questions/54737/analyzing-minesweeper-probabilities
 */
public class ProbabilisticStrategy<T extends MinesweeperTileable> extends BotStrategy<T> {

    private Random random;

    /**
     * Takes in a random number generator.
     * @param random the Random instance to use.
     */
    public ProbabilisticStrategy(Random random) {
        this.random = random;
    }

    /**
     * Initializes the strategy with a new Random instance.
     */
    public ProbabilisticStrategy() {
        this(new Random());
    }

    @Override
    public T tileToClear() {
        Solver<T> solver = new Solver<>(tilingState);
        T tile = solver.tileToClear(random);
        return tile;
    }

    @Override
    public String name() {
        return "PROBABILISTIC";
    }
}
