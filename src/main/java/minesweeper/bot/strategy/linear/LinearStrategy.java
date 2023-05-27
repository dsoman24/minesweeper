package src.main.java.minesweeper.bot.strategy.linear;

import src.main.java.minesweeper.bot.strategy.BotStrategy;
import src.main.java.minesweeper.logic.Tileable;

public class LinearStrategy<T extends Tileable> extends BotStrategy<T> {

    private int row = 0;
    private int column = 0;

    @Override
    public T tileToClear() {
        updatePosition();
        while (tilingState.get(row, column).isCleared() || tilingState.get(row, column).isFlagged()) {
            updatePosition();
        }
        return tilingState.get(row, column);
    }

    private void updatePosition() {
        if (++row >= tilingState.getNumRows()) {
            row = 0;
            column++;
        }
    }

    @Override
    public String name() {
        return "LINEAR";
    }
}
