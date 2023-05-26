package src.main.java.bot;

import src.main.java.bot.strategy.BotStrategy;
import src.main.java.minesweeper.Minesweeper;
import src.main.java.minesweeper.Status;
import src.main.java.minesweeper.Tile;

/**
 * Bot which will play one game of Minesweeper
 */
public class Bot {
    /** The strategy this bot uses */
    private BotStrategy strategy;

    public Bot(Minesweeper minesweeper, BotStrategy strategy) {
        this.strategy = strategy;
        this.strategy.setMinesweeper(minesweeper);
    }

    public BotStrategy getStrategy() {
        return strategy;
    }

    /**
     * Perform one individual move based on the current minesweeper state.
     * @return the row and column number of the tile to clear
     */
    public Tile tileToClear() {
        return strategy.tileToClear();
    }

    /**
     * Run a game until completion
     */
    public Status runGame() {
        return strategy.runGame();
    }

    public String strategyName() {
        return strategy.name();
    }
}
