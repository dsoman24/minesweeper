package src.main.java.minesweeper.bot.strategy.random;
import java.util.Random;

import src.main.java.minesweeper.bot.strategy.BotStrategy;
import src.main.java.minesweeper.logic.Tileable;

public class RandomStrategy<T extends Tileable> extends BotStrategy<T> {

    private Random rand;

    public RandomStrategy(Random rand) {
        this.rand = rand;
    }

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
