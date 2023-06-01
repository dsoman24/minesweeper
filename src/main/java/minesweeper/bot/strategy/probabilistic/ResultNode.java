package src.main.java.minesweeper.bot.strategy.probabilistic;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import src.main.java.minesweeper.logic.MinesweeperTileable;

/**
 * Node of the solution tree.
 * Stores information about the rules and their associated alpha and beta values.
 */
public class ResultNode<T extends MinesweeperTileable> {

    private Map<TileSet<T>, Integer> tileSetResults; // tilesets and alphas
    private List<TileSetRule<T>> rules; // keeps track of all simplified rules

    /**
     * Create a new result node.
     * @param tileSetReults the mapping of TileSet : alphas.
     * @param rules the rules that are being evaluated.
     */
    public ResultNode(Map<TileSet<T>, Integer> tileSetResults, List<TileSetRule<T>> rules) {
        this.tileSetResults = tileSetResults;
        this.rules = rules;
    }

    /**
     * Copy constructor.
     * Deep copies the rules but not the tileSets.
     * @param other the ResultNode to deep copy.
     */
    public ResultNode(ResultNode<T> other) {
        tileSetResults = new HashMap<>();
        for (Map.Entry<TileSet<T>, Integer> entry : other.tileSetResults.entrySet()) {
            tileSetResults.put(entry.getKey(), entry.getValue());
        }
        rules = new ArrayList<>();
        for (TileSetRule<T> rule : other.rules) {
            rules.add(new TileSetRule<>(rule));
        }
    }

    /**
     * A complete ResultNode is one such that each alpha is known. That is, there is no null alpha.
     * @return true if the resultNode is complete, false otherwise.
     */
    public boolean isComplete() {
        for (Integer alpha : tileSetResults.values()) {
            if (alpha == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Simplifies all the rules of this resultNode.
     * @return true if the simplification was successful, false otherwise
     */
    public boolean simplifyAllRules() {
        removeDuplicateRules();
        for (TileSetRule<T> rule : rules) {
            SimplifyResult simplifyResult = rule.simplify(this);
            if (simplifyResult.isFailure()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Removes duplicates rules from a this result node's rules.
     */
    private void removeDuplicateRules() {
        rules = rules.stream().distinct().collect(Collectors.toList());
    }

    /**
     * @return the total number of combinations that can be done with this resultNode.
     */
    public BigInteger numCombinations() {
        BigInteger product = BigInteger.ONE;
        for (Map.Entry<TileSet<T>, Integer> entry : tileSetResults.entrySet()) {
            TileSet<T> tileSet = entry.getKey();
            int numMines = entry.getValue();
            product = product.multiply(Combinatorics.combinations(tileSet.size(), numMines));
        }
        return product;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<TileSet<T>, Integer> entry : tileSetResults.entrySet()) {
            sb.append("\n" + entry.getKey().hashCode() + " : " + entry.getValue());
        }
        return sb.toString();
    }

    /**
     * @return the alpha of a given TileSet
     */
    public Integer get(TileSet<T> tileSet) {
        return tileSetResults.get(tileSet);
    }

    /**
     * @return the smallest TileSet that has an unknown (null) alpha.
     */
    public TileSet<T> findSmallestUnknownTileSet() {
        TileSet<T> smallest = null;
        for (Map.Entry<TileSet<T>, Integer> entry : tileSetResults.entrySet()) {
            if (entry.getValue() == null) {
                if (smallest == null || entry.getKey().size() < smallest.size()) {
                    smallest = entry.getKey();
                }
            }
        }
        return smallest;
    }

    /**
     * Updates an alpha value.
     * @param tileSet the tile set to update
     * @param numMines the alpha value of this tile set.
     */
    public void put(TileSet<T> tileSet, Integer numMines) {
        tileSetResults.put(tileSet, numMines);
    }
}
