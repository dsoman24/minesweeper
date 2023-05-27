package src.main.java.minesweeper.bot;

import src.main.java.minesweeper.bot.strategy.BotStrategy;
import src.main.java.minesweeper.bot.strategy.StrategyType;
import src.main.java.minesweeper.logic.Minesweeper;
import src.main.java.minesweeper.logic.Status;
import src.main.java.minesweeper.logic.Tile;

/**
 * Bot which will play one game of Minesweeper
 */
public class Bot {
    /** The strategy this bot uses */
    private BotStrategy strategy;

    public Bot(Minesweeper minesweeper, StrategyType strategyType) {
        this.strategy = strategyType.getStrategy();
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
