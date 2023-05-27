package src.main.java.minesweeper.bot.strategy.random;
import java.util.Random;

import src.main.java.minesweeper.bot.strategy.BotStrategy;
import src.main.java.minesweeper.logic.MinesweeperTileable;

public class RandomStrategy<T extends MinesweeperTileable> extends BotStrategy<T> {

    private Random rand;

    /**
     * Sets the random instance.
     * @param rand the random instance to use when selecting tiles to clear.
     */
    public RandomStrategy(Random rand) {
        this.rand = rand;
    }

    /**
     * Initializes with a new Random instance.
     */
    public RandomStrategy() {
        this(new Random());
    }

    @Override
    public T tileToClear() {
        int row = rand.nextInt(tilingState.getNumRows());
        int column = rand.nextInt(tilingState.getNumColumns());
        while (tilingState.get(row, column).isCleared() || tilingState.get(row, column).isFlagged()) {
            row = rand.nextInt(tilingState.getNumRows());
            column = rand.nextInt(tilingState.getNumColumns());
        }
        return tilingState.get(row, column);
    }

    @Override
    public String name() {
        return "RANDOM";
    }
}
