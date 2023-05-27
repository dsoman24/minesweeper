package src.main.java.minesweeper.bot.strategy.probabilistic;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import src.main.java.minesweeper.logic.Tileable;

public class ResultNode<T extends Tileable> {

    private Map<TileSet<T>, Integer> tileSetResults; // data
    private List<TileSetRule<T>> rules; // keeps track of all simplified rules

    public ResultNode(Map<TileSet<T>, Integer> tileSetResults, List<TileSetRule<T>> rules) {
        this.tileSetResults = tileSetResults;
        this.rules = rules;
    }

    /**
     * Copy constructor.
     * Deep copies the rules but not the tileSets
     */
    public ResultNode(ResultNode<T> other) {
        Map<TileSet<T>, Integer> newTileSetResults = new HashMap<>();
        for (Map.Entry<TileSet<T>, Integer> entry : other.tileSetResults.entrySet()) {
            newTileSetResults.put(entry.getKey(), entry.getValue());
        }
        rules = new ArrayList<>();
        for (TileSetRule<T> rule : other.rules) {
            rules.add(new TileSetRule<>(rule));
        }
        this.tileSetResults = newTileSetResults;
    }

    public boolean isComplete() {
        for (Map.Entry<TileSet<T>, Integer> entry : tileSetResults.entrySet()) {
            if (entry.getValue() == null) {
                return false;
            }
        }
        return true;
    }

    public void setTileSetResults(Map<TileSet<T>, Integer> tileSetResults) {
        this.tileSetResults = tileSetResults;
    }

    public boolean isInvalidResult() {
        return tileSetResults == null;
    }

    /**
     * Simplifies all the rules of this resultNode.
     * @return true if the simplificaiton was successful, false otherwise
     */
    public boolean simplifyAllRules() {
        for (TileSetRule<T> rule : rules) {
            SimplifyResult simplifyResult = rule.simplify(this);
            if (simplifyResult.isFailure()) {
                return false;
            }
        }
        return true;
    }

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

    public Integer get(TileSet<T> tileSet) {
        return tileSetResults.get(tileSet);
    }

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

    public void put(TileSet<T> tileSet, Integer numMines) {
        tileSetResults.put(tileSet, numMines);
    }
}
