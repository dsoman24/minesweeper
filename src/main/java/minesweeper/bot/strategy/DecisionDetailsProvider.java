package src.main.java.minesweeper.bot.strategy;

import java.util.Map;

import src.main.java.minesweeper.logic.MinesweeperTileable;

public interface DecisionDetailsProvider<T extends MinesweeperTileable> {

    /**
     * Returns a mapping of tiles to some metric used in the decision to clear, for instance probability.
     */
    Map<T, ? extends Number> decisionDetails();
}
