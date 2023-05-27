package src.main.java.minesweeper.bot.strategy;

import src.main.java.minesweeper.logic.Tileable;
import src.main.java.minesweeper.logic.TilingState;

/**
 * Strategy abstract class.
 */
public abstract class BotStrategy<T extends Tileable> {

    /**
     * Bot class initializes the minesweeper instance.
     */
    protected TilingState<T> tilingState;

    public void setTilingState(TilingState<T> tilingState) {
        this.tilingState = tilingState;
    }

    /**
     * Finds the tile to clear according to the strategy.
     * @return the tile to clear
     */
    public abstract T tileToClear();

    /**
     * The name of the strategy.
     */
    public abstract String name();

}
