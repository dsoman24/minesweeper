package src.main.java.minesweeper.logic;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Non-generic Minesweeper game implementation.
 * Uses a TilingState of Tile objects.
 * Logic only, no UI elements.
 * @author Daniel Öman
 * @date 12/31/2022
 * @version 1.0
 */
public class Minesweeper {

    private TilingState<Tile> tilingState;
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
     * 1-arg constructor.
     * @param difficulty the difficulty of this game instance
     */
    public Minesweeper(Difficulty difficulty) {
        this(difficulty, new Random());
    }

    /**
     * 2-arg constructor.
     * @param difficulty the difficulty of this game instance
     * @param random the random number generator to use when generating mine positions.
     */
    public Minesweeper(Difficulty difficulty, Random random) {
        rows = difficulty.getNumberOfRows();
        columns = difficulty.getNumberOfColumns();
        initialDensity = difficulty.getDensity();
        numMines = difficulty.getNumberOfMines();
        mineSet = new HashSet<>();
        playing = true;
        numTilesCleared = 0;
        flagsRemaining = numMines;

        // initialize the tiling state
        tilingState = new TilingState<>(rows, columns, numMines);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                Tile tile = new Tile(i, j, difficulty.getDensity());
                tilingState.add(i, j, tile);
            }
        }
        this.random = random;
        this.elapsedMillis = 0;
    }

    @Override
    public String toString() {
        return tilingState.toString();
    }

    /**
     * Public helper method to check if a position is within bounds.
     * @param row the row of the position to check
     * @param column the column of the position to check
     * @return if the position is within the bounds of the minefield
     */
    public boolean isInBounds(int row, int column) {
        return tilingState.isInBounds(row, column);
    }

    /**
     * Generate the game state. Will guarantee a 0 on start.
     * @param startingRow the row of the first position cleared.
     * @param startingColumn the column of the first position cleared.
     */
    private void startGame(int startingRow, int startingColumn) {
        // Begin by randomly generating mines
        this.startTime = System.currentTimeMillis();
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
            mineSet.add(getTileAt(row, column));
            // now go around and increment the tiles immediately around it
            for (int x = row - 1; x < row + 2; x++) {
                for (int y = column - 1; y < column + 2; y++) {
                    if (!(x == row && y == column) && isInBounds(x, y)) {
                        getTileAt(x, y).incrementNumberOfNeighboringMines();
                    }
                }
            }
        }
    }

    /**
     * Returns the tile at a given positon.
     * @param row the row to get
     * @param column the column to get
     * @return the tile at (row, column)
     */
    public Tile getTileAt(int row, int column) {
        return tilingState.get(row, column);
    }

    /**
     * @return the number of flags that have not been placed
     */
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
            Tile currentTile = getTileAt(row, column);
            if (!currentTile.isCleared() && !currentTile.isFlagged()) {
                // Generate the tiles if no tiles have been cleared.
                if (numTilesCleared == 0) {
                    startGame(row, column);
                }
                playing = clearSurroundingTiles(currentTile);
                tilingState.updateDensity(density());
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
     * Clear a tile.
     * @param tile the tile to clear
     */
    public void clear(Tile tile) {
        clear(tile.getRow(), tile.getColumn());
    }

    /**
     * Perform a multi-clear on a cleared and non-zero numbered tile.
     * @param tile the tile to attempt to multi-clear
     */
    public void multiClear(Tile tile) {
        if (tile.isCleared() && tile.getNumberOfNeighboringMines() > 0 && tile.getNumberOfNeighboringMines() == tilingState.getNumberOfFlaggedNeighbors(tile)){
            Set<Tile> neighbors = tilingState.getNeighbors(tile);
            for (Tile neighbor : neighbors) {
                clear(neighbor);
            }
        }
    }

    /**
     * Recursive helper method to clear the tiles using DFS.
     * Clears all this tile and adjacent tiles if not visited, stops after it clears a non-zero or flagged tile.
     * @param curr the current tile we are clearing
     * @return false if the current tile is a mine, true otherwise.
     *         Will only return false if the first tile in the recursive callis a mine.
     */
    private boolean clearSurroundingTiles(Tile curr) {
        if (hasMine(curr)) {
            return false;
        }
        if (!curr.isCleared() && !curr.isFlagged()) {
            curr.setCleared();
            numTilesCleared++;
            if (curr.getNumberOfNeighboringMines() == 0) {
                Set<Tile> neighbors = tilingState.getNeighbors(curr);
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
            Tile currentTile = getTileAt(row, column);
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
                tilingState.updateDensity(density());
            }
        }
    }

    /**
     * Check if the game is over and has been won.
     * @return true if the game has been won, false otherwise.
     */
    private boolean hasWon() {
        return !playing && won;
    }

    /**
     * Check if the game is over and has been lost.
     * @return true if the game has been lost, false otherwise.
     */
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
     * Check if a tile is a mine (the tile is contained within the mineSet)
     * @return true if the tile is a mine, false otherwise
     */
    public boolean hasMine(Tile tile) {
        return mineSet.contains(tile);
    }

    /**
     * @return the number of cleared tiles in this game.
     */
    public int getNumberOfTilesCleared() {
        return numTilesCleared;
    }

    /**
     * @return the total number of mines in this game.
     */
    public int getNumMines() {
        return numMines;
    }

    /**
     * @return the total number of rows in this game.
     */
    public int getNumRows() {
        return rows;
    }

    /**
     * @return the total number of columns in this game.
     */
    public int getNumberOfColumns() {
        return columns;
    }

    /**
     * @return true if the game is in play, false otherwise.
     */
    public boolean isPlaying() {
        return playing;
    }

    /**
     * @return the number of moves that have been played up to now.
     */
    public int getNumberOfMoves() {
        return numMoves;
    }

    /**
     * Removes all the flags from the game and updates the tilingState's density property.
     */
    public boolean removeAllFlags() {
        boolean flagRemoved = false;
        for (Tile tile : tilingState) {
            if (tile.isFlagged()) {
                flagsRemaining++;
                tile.setFlag(false);
                flagRemoved = true;
            }
        }
        if (flagRemoved) {
            tilingState.updateDensity(density());
        }
        return flagRemoved;
    }

    /**
     * @return the current density.
     */
    public double density() {
        return (double) numMines / (rows * columns - numTilesCleared - (numMines - flagsRemaining));
    }

    /**
     * @return the initial density of the game.
     */
    public double getInitialDensity() {
        return initialDensity;
    }

    /**
     * @return the elapsed time in milliseconds since the start of the game.
     */
    public long getElapsedMillis() {
        return elapsedMillis;
    }

    /**
     * Updates the elapsed time through: elpasedMillis = current time - start time
     */
    private void updateElapsedTime() {
        elapsedMillis = System.currentTimeMillis() - startTime;
    }

    /**
     * Returns a representation of the current game state containing many features.
     * @return the GameSummary object that stores information about this game.
     */
    public GameSummary getSummary() {
        return new GameSummary(this);
    }

    /**
     * Returns a more "physical" representation of the current game state.
     * @return the current tiling state of the game.
     */
    public TilingState<Tile> getTilingState() {
        return tilingState;
    }
}
