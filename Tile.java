import java.util.HashSet;
import java.util.Set;

/**
 * Class to represent the tile
 */
public class Tile {
    private int row;
        private int column;
        private int neighboringMines;
        private boolean mine;
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
            mine = false;
            cleared = false;
        }

        /**
         * Method to clear a tile.
         * Calls a recursive helper.
         * @return true if clear was successful (not a mine), false otherwise (lose condition)
         */
        public boolean clearSurroundingTiles() {
            if (mine) {
                return false;
            }
            clearSurroundingTilesHelper(this);
            return true;
        }

        /**
         * Recursive helper method to clear the tiles using DFS.
         * Clears all this tile and adjacent tiles if not visited, stops after it clears a non-zero or flagged tile.
         * @param curr the current tile we are clearing
         */
        private void clearSurroundingTilesHelper(Tile curr) {
            if (!curr.cleared && !curr.flag) {
                curr.cleared = true;
                minesweeper.incrementNumTilesCleared();
                if (curr.neighboringMines == 0) {
                    Set<Tile> neighbors = getNeighbors(curr);
                    for (Tile tile : neighbors) {
                        clearSurroundingTilesHelper(tile);
                    }
                }
            }
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

        public void addMine() {
            this.mine = true;
        }

        @Override
        public String toString() {
            return mine ? "M" : String.format("%d", neighboringMines);
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof Tile) {
                Tile other = (Tile) o;
                return this.row == other.row
                    && this.column == other.column
                    && this.neighboringMines == other.neighboringMines
                    && this.mine == other.mine;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return column * minesweeper.getNumRows() + row;
        }

        public boolean isFlagged() {
            return flag;
        }

        public boolean hasMine() {
            return mine;
        }

        public boolean isCleared() {
            return cleared;
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
