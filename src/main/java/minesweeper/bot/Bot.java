package src.main.java.minesweeper.bot;

import java.util.Map;

import src.main.java.minesweeper.bot.strategy.BotStrategy;
import src.main.java.minesweeper.bot.strategy.DecisionDetailsProvider;
import src.main.java.minesweeper.logic.MinesweeperTileable;
import src.main.java.minesweeper.logic.TilingState;

/**
 * Bot which will play one move of game of Minesweeper.
 * Operates on a TilingState on generic tiles of type Tileable.
 * @param <T> the type of tile this bot operates on.
 *            Must be minesweeper-tilable.
 */
public class Bot<T extends MinesweeperTileable> {
    private BotStrategy<T> strategy;

    /**
     * @param tilingState the TilingState to operate on.
     * @param strategy the strategy to use.
     */
    public Bot(TilingState<T> tilingState, BotStrategy<T> strategy) {
        this.strategy = strategy;
        this.strategy.setTilingState(tilingState);
    }

    /**
     * Perform one individual move based on the current minesweeper state.
     * @return the tile to clear according to the strategy.
     */
    public T tileToClear() {
        return strategy.tileToClear();
    }

    /**
     * @return the name of the strategy this bot uses.
     */
    public String strategyName() {
        return strategy.name();
    }

    @SuppressWarnings("unchecked")
    public Map<T, ? extends Number> decisionDetails() {
        if (strategy instanceof DecisionDetailsProvider) {
            return ((DecisionDetailsProvider<T>) strategy).decisionDetails();
        }
        return null;
    }
}
