import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Class to group all tiles within a minesweeper game into TileSets, and create TileSetRules based on these TileSets.
 */
public class RuleManager {
    private List<TileSet> tileSets;
    private List<TileSetRule> rules; // map of ruleTile : TileSetRule
    private SupposedResults supposedResults;
    private Minesweeper minesweeper;

    public RuleManager(Minesweeper minesweeper) {
        this.minesweeper = minesweeper;
        this.tileSets = new ArrayList<>();
        this.rules = new ArrayList<>();
        this.supposedResults = new SupposedResults();
    }

    // private void printTileSetsAndRules() {
    //     System.out.println("TILESETS");
    //     for (TileSet tileSet : tileSets) {
    //         System.out.println(tileSet);
    //     }
    //     System.out.println("RULES");
    //     for (TileSetRule rule : rules) {
    //         System.out.println(rule);
    //     }
    // }

    /**
     * Create all TileSets (variables) for the given minesweeper gamestate.
     */
    private void createTileSets() {
        int numRows = minesweeper.getNumRows();
        int numCols = minesweeper.getNumColumns();

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                if (!minesweeper.getTileAt(i, j).isCleared()) {
                    boolean added = false;
                    for (TileSet tileSet : tileSets) {
                        added = tileSet.add(minesweeper.getTileAt(i, j));
                        if (added) {
                            break;
                        }
                    }
                    if (!added) {
                        tileSets.add(new TileSet(minesweeper.getTileAt(i, j)));
                    }
                }
            }
        }
    }

    /**
     * Create all TileSetRules (linear equations) for the given minesweeper gamestate.
     */
    private void createRules() {
        TileSetRule globalRule = new TileSetRule(minesweeper.getFlagsRemaining());
        for (TileSet tileSet : tileSets) {
            for (Tile ruleTile : tileSet.getCommonClearedNeighbors()) {
                TileSetRule rule = getRuleByResultTile(ruleTile);
                if (rule == null) { // if a rule with that result tile does not exist, we create and add it
                    rule = new TileSetRule(ruleTile);
                    rules.add(rule);
                }
                rule.addTileSet(tileSet); // we add the TileSet to the rule.
            }
            globalRule.addTileSet(tileSet);
        }
        rules.add(globalRule); // globalRule is the sum of all groups.
    }

    private TileSetRule getRuleByResultTile(Tile resultTile) {
        for (TileSetRule rule : rules) {
            if (resultTile.equals(rule.getResultTile())) {
                return rule;
            }
        }
        return null;
    }

    private void calculateAndAssignProbabilities() {
        List<BigInteger> numCombinationsPerSolution = supposedResults.numCombinationsPerSolution();
        BigInteger totalNumCombinations = supposedResults.totalNumCombinations();
        for (TileSet tileSet : tileSets) {
            BigDecimal probability = BigDecimal.ZERO;
            for (int i = 0; i < supposedResults.getNumberOfResultNodes(); i++) {
                int numMines = supposedResults.getNumMinesAtSpecificResultNode(i, tileSet);
                BigDecimal decimal = new BigDecimal(numCombinationsPerSolution.get(i));
                probability = probability.add(decimal.multiply(BigDecimal.valueOf((double) numMines / tileSet.size())));
            }
            if (probability.compareTo(BigDecimal.ZERO) != 0) {
                probability = probability.divide(new BigDecimal(totalNumCombinations), MathContext.DECIMAL32);
            }
            tileSet.setProbability(probability.doubleValue());
        }
    }

    /**
     * Calls recursive helper method.
     * Fills out supposed results
     */
    private void solveAllPossibilities() {
        Map<TileSet, Integer> rootResult = new HashMap<>();
        for (TileSet tileSet : tileSets) {
            rootResult.put(tileSet, null);
        }
        ResultNode root = new ResultNode(rootResult, rules);
        solveHelper(root);
    }

    /**
     * Recursive helper method to solve possibilities.
     * Pre-order traversal
     */
    private void solveHelper(ResultNode current) {
        // is a leaf if there are no null values in the map
        if (current.isLeaf()) {
            supposedResults.addResultNode(current);
            return;
        }
        // find smallest "unknown" node
        TileSet smallest = current.findSmallestUnknownTileSet();
        // iterate over the possibilities e.g. a set with 2 tiles could have 0, 1, or 2 mines in it
        for (int i = 0; i <= Math.min(smallest.size(), minesweeper.getNumMines()); i++) {
            ResultNode child = new ResultNode(current);
            child.put(smallest, i);
            boolean validSimplification = child.simplifyAllRules();
            if (validSimplification) {
                solveHelper(child);
            }
        }
    }

    private TileSet leastLikelyTileSet() {
        TileSet leastLikely = tileSets.get(0);
        for (TileSet tileSet : tileSets) {
            if (tileSet.getProbability() < leastLikely.getProbability()) {
                leastLikely = tileSet;
            }
        }
        return leastLikely;
    }

    private int[] findLeastLikelyTile(Random random) {
        int[] coordinates = new int[2]; // [row, column]
        TileSet leastLikelySet = leastLikelyTileSet();
        Tile randomLeastLikelyTile = leastLikelySet.selectRandomTile(random); // picks a random tile from the least likely set
        coordinates[0] = randomLeastLikelyTile.getRow();
        coordinates[1] = randomLeastLikelyTile.getColumn();
        // System.out.println(String.format("Clearing (%d, %d) with probability %.4f", coordinates[0], coordinates[1], leastLikelySet.getProbability()));
        return coordinates;
    }

    /**
     * Wrapper method, performs the steps of the algorithm.
     */
    public int[] solve(Random random) {
        // 1. Group tiles into TileSets.
        createTileSets();
        // 2. Create rules based on cleared and non-zero numbered tiles.
        createRules();
        // printTileSetsAndRules();
        // 3. Create all possible solutions
        solveAllPossibilities();
        // System.out.println("supposed results");
        // System.out.println(supposedResults);
        // 4. Calculate the probabilities of each TileSet
        calculateAndAssignProbabilities();
        // 5. Choose the least-likely tile
        int[] tileToClear = findLeastLikelyTile(random);
        // System.out.println("--------------------");
        return tileToClear;
        // add probability calculation into this method
    }

}
