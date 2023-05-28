package src.main.java.minesweeper.bot.strategy;

import src.main.java.minesweeper.bot.strategy.linear.LinearStrategy;
import src.main.java.minesweeper.bot.strategy.probabilistic.ProbabilisticStrategy;
import src.main.java.minesweeper.bot.strategy.random.RandomStrategy;
import src.main.java.minesweeper.logic.Tile;

/**
 * Enum to initialize strategies and essentially include all of them.
 * This enum only has strategies that act on Tile objects.
 * NOT GENERIC!
 * "one import fits all".
 * When creating new strategies, add them here.
 */
public enum StrategyTypeOnTile {
    LINEAR(new LinearStrategy<Tile>()),
    RANDOM(new RandomStrategy<Tile>()),
    PROBABILISTIC(new ProbabilisticStrategy<Tile>());

    private BotStrategy<Tile> strategy;
    private StrategyTypeOnTile(BotStrategy<Tile> strategy) {
        this.strategy = strategy;
    }

    public BotStrategy<Tile> getStrategy() {
        return strategy;
    }
}
