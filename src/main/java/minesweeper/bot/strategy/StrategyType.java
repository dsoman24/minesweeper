package src.main.java.minesweeper.bot.strategy;

import src.main.java.minesweeper.bot.strategy.linear.LinearStrategy;
import src.main.java.minesweeper.bot.strategy.probabilistic.ProbabilisticStrategy;
import src.main.java.minesweeper.bot.strategy.random.RandomStrategy;

/**
 * Enum to initialize strategies and essentially include all of them.
 * "one import fits all"
 * When creating new strategies, add them here.
 */
public enum StrategyType {
    LINEAR(new LinearStrategy()),
    RANDOM(new RandomStrategy()),
    PROBABILISTIC(new ProbabilisticStrategy());

    private BotStrategy strategy;
    private StrategyType(BotStrategy strategy) {
        this.strategy = strategy;
    }

    public BotStrategy getStrategy() {
        return strategy;
    }
}
