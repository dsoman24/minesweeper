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
        // TileSet leastLikelySet = ruleManager.leastLikelyTileSet();
        // Tile randomLeastLikelyTile = leastLikelySet.selectRandomTile(random); // picks a random tile from the least likely set
        // System.out.println(String.format("Clearing (%d, %d) with probability %.4f", randomLeastLikelyTile.getRow(), randomLeastLikelyTile.getColumn(), leastLikelySet.getProbability()));
        // minesweeper.clear(coordinates[0], coordinates[1]);
        return minesweeper.status();
    }


    /*
     * IDEA:
     * 1. Group tiles into mutually disjoint sets (TileSet) characterized by which numbered and cleared tiles they are affected by. This decreases the number of computations necessary. Each tile in a set will have the same probability.
     * 2. Clear an arbitrary tile from the lowest probability TileSet and re-compute probabilities. Repeat step 1 until game ends.
     */
}
