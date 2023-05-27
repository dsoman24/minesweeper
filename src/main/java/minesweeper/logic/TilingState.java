package src.main.java.minesweeper.logic;

import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Generic to represent the current TilingState
 */
public class TilingState<T extends Tileable> implements Iterable<T>{
    private T[][] tiles;
    private int rows;
    private int columns;

    private int numMines;

    private double density;

    @SuppressWarnings("unchecked")
    public TilingState(int rows, int columns, int numMines) {
        tiles = (T[][]) new Tileable[rows][columns];
        this.rows = rows;
        this.columns = columns;
        this.numMines = numMines;
        this.density = (double) numMines / (rows * columns);
    }

    public void updateDensity(double density) {
        this.density = density;
    }

    public void add(int row, int column, T tile) {
        tiles[row][column] = tile;
    }

    public T get(int row, int column) {
        return tiles[row][column];
    }

    public int getTotalNumberOfMines() {
        return numMines;
    }

    @Override
    public String toString() {
        String state = "";
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                state += get(i, j) + " ";
            }
            state += "\n";
        }
        return state;
    }

    public boolean isInBounds(int row, int column) {
        return row >= 0 && row < rows
            && column >= 0 && column < columns
            && row * column <= (rows - 1) * (columns - 1) && row * column >= 0;
    }

    /**
     * Get neighbors of a given Tileable.
     */
    public Set<T> getNeighbors(T tile) {
        Set<T> neighbors = new HashSet<>();
        for (int i = tile.getRow() - 1; i < tile.getRow() + 2; i++) {
            for (int j = tile.getColumn() - 1; j < tile.getColumn() + 2; j++) {
                if (!(i == tile.getRow() && j == tile.getColumn()) && isInBounds(i, j)) {
                    neighbors.add(get(i, j));
                }
            }
        }
        return neighbors;
    }

    public Set<T> getClearedAndNumberedNeighbors(T tile) {
        Set<T> cleared = new HashSet<>();
        Set<T> neighbors = getNeighbors(tile);
        for (T neighbor : neighbors) {
            if (neighbor.isCleared()) {
                cleared.add(neighbor);
            }
        }
        return cleared;
    }

    public int getNumRows() {
        return rows;
    }

    public int getNumColumns() {
        return columns;
    }

    @Override
    public Iterator<T> iterator() {
        return new TilingStateIterator();
    }

    private class TilingStateIterator implements Iterator<T> {

        private int currentRow = 0;
        private int currentColumn = 0;

        @Override
        public boolean hasNext() {
            return currentRow < rows && currentColumn < columns;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No more tiles in this TilingState");
            }
            T returnTile = tiles[currentRow][currentColumn];

            if (++currentRow >= rows) {
                currentRow = 0;
                currentColumn++;
            };

            return returnTile;
        }

    }

    public double density() {
        return density;
    }

}