package src.main.java.minesweeper.bot;

import src.main.java.minesweeper.bot.strategy.BotStrategy;
import src.main.java.minesweeper.logic.Tileable;
import src.main.java.minesweeper.logic.TilingState;

/**
 * Bot which will play one game of Minesweeper
 */
public class Bot<T extends Tileable> {
    /** The strategy this bot uses */
    private BotStrategy<T> strategy;

    public Bot(TilingState<T> tilingState, BotStrategy<T> strategy) {
        this.strategy = strategy;
        this.strategy.setTilingState(tilingState);
    }

    public BotStrategy<T> getStrategy() {
        return strategy;
    }

    /**
     * Perform one individual move based on the current minesweeper state.
     * @return the row and column number of the tile to clear
     */
    public T tileToClear() {
        return strategy.tileToClear();
    }

    public String strategyName() {
        return strategy.name();
    }
}
