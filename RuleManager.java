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

    public void printTileSetsAndRules() {
        System.out.println("TILESETS");
        for (TileSet tileSet : tileSets) {
            System.out.println(tileSet);
        }
        System.out.println("RULES");
        for (TileSetRule rule : rules) {
            System.out.println(rule);
        }
    }

    /**
     * Create all TileSets (variables) for the given minesweeper gamestate.
     */
    private void groupTiles() {
        int numRows = minesweeper.getNumRows();
        int numCols = minesweeper.getNumColumns();

        for (int i = 0; i < numCols; i++) {
            for (int j = 0; j < numRows; j++) {
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
    private void makeRules() {
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

    public void calculateAndAssignProbabilities() {
        List<Integer> numCombinationsPerSolution = supposedResults.numCombinationsPerSolution();
        Combinatorics.calculateFactorials();
        int totalNumCombinations = supposedResults.totalNumCombinations();
        for (TileSet tileSet : tileSets) {
            double probability = 0.0;
            for (int i = 0; i < supposedResults.getNumberOfResultNodes(); i++) {
                int numMines = supposedResults.getNumMinesAtSpecificResultNode(i, tileSet);
                probability += ((double) numMines / tileSet.size()) * numCombinationsPerSolution.get(i);
            }
            probability /= totalNumCombinations;
            tileSet.setProbability(probability);
        }
    }

    // private TileSet findSmallestTileSet() {
    //     TileSet smallest = tileSets.get(0);
    //     for (TileSet tileSet : tileSets) {
    //         if (smallest.size() > tileSet.size()) {
    //             smallest = tileSet;
    //         }
    //     }
    //     return smallest;
    // }

    // public void solve() {
    //     System.out.println("***********");
    //     System.out.println("Number of TileSets: " + tileSets.size());
    //     // for (TileSet tileSet : tileSets) {
    //     //     System.out.println(tileSet);
    //     // }
    //     System.out.println("Number of Rules: " + rules.size());
    //     // for (TileSetRule rule : rules) {
    //     //     System.out.println(rule);
    //     // }
    //     TileSet smallest = findSmallestTileSet();
    //     int numIterations = smallest.size();
    //     // System.out.println("Smallest selected 1: " + smallest);
    //     for (int i = 0; i <= numIterations; i++) {
    //         Map<TileSet, Integer> result = new HashMap<>();
    //         for (TileSet tileSet : tileSets) {
    //             result.put(tileSet, null);
    //         }
    //         result.put(smallest, i);
    //         result = simplifyAllRulesGivenSupposedResult(result);
    //         solveHelper(result);
    //     }
    //     System.out.println("Number of Solutions: " + supposedResults.getNumberOfSupposedResults());
    //     System.out.println(supposedResults);
    //     System.out.println("--------");
    // }

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
        System.out.println("SOLUTIONS");
        System.out.println("Number of Solutions: " + supposedResults.getNumberOfResultNodes());
        System.out.println(supposedResults);
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

    public TileSet leastLikelyTileSet() {
        TileSet leastLikely = tileSets.get(0);
        for (TileSet tileSet : tileSets) {
            if (tileSet.getProbability() < leastLikely.getProbability()) {
                leastLikely = tileSet;
            }
        }
        return leastLikely;
    }

    private int[] tileToClear(Random random) {
        int[] coordinates = new int[2]; // [row, column]
        TileSet leastLikelySet = leastLikelyTileSet();
        Tile randomLeastLikelyTile = leastLikelySet.selectRandomTile(random); // picks a random tile from the least likely set
        coordinates[0] = randomLeastLikelyTile.getRow();
        coordinates[1] = randomLeastLikelyTile.getColumn();
        // System.out.println(String.format("Clearing (%d, %d) with probability %.4f", coordinates[0], coordinates[1], leastLikelySet.getProbability()));
        return coordinates;
    }

    /**
     * Driver method
     */
    public int[] solve(Random random) {
        System.out.println("*************");
        groupTiles();
        makeRules();
        printTileSetsAndRules();
        solveAllPossibilities();
        // calculateAndAssignProbabilities();
        int[] tileToClear = tileToClear(random);
        System.out.println("--------------------");
        return tileToClear;
        // add probability calculation into this method
    }

}
