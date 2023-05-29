package src.main.java.minesweeper.bot.strategy.probabilistic;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import src.main.java.minesweeper.logic.MinesweeperTileable;
import src.main.java.minesweeper.logic.TilingState;


/**
 * Generic TileSet class.
 * Each tile is unique to a set, and is characterized by the neighboring numbered cleared tiles.
 * A TileSet is essentially the variable in the linear equation.
 * Each tile in the tileset is the assigned the probability of its encompassing Set
 */
public class TileSet<T extends MinesweeperTileable> implements Iterable<T>{
    private Set<T> set;
    private Set<T> commonClearedNeighbors;
    private TilingState<T> tilingState;
    private double probability;

    /**
     * @param tile the first tile to add to the tileset
     * @param tilingState the current game state
     */
    public TileSet(T tile, TilingState<T> tilingState) {
        set = new HashSet<>();
        set.add(tile);
        commonClearedNeighbors = tilingState.getInPlayNeighbors(tile);
        probability = tile.initialDensity(); // will have the same probability as the game density
        this.tilingState = tilingState;
    }

    /**
     * Add to this TileSet only if the tile is already within the commonClearedNeighbors
     * @param tile the tile to attempt to add
     * @return true if the tile is added (and shares common cleared neighbors), false otherwise
     */
    public boolean add(T tile) {
        if (equalClearedNeighbors(tile)) {
            set.add(tile);
            return true;
        }
        return false;
    }

    /**
     * Check if a tile shares common cleared neighbors to that of this TileSet.
     * @param other the tile to check
     * @return true if the tile has common cleared neighbors, false otherwise.
     */
    private boolean equalClearedNeighbors(T other) {
        Set<T> otherClearedNeighbors = tilingState.getInPlayNeighbors(other);
        if (commonClearedNeighbors.size() != otherClearedNeighbors.size()) {
            return false;
        }
        for (T tile : otherClearedNeighbors) {
            if (!commonClearedNeighborsContains(tile)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Helper method to check if a given tile has the same common cleared neighbors.
     * @param tile the tile to check
     * @return true if the tile has the same common cleared neighbors as this TileSet, false otherwise
     */
    private boolean commonClearedNeighborsContains(T tile) {
        return commonClearedNeighbors.contains(tile);
    }

    /**
     * @return the set of common cleared neighbors.
     */
    public Set<T> getCommonClearedNeighbors() {
        return commonClearedNeighbors;
    }

    /**
     * @return the probabily of this TileSet having a mine.
     */
    public double getProbability() {
        return probability;
    }

    /**
     * Sets the probability of this TileSet.
     * @param probability the probability of this TileSet having a mine
     */
    public void setProbability(double probability) {
        this.probability = probability;
    }

    /**
     * @return the size of this TileSet e.g. the number of tiles.
     */
    public int size() {
        return set.size();
    }

    /**
     * Selects a random tile within this TileSet.
     * @param random the random number generator to use
     * @return the random tile.
     */
    @SuppressWarnings("unchecked")
    public T selectRandomTile(Random random) {
        int index = random.nextInt(size());
        T[] tiles = set.toArray((T[]) new MinesweeperTileable[size()]);
        return tiles[index];
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        // sb.append(String.format("%.4f ", probability));
        sb.append(String.format("%d: ", hashCode()));
        for (T tile : set) {
            sb.append(String.format("(%d, %d) ", tile.getRow(), tile.getColumn()));
        }
        return sb.toString();
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        if (obj instanceof TileSet) {
            TileSet<T> other = (TileSet<T>) obj;
            if (size() != other.size()) {
                return false;
            }
            if (commonClearedNeighbors.size() != other.commonClearedNeighbors.size()) {
                return false;
            }
            for (T tile : set) {
                if (!other.set.contains(tile)) {
                    return false;
                }
            }
            for (T tile : commonClearedNeighbors) {
                if (!other.commonClearedNeighbors.contains(tile)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        for (T tile : set) {
            hash += tile.hashCode();
        }
        for (T tile : commonClearedNeighbors) {
            hash += tile.hashCode();
        }
        return hash;
    }

    @Override
    public Iterator<T> iterator() {
        return set.iterator();
    }
}
