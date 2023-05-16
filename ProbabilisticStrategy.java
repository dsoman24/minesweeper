import java.util.Random;


/**
 * Probabilistic minesweeper strategy
 * Algorithm inspired by: https://codereview.stackexchange.com/questions/54737/analyzing-minesweeper-probabilities
 */
public class ProbabilisticStrategy extends BotStrategy {

    private Random random;

    public ProbabilisticStrategy(Minesweeper minesweeper, Random random) {
        super(minesweeper);
        this.random = random;
    }

    public ProbabilisticStrategy(Minesweeper minesweeper) {
        this(minesweeper, new Random());
    }

    @Override
    public Status move() {
        minesweeper.removeAllFlags();
        RuleManager ruleManager = new RuleManager(minesweeper);
        int[] coordinates = ruleManager.solve(random);
        minesweeper.clear(coordinates[0], coordinates[1]);
        return minesweeper.status();
    }
}
