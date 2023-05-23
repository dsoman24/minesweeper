package src.minesweeper;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
/**
 * Minesweeper game implementation.
 * Logic only, no UI elements.
 * @author Daniel Öman
 * @date 12/31/2022
 * @version 1.0
 */
public class Minesweeper {

    private Tile[][] tiles;
    private Set<Tile> mineSet;
    private int rows;
    private int columns;
    private int numMines;
    private double initialDensity;

    private int flagsRemaining;

    private boolean playing;
    private boolean won;
    private int numTilesCleared;

    private int numMoves;

    private Random random;

    /** Time elapsed since game start */
    private long elapsedMillis;

    private long startTime;

    /**
     * where the start condition will not cause an infinite loop.
     */
    public Minesweeper(Difficulty difficulty) {
        this(difficulty, new Random());
    }

    public Minesweeper(Difficulty difficulty, Random random) {
        rows = difficulty.getNumRows();
        columns = difficulty.getNumColumns();
        initialDensity = difficulty.getDensity();
        numMines = difficulty.getNumMines();
        mineSet = new HashSet<>();
        playing = true;
        numTilesCleared = 0;
        flagsRemaining = numMines;
        tiles = new Tile[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                Tile tile = new Tile(i, j, this);
                tiles[i][j] = tile;
            }
        }
        this.random = random;
        this.elapsedMillis = 0;
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        String minefield = "";
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                minefield += tiles[i][j] + " ";
            }
            minefield += "\n";
        }
        return minefield;
    }

    /**
     * Public helper method to check if a position is within bounds.
     * @param row the row of the position to check
     * @param column the column of the position to check
     * @return if the position is within the bounds of the minefield
     */
    public boolean isInBounds(int row, int column) {
        return row >= 0 && row < rows
            && column >= 0 && column < columns
            && row * column <= (rows - 1) * (columns - 1) && row * column >= 0;
    }

    /**
     * Generate the game state. Will guarantee a 0 on start.
     */
    private void startGame(int startingRow, int startingColumn) {
        // Begin by randomly generating mines

        List<int[]> validPositions = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                validPositions.add(new int[]{i, j});
            }
        }
        for (int i = 0; i < numMines; i++) {

            int randomIndex = random.nextInt(validPositions.size());
            int[] minePosition = validPositions.get(randomIndex);
            int row = minePosition[0];
            int column = minePosition[1];

            // Guaranteed to not start on a mine, unless the density is 1.
            if (numMines <= rows * columns - 1) {
                while (row == startingRow && column == startingColumn) {
                    randomIndex = random.nextInt(validPositions.size());
                    minePosition = validPositions.get(randomIndex);
                    row = minePosition[0];
                    column = minePosition[1];
                }
            }

            validPositions.remove(randomIndex);

            // add to the mineset if it is a mine
            mineSet.add(tiles[row][column]);
            // now go around and increment the tiles immediately around it
            for (int x = row - 1; x < row + 2; x++) {
                for (int y = column - 1; y < column + 2; y++) {
                    if (!(x == row && y == column) && isInBounds(x, y)) {
                        tiles[x][y].addNeighboringMine();
                    }
                }
            }
        }
    }

    public Tile getTileAt(int row, int column) {
        return tiles[row][column];
    }

    public int getFlagsRemaining() {
        return flagsRemaining;
    }

    /**
     * Method to clear a single tile from the board.
     * Updates elapsed time upon clearing.
     * TO-DO use in bot functionality
     * @param row the row of the tile to clear
     * @param column the column of the tile to clear
     */
    public void clear(int row, int column) {
        if (playing) {
            Tile currentTile = tiles[row][column];
            if (!currentTile.isCleared() && !currentTile.isFlagged()) {
                // Generate the tiles if no tiles have been cleared.
                if (numTilesCleared == 0) {
                    startGame(row, column);
                }
                playing = clearSurroundingTiles(currentTile);
                if (playing && numTilesCleared == rows * columns - numMines) {
                    won = true;
                    playing = false;
                }
                numMoves++;
                updateElapsedTime();
            }
        }
    }

    /**
     * Recursive helper method to clear the tiles using DFS.
     * Clears all this tile and adjacent tiles if not visited, stops after it clears a non-zero or flagged tile.
     * @param curr the current tile we are clearing
     */
    private boolean clearSurroundingTiles(Tile curr) {
        if (hasMine(curr)) {
            return false;
        }
        if (!curr.isCleared() && !curr.isFlagged()) {
            curr.setCleared();
            incrementNumTilesCleared();
            if (curr.getNumNeighboringMines() == 0) {
                Set<Tile> neighbors = curr.getNeighbors();
                for (Tile tile : neighbors) {
                    clearSurroundingTiles(tile);
                }
            }
        }
        return true;
    }

    /**
     * Method to flag a tile
     * Utilized in tile clicking functionality.
     * @param row the row of the tile to flag
     * @param column the column of the tile to flag
     */
    public void flag(int row, int column) {
        if (playing) {
            Tile currentTile = tiles[row][column];
            if (!currentTile.isCleared()) {
                if (!currentTile.isFlagged()) {
                    if (flagsRemaining > 0) {
                        flagsRemaining--;
                        currentTile.setFlag(true);
                    }
                } else {
                    flagsRemaining++;
                    currentTile.setFlag(false);
                }
                numMoves++;
            }
        }
    }

    private boolean hasWon() {
        return !playing && won;
    }

    private boolean hasLost() {
        return !playing && !won;
    }

    /**
     * @return the status of the game (from Status enum)
     */
    public Status status() {
        if (hasWon()) {
            return Status.WIN;
        } else if (hasLost()) {
            return Status.LOSE;
        } else {
            return Status.PLAYING;
        }
    }

    /**
     * Check if a tile is a mine
     */
    public boolean hasMine(Tile tile) {
        return mineSet.contains(tile);
    }

    public int getNumTilesCleared() {
        return numTilesCleared;
    }

    public int getNumMines() {
        return numMines;
    }

    public int getNumRows() {
        return rows;
    }

    public int getNumColumns() {
        return columns;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void incrementNumTilesCleared() {
        numTilesCleared++;
    }

    public int getNumMoves() {
        return numMoves;
    }

    public void removeAllFlags() {
        for (Tile[] row : tiles) {
            for (Tile tile : row) {
                if (tile.isFlagged()) {
                    tile.setFlag(false);
                }
            }
        }
    }

    /**
     * Gets the current density
     */
    public double density() {
        return (double) numMines / (rows * columns - numTilesCleared - (numMines - flagsRemaining));
    }

    public double getInitialDensity() {
        return initialDensity;
    }

    public long getElapsedMillis() {
        return elapsedMillis;
    }

    private void updateElapsedTime() {
        elapsedMillis = System.currentTimeMillis() - startTime;
    }

    public GameSummary getSummary() {
        return new GameSummary(this);
    }
}
