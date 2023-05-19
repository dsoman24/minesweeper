package src.bot.strategy.linear;

import src.bot.strategy.BotStrategy;
import src.minesweeper.Minesweeper;
import src.minesweeper.Tile;

public class LinearStrategy extends BotStrategy {

    private int row = 0;
    private int column = 0;


    public LinearStrategy(Minesweeper minesweeper) {
        super(minesweeper);
    }

    @Override
    public Tile tileToClear() {
        updatePosition();
        while (minesweeper.getTileAt(row, column).isCleared() || minesweeper.getTileAt(row, column).isFlagged()) {
            updatePosition();
        }
        return minesweeper.getTileAt(row, column);
    }

    private void updatePosition() {
        row++;
        if (row >= minesweeper.getNumRows()) {
            row = 0;
            column++;
        }
    }
}