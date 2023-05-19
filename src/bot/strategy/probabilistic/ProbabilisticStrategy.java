package src.bot.strategy.probabilistic;
import java.util.Random;

import src.bot.strategy.BotStrategy;
import src.minesweeper.Minesweeper;
import src.minesweeper.Tile;


/**
 * Probabilistic minesweeper strategy.
 * Algorithm inspired by: https://codereview.stackexchange.com/questions/54737/analyzing-minesweeper-probabilities
 */
public class ProbabilisticStrategy extends BotStrategy {

    private Random random;
    // ruleManager is not initialized until tileToClear is called
    private RuleManager ruleManager;

    public ProbabilisticStrategy(Minesweeper minesweeper, Random random) {
        super(minesweeper);
        this.random = random;
    }

    public ProbabilisticStrategy(Minesweeper minesweeper) {
        this(minesweeper, new Random());
    }

    @Override
    public Tile tileToClear() {
        ruleManager = new RuleManager(minesweeper);
        Tile tile = ruleManager.tileToClear(random);
        return tile;
    }

    @Override
    public String name() {
        return "PROBABILISTIC";
    }
}
