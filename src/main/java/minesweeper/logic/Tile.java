package src.main.java.minesweeper.logic;

/**
 * Class to represent the tile
 */
public class Tile implements MinesweeperTileable {
    private int row;
    private int column;
    private int neighboringMines;
    private boolean flag;
    private boolean cleared;
    private double initialDensity;

    /**
     * Constructor for a tile.
     * This constructor is most accurately used after populating mines set.
     * @param row the row the tile is on
     * @param column the column the tile is on
     */
    public Tile(int row, int column, double initialDensity) {
        this.row = row;
        this.column = column;
        cleared = false;
        this.initialDensity = initialDensity;
    }

    // Overriden from Tileable

    @Override()
    public int getNumberOfNeighboringMines() {
        return neighboringMines;
    }

    @Override
    public double initialDensity() {
        return initialDensity;
    }

    @Override
    public int hashCode() {
        return row * 5 + column * 7;
    }

    @Override
    public boolean isFlagged() {
        return flag;
    }

    @Override
    public boolean isCleared() {
        return cleared;
    }

    @Override
    public int getRow() {
        return row;
    }

    @Override
    public int getColumn() {
        return column;
    }

    // The only necessary methods for this concrete minesweeper implementation.

    public void incrementNumberOfNeighboringMines() {
        neighboringMines++;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public void setCleared() {
        cleared = true;
    }


    // Overriden from Object

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

}