package src.main.java.minesweeper.bot.strategy.probabilistic;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import src.main.java.minesweeper.logic.MinesweeperTileable;

/**
 * Class to represent a rule.
 * This is essentially a linear equation. The TileSets are used instead of individual tiles as variables
 * to reduce # of computations
 */
public class TileSetRule<T extends MinesweeperTileable> {
    private List<TileSet<T>> tileSets;
    private int result; // how many mines are within this rule
    private T resultTile;

    /**
     * 2-arg constructor, used to initialize all fields. Only private
     * @param resultTile the result tile to build this rule around. This can be null.
     * @param result the result to use.
     */
    private TileSetRule(T resultTile, int result) {
        tileSets = new ArrayList<>();
        this.resultTile = resultTile;
        this.result = result;
    }

    /**
     * 1-arg constructor.
     * @param resultTile the tile to build this rule around.
     */
    public TileSetRule(T resultTile) {
        this(resultTile, resultTile.getNumberOfNeighboringMines());
    }

    /**
     * "Shallow" copy constructor, not necessary to copy the tileSets
     * @param other the other rule to shallow copy.
     */
    public TileSetRule(TileSetRule<T> other) {
        this.tileSets = new ArrayList<>();
        for (TileSet<T> tileSet : other.tileSets) {
            tileSets.add(tileSet);
        }
        this.result = other.result;
        this.resultTile = other.resultTile;
    }

    /**
     * Global rule constructor, takes in an integer. Rule is the global rule if the resultTile is null.
     * @param the result of this rule, sets the resultTile to null.
     */
    public TileSetRule(int result) {
        this(null, result);
    }

    /**
     * @return the number of tiles in the entire rule.
     */
    public int numTiles() {
        int size = 0;
        for (TileSet<T> tileSet : tileSets) {
            size += tileSet.size();
        }
        return size;
    }

    /**
     * @return the number of TileSets in this rule.
     */
    public int numTileSets() {
        return tileSets.size();
    }

    /**
     * A rule is a global rule if the resultTile is null.
     * @return true if the rule is a global rule
     */
    private boolean isGlobalRule() {
        return resultTile == null;
    }

    /**
     * Adds a tileSet to this rule.
     * @param tileSet the tileSet to add
     */
    public void addTileSet(TileSet<T> tileSet) {
        tileSets.add(tileSet);
    }

    /**
     * @return the result of this tileset i.e. the beta.
     */
    public int getResult() {
        return result;
    }

    /**
     * @return the result tile of this rule.
     */
    public T getResultTile() {
        return resultTile;
    }

    /**
     * Simplifies a single rule based on a resultNode (current state). See README for details on simplification.
     * @param resultNode the result node to update based on the simplification.
     * @return the result of the simplification
     */
    public SimplifyResult simplify(ResultNode<T> resultNode) {
        if (numTileSets() == 0 && result == 0) {
            return SimplifyResult.NO_EFFECT;
        }

        Iterator<TileSet<T>> iterator = tileSets.iterator();
        // We only keep TileSets that have unknown values
        while (iterator.hasNext()) {
            TileSet<T> tileSet = iterator.next();
            Integer numMines = resultNode.get(tileSet);
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
            for (TileSet<T> tileSet : tileSets) {
                resultNode.put(tileSet, 0);
            }
            tileSets.clear();
            result = 0;
            return SimplifyResult.SIMPLIFIED;
        }

        // 3. Something like [a + b + c] + [c + d] = 5. Clearly, everything must be 5.
        if (numTiles() == result) {
            for (TileSet<T> tileSet : tileSets) {
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
        for (TileSet<T> tileSet : tileSets) {
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
        for (TileSet<T> tileSet : tileSets) {
            hash += tileSet.hashCode();
        }
        hash += result;
        return hash;
    }

    @Override
    @SuppressWarnings("unchecked")
    // equal if they have the same tilesets and result
    public boolean equals(Object obj) {
        if (obj instanceof TileSetRule<?>) {
            TileSetRule<T> other = (TileSetRule<T>) obj;
            if (result != other.result) {
                return false;
            }
            if (tileSets.size() != other.tileSets.size()) {
                return false;
            }
            for (TileSet<T> tileSet : tileSets) {
                if (!other.tileSets.contains(tileSet)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
