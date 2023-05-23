package src.minesweeper;
import java.util.HashSet;
import java.util.Set;

/**
 * Class to represent the tile
 */
public class Tile {
    private int row;
    private int column;
    private int neighboringMines;
    private boolean flag;
    private boolean cleared;
    private Minesweeper minesweeper;

    /**
     * Constructor for a tile.
     * This constructor is most accurately used after populating mines set.
     * @param row the row the tile is on
     * @param column the column the tile is on
     */
    public Tile(int row, int column, Minesweeper minesweeper) {
        this.row = row;
        this.column = column;
        this.minesweeper = minesweeper;
        cleared = false;
    }

    public int getNumNeighboringMines() {
        return neighboringMines;
    }

    /**
     * Get neighbors of a given Tile instance.
     */
    public Set<Tile> getNeighbors(Tile tile) {
        Set<Tile> neighbors = new HashSet<>();
        for (int i = tile.row - 1; i < tile.row + 2; i++) {
            for (int j = tile.column - 1; j < tile.column + 2; j++) {
                if (!(i == tile.row && j == tile.column) && minesweeper.isInBounds(i, j)) {
                    neighbors.add(minesweeper.getTileAt(i, j));
                }
            }
        }
        return neighbors;
    }

    /**
     * Get neighbors of this Tile instance.
     */
    public Set<Tile> getNeighbors() {
        return getNeighbors(this);
    }

    /**
     * Get neighbors of this Tile instance that have been cleared.
     */
    public Set<Tile> getClearedAndNumberedNeighbors() {
        Set<Tile> cleared = new HashSet<>();
        Set<Tile> neighbors = getNeighbors();
        for (Tile tile : neighbors) {
            if (tile.isCleared()) {
                cleared.add(tile);
            }
        }
        return cleared;
    }

    public void addNeighboringMine() {
        neighboringMines++;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }


    @Override
    public String toString() {
        return String.format("(%d, %d): %d", row, column, neighboringMines);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Tile) {
            Tile other = (Tile) o;
            return this.row == other.row
                && this.column == other.column
                && this.neighboringMines == other.neighboringMines;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return row * minesweeper.getNumColumns() + column;
    }

    public boolean isFlagged() {
        return flag;
    }

    public boolean isCleared() {
        return cleared;
    }

    public void setCleared() {
        cleared = true;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public double density() {
        return minesweeper.density();
    }
}
