import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultNode {

    private Map<TileSet, Integer> tileSetResults; // data
    private List<TileSetRule> rules; // keeps track of all simplified rules

    public ResultNode(Map<TileSet, Integer> tileSetResults, List<TileSetRule> rules) {
        this.tileSetResults = tileSetResults;
        this.rules = rules;
    }

    /**
     * Copy constructor.
     * Deep copies the rules but not the tileSets
     */
    public ResultNode(ResultNode other) {
        Map<TileSet, Integer> newTileSetResults = new HashMap<>();
        for (Map.Entry<TileSet, Integer> entry : other.tileSetResults.entrySet()) {
            newTileSetResults.put(entry.getKey(), entry.getValue());
        }
        rules = new ArrayList<>();
        for (TileSetRule rule : other.rules) {
            rules.add(new TileSetRule(rule));
        }
        this.tileSetResults = newTileSetResults;
    }

    public boolean isComplete() {
        for (Map.Entry<TileSet, Integer> entry : tileSetResults.entrySet()) {
            if (entry.getValue() == null) {
                return false;
            }
        }
        return true;
    }

    public void setTileSetResults(Map<TileSet, Integer> tileSetResults) {
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
        for (TileSetRule rule : rules) {
            SimplifyResult simplifyResult = rule.simplify(this);
            if (simplifyResult.isFailure()) {
                return false;
            }
        }
        return true;
    }

    public BigInteger numCombinations() {
        BigInteger product = BigInteger.ONE;
        for (Map.Entry<TileSet, Integer> entry : tileSetResults.entrySet()) {
            TileSet tileSet = entry.getKey();
            int numMines = entry.getValue();
            product = product.multiply(Combinatorics.combinations(tileSet.size(), numMines));
        }
        return product;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<TileSet, Integer> entry : tileSetResults.entrySet()) {
            sb.append("\n" + entry.getKey().hashCode() + " : " + entry.getValue());
        }
        return sb.toString();
    }

    public Integer get(TileSet tileSet) {
        return tileSetResults.get(tileSet);
    }

    public TileSet findSmallestUnknownTileSet() {
        TileSet smallest = null;
        for (Map.Entry<TileSet, Integer> entry : tileSetResults.entrySet()) {
            if (entry.getValue() == null) {
                if (smallest == null || entry.getKey().size() < smallest.size()) {
                    smallest = entry.getKey();
                }
            }
        }
        return smallest;
    }

    public void put(TileSet tileSet, Integer numMines) {
        tileSetResults.put(tileSet, numMines);
    }
}
