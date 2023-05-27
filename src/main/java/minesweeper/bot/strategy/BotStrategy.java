package src.main.java.minesweeper.bot.strategy;

import src.main.java.minesweeper.logic.MinesweeperTileable;
import src.main.java.minesweeper.logic.TilingState;

/**
 * Strategy abstract class.
 * Contains method that returns which tile should be cleared.
 * @param <T> the type of tile to operate on, must be minesweeper-tileable.
 */
public abstract class BotStrategy<T extends MinesweeperTileable> {

    /**
     * Bot class initializes the TilingState instance using setTilingState().
     */
    protected TilingState<T> tilingState;

    /**
     * Sets the tilingState to evalue for this strategy.
     */
    public void setTilingState(TilingState<T> tilingState) {
        this.tilingState = tilingState;
    }

    /**
     * Finds the tile to clear according to the strategy.
     * @return the tile to clear
     */
    public abstract T tileToClear();

    /**
     * Each strategy has its own name.
     * @return the name of the strategy
     */
    public abstract String name();

}
