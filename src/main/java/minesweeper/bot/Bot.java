package src.main.java.minesweeper.bot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import src.main.java.minesweeper.bot.strategy.BotStrategy;
import src.main.java.minesweeper.bot.strategy.DecisionDetailsProvider;
import src.main.java.minesweeper.bot.strategy.FlaggingStrategy;
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
     * @return the next tile to clear according to the strategy.
     */
    public T tileToClear() {
        return strategy.tileToClear();
    }

    /**
     * @return the decision details, e.g. numerical metrics on what tile is being cleared.
     */
    @SuppressWarnings("unchecked")
    public Map<T, ? extends Number> decisionDetails() {
        if (strategy instanceof DecisionDetailsProvider) {
            return ((DecisionDetailsProvider<T>) strategy).decisionDetails();
        }
        return new HashMap<>();
    }

    /**
     * @return a list of the tiles to flag according to the strategy.
     */
    @SuppressWarnings("unchecked")
    public List<T> tilesToFlag() {
        if (strategy instanceof FlaggingStrategy) {
            return ((FlaggingStrategy<T>) strategy).tilesToFlag();
        }
        return new ArrayList<>();
    }

    /**
     * @return the name of the strategy this bot uses.
     */
    public String strategyName() {
        return strategy.name();
    }
}
