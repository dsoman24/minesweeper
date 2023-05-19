package src.bot.strategy;

import src.minesweeper.Minesweeper;
import src.minesweeper.Status;
import src.minesweeper.Tile;

/**
 * Strategy abstract class.
 */
public abstract class BotStrategy {

    protected Minesweeper minesweeper;

    public BotStrategy(Minesweeper minesweeper) {
        this.minesweeper = minesweeper;
    }

    /**
     * Run moves until completion
     */
    public Status runGame() {
        while (minesweeper.status() == Status.PLAYING) {
            Tile tile = tileToClear();
            minesweeper.clear(tile.getRow(), tile.getColumn());
        }
        return minesweeper.status();
    }

    /**
     * Finds the tile to clear according to the strategy.
     * @return the tile to clear
     */
    public abstract Tile tileToClear();

    /**
     * The name of the strategy.
     */
    public abstract String name();

}
