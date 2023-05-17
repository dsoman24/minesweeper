import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Class to represent a rule.
 * This is essentially a linear equation. The TileSets are used instead of individual tiles as variables
 * to reduce # of computations
 */
public class TileSetRule {
    private List<TileSet> tileSets;
    private int result; // how many mines are within this rule
    private Tile resultTile;

    public TileSetRule(Tile resultTile, int result) {
        tileSets = new ArrayList<>();
        this.resultTile = resultTile;
        this.result = result;
    }

    public TileSetRule(Tile resultTile) {
        this(resultTile, resultTile.getNumNeighboringMines());
    }

    /**
     * "Shallow" copy constructor, not necessary to copy the tileSets
     */
    public TileSetRule(TileSetRule other) {
        this.tileSets = new ArrayList<>();
        for (TileSet tileSet : other.tileSets) {
            tileSets.add(tileSet);
        }
        this.result = other.result;
        this.resultTile = other.resultTile;
    }

    /**
     * Global rule constructor, takes in an integer. Rule is the global rule if the resultTile is null.
     */
    public TileSetRule(int result) {
        this(null, result);
    }

    public TileSetRule() {
        this(null, 0);
    }

    public int numTiles() {
        int size = 0;
        for (TileSet tileSet : tileSets) {
            size += tileSet.size();
        }
        return size;
    }

    public int numTileSets() {
        return tileSets.size();
    }

    public boolean isGlobalRule() {
        return resultTile == null;
    }

    public void addTileSet(TileSet tileSet) {
        tileSets.add(tileSet);
    }

    public int getResult() {
        return result;
    }

    public Tile getResultTile() {
        return resultTile;
    }

    /**
     * Simplifies a single rule based on a resultNode (current state).
     */
    public SimplifyResult simplify(ResultNode resultNode) {
        if (numTileSets() == 0 && result == 0) {
            return SimplifyResult.NO_EFFECT;
        }
        Iterator<TileSet> iterator = tileSets.iterator();

        // We only keep TileSets that have unknown values
        while (iterator.hasNext()) {
            Integer numMines = resultNode.get(iterator.next());
            if (numMines != null) {
                result -= numMines; // remove known TileSets from this rule
                iterator.remove();
            }
        }
        // invalid simplifications
        if (result < 0) {
            return SimplifyResult.NEGATIVE;
        }
        if (result > numTiles()) {
            return SimplifyResult.TOO_BIG;
        }


        // three simplification scenarios:
        // 1. something like [A] = X, clearly, TileSet [A] must have a value of X
        if (numTileSets() == 1) {
            resultNode.put(tileSets.get(0), result);
            tileSets.clear();
            result = 0;
            return SimplifyResult.SIMPLIFIED;
        }

        // 2. something like [A] + [B] = 0. Clearly, both [A] and [B] must be 0
        if (result == 0) {
            for (TileSet tileSet : tileSets) {
                resultNode.put(tileSet, 0);
            }
            tileSets.clear();
            result = 0;
            return SimplifyResult.SIMPLIFIED;
        }

        // 3. Something like [a + b + c] + [c + d] = 5. Clearly, everything must be 5.
        if (numTiles() == result) {
            for (TileSet tileSet : tileSets) {
                resultNode.put(tileSet, tileSet.size() * result / numTileSets());
            }
            return SimplifyResult.SIMPLIFIED;
        }
        return SimplifyResult.NO_EFFECT;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (TileSet tileSet : tileSets) {
            sb.append("[" + tileSet.hashCode() + "]");
            if (i < tileSets.size() - 1) {
                sb.append(" + ");
            }
            i++;
        }
        sb.append(" = " + result + " : (" + (isGlobalRule() ? "GLOBAL" : resultTile.getRow() + ", " + resultTile.getColumn())+ ")");
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int hash = 0;
        for (TileSet tileSet : tileSets) {
            hash += tileSet.hashCode();
        }
        hash += result;
        if (resultTile != null) {
            hash += resultTile.hashCode();
        }
        return hash;
    }
}
