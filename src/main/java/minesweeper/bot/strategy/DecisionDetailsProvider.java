package src.main.java.minesweeper.bot.strategy;

import java.util.Map;

import src.main.java.minesweeper.logic.MinesweeperTileable;

public interface DecisionDetailsProvider<T extends MinesweeperTileable> {

    /**
     * Always call after tileToClear().
     * @return a mapping of tiles to some metric used in the decision to clear, for instance probability of a mine.
     */
    Map<T, ? extends Number> decisionDetails();
}
