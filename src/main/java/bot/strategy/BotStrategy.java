package src.main.java.bot.strategy;

import src.main.java.minesweeper.Minesweeper;
import src.main.java.minesweeper.Status;
import src.main.java.minesweeper.Tile;

/**
 * Strategy abstract class.
 */
public abstract class BotStrategy {

    /**
     * Bot class initializes the minesweeper instance.
     */
    protected Minesweeper minesweeper;

    public void setMinesweeper(Minesweeper minesweeper) {
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
