package src.main.java.minesweeper.bot.strategy.linear;

import src.main.java.minesweeper.bot.strategy.BotStrategy;
import src.main.java.minesweeper.logic.MinesweeperTileable;

public class LinearStrategy<T extends MinesweeperTileable> extends BotStrategy<T> {

    @Override
    public T tileToClear() {
        int row = 0;
        int column = 0;
        while (tilingState.get(row, column).isCleared() || tilingState.get(row, column).isFlagged()) {
            if (++row >= tilingState.getNumRows()) {
                row = 0;
                column++;
            };
        }
        return tilingState.get(row, column);
    }

    @Override
    public String name() {
        return "LINEAR";
    }
}
