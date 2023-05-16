import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

/**
 * TileSet class.
 * Each tile is unique to a set, and is characterized by the neighboring numbered cleared tiles.
 * A TileSet is essentially the variable in the linear equation.
 * Each tile in the tileset is the assigned the probability of its encompassing Set
 */
public class TileSet implements Iterable<Tile>{
    private Set<Tile> set;
    private Set<Tile> commonClearedNeighbors;
    private double probability;

    public TileSet(Tile tile) {
        set = new HashSet<>();
        set.add(tile);
        commonClearedNeighbors = tile.getClearedAndNumberedNeighbors();
        probability = 1; // assume it has mine to begin
    }

    /**
     * Add to this TileSet only if the tile is already within the commonClearedNeighbors
     * @return true if the tile is added (and shares common cleared neighbors), false otherwise
     */
    public boolean add(Tile tile) {
        if (equalClearedNeighbors(tile)) {
            set.add(tile);
            return true;
        }
        return false;
    }

    /**
     * Check if a tile shares common cleared neighbors to that of this TileSet
     */
    private boolean equalClearedNeighbors(Tile other) {
        Set<Tile> otherClearedNeighbors = other.getClearedAndNumberedNeighbors();
        if (commonClearedNeighbors.size() != otherClearedNeighbors.size()) {
            return false;
        }
        for (Tile tile : otherClearedNeighbors) {
            if (!commonClearedNeighborsContains(tile)) {
                return false;
            }
        }
        return true;
    }

    public boolean commonClearedNeighborsContains(Tile tile) {
        return commonClearedNeighbors.contains(tile);
    }

    public Set<Tile> getCommonClearedNeighbors() {
        return commonClearedNeighbors;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    public int size() {
        return set.size();
    }

    public Tile selectRandomTile(Random random) {
        int index = random.nextInt(size());
        Tile[] tiles = set.toArray(new Tile[size()]);
        return tiles[index];
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        // sb.append(String.format("%.4f ", probability));
        sb.append(String.format("%d: ", hashCode()));
        for (Tile tile : set) {
            sb.append(String.format("(%d, %d) ", tile.getRow(), tile.getColumn()));
        }
        // sb.append("[");
        // for (Tile tile : commonClearedNeighbors) {
        //     sb.append(String.format("(%d, %d) ", tile.getRow(), tile.getColumn()));
        // }
        // sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TileSet) {
            TileSet other = (TileSet) obj;
            if (size() != other.size()) {
                return false;
            }
            if (commonClearedNeighbors.size() != other.commonClearedNeighbors.size()) {
                return false;
            }
            for (Tile tile : set) {
                if (!other.set.contains(tile)) {
                    return false;
                }
            }
            for (Tile tile : commonClearedNeighbors) {
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
        for (Tile tile : set) {
            hash += tile.hashCode();
        }
        for (Tile tile : commonClearedNeighbors) {
            hash += tile.hashCode();
        }
        return hash;
    }

    @Override
    public Iterator<Tile> iterator() {
        return set.iterator();
    }
}
