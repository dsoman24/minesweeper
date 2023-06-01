package src.main.java.minesweeper.bot.strategy.probabilistic;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import src.main.java.minesweeper.logic.MinesweeperTileable;
import src.main.java.minesweeper.logic.TilingState;


/**
 * Class to solve a minesweeper game state and provide the best tile to clear.
 * See README for details on the solver algorithm.
 * @param <T> the generic Tileable object this bot can solve minesweeper games for.
 */
public class Solver<T extends MinesweeperTileable> {
    private List<TileSet<T>> tileSets;
    private List<TileSetRule<T>> rules; // map of ruleTile : TileSetRule
    private SolutionSet<T> solutionSet;
    private TilingState<T> tilingState;

    /**
     * Initializes the solver with the minesweeper gamestate to solve, and empty tileSets, rules, and solutionSet.
     * @param tilingState the game state to initialize the solver with
     */
    public Solver(TilingState<T> tilingState) {
        this.tilingState = tilingState;
        this.tileSets = new ArrayList<>();
        this.rules = new ArrayList<>();
        this.solutionSet = new SolutionSet<>();
    }

    /**
     * Creates all TileSets (variables) for the given minesweeper gamestate.
     */
    private void createTileSets() {
        int numRows = tilingState.getNumRows();
        int numCols = tilingState.getNumColumns();

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                // only add the tile to the tileset if it is not cleared and not flagged
                if (validTileForTileSet(tilingState.get(i, j))) {
                    boolean added = false;
                    for (TileSet<T> tileSet : tileSets) {
                        added = tileSet.add(tilingState.get(i, j));
                        if (added) {
                            break;
                        }
                    }
                    if (!added) {
                        tileSets.add(new TileSet<>(tilingState.get(i, j), tilingState));
                    }
                }
            }
        }
    }

    private boolean validTileForTileSet(T tile) {
        return !tile.isCleared() && !tile.isFlagged();
    }

    /**
     * Create all TileSetRules (linear equations) for the given minesweeper gamestate.
     */
    private void createRules() {
        TileSetRule<T> globalRule = new TileSetRule<T>(tilingState.getTotalNumberOfMines());
        for (TileSet<T> tileSet : tileSets) {
            for (T ruleTile : tileSet.getCommonClearedNeighbors()) {
                TileSetRule<T> rule = getRuleByResultTile(ruleTile);
                if (rule == null) { // if a rule with that result tile does not exist, we create and add it
                    rule = new TileSetRule<>(ruleTile);
                    rules.add(rule);
                }
                rule.addTileSet(tileSet); // we add the TileSet to the rule.
            }
            globalRule.addTileSet(tileSet);
        }
        rules.add(globalRule); // globalRule is the sum of all groups.
    }

    /**
     * Helper method to get the rule corresponding to the given result tile.
     * @return TileSetRule the tileset rule corresponding to the given result tile
     */
    private TileSetRule<T> getRuleByResultTile(T resultTile) {
        for (TileSetRule<T> rule : rules) {
            if (resultTile.equals(rule.getResultTile())) {
                return rule;
            }
        }
        return null;
    }

    /**
     * Calls recursive helper method.
     * Populates the solution set with complete result nodes.
     */
    private void buildSolutionSet() {
        Map<TileSet<T>, Integer> rootResult = new HashMap<>();
        for (TileSet<T> tileSet : tileSets) {
            rootResult.put(tileSet, null);
        }
        ResultNode<T> root = new ResultNode<>(rootResult, rules);
        buildSolutionSet(root);
    }

    /**
     * Recursive helper method to solve possibilities and fill out the solution set.
     * Pre-order traversal of the solution tree.
     */
    private void buildSolutionSet(ResultNode<T> current) {
        // is a leaf if there are no null values in the map
        if (current.isComplete()) {
            solutionSet.addResultNode(current);
            return;
        }
        // find smallest "unknown" node
        TileSet<T> smallest = current.findSmallestUnknownTileSet();
        System.out.println(smallest);
        // iterate over the possibilities e.g. a set with 2 tiles could have 0, 1, or 2 mines in it
        for (int i = 0; i <= Math.min(smallest.size(), tilingState.getTotalNumberOfMines()); i++) {
            // child is a new node that is a deep copy of current
            ResultNode<T> child = new ResultNode<>(current);
            // attempt to solve the rules supposing that alpha for smallest = i
            child.put(smallest, i);
            // simplify, and only proceed if simplifications were successful
            if (child.simplifyAllRules()) {
                buildSolutionSet(child);
            }
        }
    }

    /**
     * Assings probabilities to all TileSets.
     */
    private void calculateAndAssignProbabilities() {
        List<BigInteger> numCombinationsPerSolution = solutionSet.numCombinationsPerSolution();
        BigInteger totalNumCombinations = solutionSet.totalNumCombinations();
        for (TileSet<T> tileSet : tileSets) {
            BigDecimal probability = BigDecimal.ZERO;
            // BUG HERE, REMOVE CONDITIONAL CHECK FOR 0. IDEALLY IT DOESN'T NEED IT
            if (solutionSet.size() == 0) {
                probability = BigDecimal.valueOf(tilingState.density());
            } else {
                for (int i = 0; i < solutionSet.size(); i++) {
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
     * Returns a mapping of each tile in the minesweeper game to the corresponding probability in its encompassing TileSet.
     * @return the mapping of each tile to its corresponding probability
     */
    public Map<T, Double> getProbabilityMap() {
        Map<T, Double> probabilityMap = new HashMap<T, Double>();
        for (TileSet<T> tileSet : tileSets) {
            double probability = tileSet.getProbability();
            for (T tile : tileSet) {
                probabilityMap.put(tile, probability);
            }
        }
        return probabilityMap;
    }

    /**
     * Finds the first minimum likelihood tile set.
     */
    private TileSet<T> minimumLikelihoodTileSet() {
        TileSet<T> leastLikely = tileSets.get(0);
        for (TileSet<T> tileSet : tileSets) {
            if (tileSet.getProbability() < leastLikely.getProbability()) {
                leastLikely = tileSet;
            }
        }
        return leastLikely;
    }

    /**
     * @return a random tile within the minimum likelihood tile set.
     */
    private T findRandomMinimumLikelihoodTile(Random random) {
        TileSet<T> leastLikelySet = minimumLikelihoodTileSet();
        // picks a random tile from the least likely set
        T randomLeastLikelyTile = leastLikelySet.selectRandomTile(random);
        return randomLeastLikelyTile;
    }

    /**
     * Wrapper method, performs the steps of the algorithm and finds the tile to clear.
     * See README for details.
     * @param random the random instance to use when picking the random tile.
     * @return the tile to clear according to this strategy.
     */
    public T tileToClear(Random random) {
        System.out.println("********************************");
        // 1. Group tiles into TileSets.
        createTileSets();
        // 2. Create rules based on cleared and non-zero numbered tiles.
        createRules();
        System.out.println("Number of rules: " + rules.size());
        // for (TileSetRule<T> rule : rules) {
        //     System.out.println(rule);
        // }

        // 3. Create all possible solutions
        buildSolutionSet();
        System.out.println("Size of result set: " + solutionSet.size());

        // 4. Calculate the probabilities of each TileSet
        calculateAndAssignProbabilities();
        // 5. Choose the least-likely tile
        T tileToClear = findRandomMinimumLikelihoodTile(random);
        return tileToClear;
    }

    /**
     * Finds all the tiles with probability 1.0 +/- epsilon (0.0001).
     * @return a list of tiles to flag.
     */
    public List<T> tilesToFlag() {
        double epsilon = 0.0001;
        List<T> tilesToFlag = new ArrayList<>();
        for (TileSet<T> tileSet : tileSets) {
            double probability = tileSet.getProbability();
            if (probability > 1.0 - epsilon && probability < 1.0 + epsilon) {
                for (T tile : tileSet) {
                    tilesToFlag.add(tile);
                }
            }
        }
        return tilesToFlag;
    }
}
