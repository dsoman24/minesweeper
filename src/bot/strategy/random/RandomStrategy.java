package src.bot.strategy.random;
import java.util.Random;

import src.bot.strategy.BotStrategy;
import src.minesweeper.Tile;

public class RandomStrategy extends BotStrategy {

    private Random rand;

    public RandomStrategy(Random rand) {
        this.rand = rand;
    }

    public RandomStrategy() {
        this(new Random());
    }

    @Override
    public Tile tileToClear() {
        int row = rand.nextInt(minesweeper.getNumRows());
        int column = rand.nextInt(minesweeper.getNumColumns());
        while (minesweeper.getTileAt(row, column).isCleared() || minesweeper.getTileAt(row, column).isFlagged()) {
            row = rand.nextInt(minesweeper.getNumRows());
            column = rand.nextInt(minesweeper.getNumColumns());
        }
        return minesweeper.getTileAt(row, column);
    }

    @Override
    public String name() {
        return "RANDOM";
    }
}
