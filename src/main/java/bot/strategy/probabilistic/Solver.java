package src.main.java.bot.strategy.probabilistic;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import src.main.java.minesweeper.Minesweeper;
import src.main.java.minesweeper.Tile;


/**
 * Class to group all tiles within a minesweeper game into TileSets, and create TileSetRules based on these TileSets.
 * This class then creates the solution set based on these rules.
 */
public class Solver {
    private List<TileSet> tileSets;
    private List<TileSetRule> rules; // map of ruleTile : TileSetRule
    private SolutionSet solutionSet;
    private Minesweeper minesweeper;

    public Solver(Minesweeper minesweeper) {
        this.minesweeper = minesweeper;
        this.tileSets = new ArrayList<>();
        this.rules = new ArrayList<>();
        this.solutionSet = new SolutionSet();
    }

    /**
     * Create all TileSets (variables) for the given minesweeper gamestate.
     */
    private void createTileSets() {
        int numRows = minesweeper.getNumRows();
        int numCols = minesweeper.getNumColumns();

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                // only add the tile to the tileset if it is not cleared and not flagged
                if (validTileForTileSet(minesweeper.getTileAt(i, j))) {
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

    private boolean validTileForTileSet(Tile tile) {
        return !tile.isCleared() && !tile.isFlagged();
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
        List<BigInteger> numCombinationsPerSolution = solutionSet.numCombinationsPerSolution();
        BigInteger totalNumCombinations = solutionSet.totalNumCombinations();
        for (TileSet tileSet : tileSets) {
            BigDecimal probability = BigDecimal.ZERO;
            // BUG HERE, REMOVE CONDITIONAL CHECK FOR 0. IDEALLY IT DOESN'T NEED IT
            if (solutionSet.getNumberOfResultNodes() == 0) {
                probability = BigDecimal.valueOf(minesweeper.density());
            } else {
                for (int i = 0; i < solutionSet.getNumberOfResultNodes(); i++) {
                    int numMines = solutionSet.getNumMinesAtSpecificResultNode(i, tileSet);
                    BigDecimal decimal = new BigDecimal(numCombinationsPerSolution.get(i));
                    probability = probability.add(decimal.multiply(BigDecimal.valueOf((double) numMines / tileSet.size())));
                }
                probability = probability.divide(new BigDecimal(totalNumCombinations), MathContext.DECIMAL128);
            }
            tileSet.setProbability(probability.doubleValue());
        }
    }

    /**
     * Calls recursive helper method.
     * Fills out solution set
     */
    private void buildSolutionSet() {
        Map<TileSet, Integer> rootResult = new HashMap<>();
        for (TileSet tileSet : tileSets) {
            rootResult.put(tileSet, null);
        }
        ResultNode root = new ResultNode(rootResult, rules);
        buildSolutionSet(root);
    }

    /**
     * Recursive helper method to solve possibilities and fill out the solution set.
     * Pre-order traversal
     */
    private void buildSolutionSet(ResultNode current) {
        // is a leaf if there are no null values in the map
        if (current.isComplete()) {
            solutionSet.addResultNode(current);
            return;
        }
        // find smallest "unknown" node
        TileSet smallest = current.findSmallestUnknownTileSet();
        // iterate over the possibilities e.g. a set with 2 tiles could have 0, 1, or 2 mines in it
        for (int i = 0; i <= Math.min(smallest.size(), minesweeper.getNumMines()); i++) {
            // child is a new node that is a deep copy of current
            ResultNode child = new ResultNode(current);
            // attempt to solve the rules supposing that alpha for smallest = i
            child.put(smallest, i);
            boolean validSimplification = child.simplifyAllRules();
            if (validSimplification) {
                buildSolutionSet(child);
            }
        }
    }

    /**
     * Finds the first minium likelihood tile set
     */
    private TileSet minimumLikelihoodTileSet() {
        TileSet leastLikely = tileSets.get(0);
        for (TileSet tileSet : tileSets) {
            if (tileSet.getProbability() < leastLikely.getProbability()) {
                leastLikely = tileSet;
            }
        }
        return leastLikely;
    }

    private Tile findRandomMinimumLikelihoodTile(Random random) {
        TileSet leastLikelySet = minimumLikelihoodTileSet();
        // picks a random tile from the least likely set
        Tile randomLeastLikelyTile = leastLikelySet.selectRandomTile(random);
        return randomLeastLikelyTile;
    }

    /**
     * Wrapper method, performs the steps of the algorithm and finds the tile to clear.
     */
    public Tile tileToClear(Random random) {
        // 1. Group tiles into TileSets.
        createTileSets();
        // 2. Create rules based on cleared and non-zero numbered tiles.
        createRules();
        // 3. Create all possible solutions
        buildSolutionSet();
        // 4. Calculate the probabilities of each TileSet
        calculateAndAssignProbabilities();
        // 5. Choose the least-likely tile
        Tile tileToClear = findRandomMinimumLikelihoodTile(random);
        return tileToClear;
    }

    /**
     * Would it be smart to flag all tiles with probability 1?
     */
    public List<Tile> tilesToFlag() {
        List<Tile> tilesToFlag = new ArrayList<>();
        for (TileSet tileSet : tileSets) {
            for (Tile tile : tileSet) {
                tilesToFlag.add(tile);
            }
        }
        return tilesToFlag;
    }
}
