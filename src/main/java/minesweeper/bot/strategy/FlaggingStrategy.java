package src.main.java.minesweeper.bot.strategy;

import java.util.List;

import src.main.java.minesweeper.logic.MinesweeperTileable;

/**
 * Interface that strategies that can flag must implement.
 */
public interface FlaggingStrategy<T extends MinesweeperTileable> {

    /**
     * @return a list of MinesweeperTileable tiles that the strategy suggest must be flagged.
     */
    List<T> tilesToFlag();
}
