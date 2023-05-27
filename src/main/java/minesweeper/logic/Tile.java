package src.main.java.minesweeper.logic;

/**
 * Class to represent the tile
 */
public class Tile implements Tileable {
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

    @Override
    public int getNumberOfNeighboringMines() {
        return neighboringMines;
    }

    @Override
    public double initialDensity() {
        return initialDensity;
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
        return row * 5 + column * 7;
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
}