import java.util.Random;
/**
 * Minesweeper game implementation.
 * @author Daniel Öman
 * @date 12/31/2022
 * @version 1.0
 */
public class Minesweeper {

    private Tile[][] tiles;
    private int rows;
    private int columns;
    private int numMines;

    private int startingRow;
    private int startingColumn;

    private int flagsRemaining;

    // if the game is currently in play
    private boolean playing;
    private boolean won;
    private int numTilesCleared;

    /**
     * 3-args constructor
     * @param rows the number of rows
     * @param columns the number of columns
     * @param numMines the number of mines
     */
    public Minesweeper(Difficulty difficulty) {
        rows = difficulty.getRows();
        columns = difficulty.getColumns();
        numMines = difficulty.getNumMines();
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
     * Generate the game state on the first click. Will guarantee a 0 on start.
     */
    private void startGame() {
        // Begin by randomly generating mines
        Random rand = new Random();
        for (int i = 0; i < numMines; i++) {
            int row = rand.nextInt(rows);
            int column = rand.nextInt(columns);

            while (tiles[row][column].hasMine()
                || (row >= startingRow - 1
                && row <= startingRow + 1
                && column >= startingColumn - 1
                && column <= startingColumn + 1)) {
                row = rand.nextInt(rows);
                column = rand.nextInt(columns);
            }
            tiles[row][column].addMine();
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

    public Tile[][] getTiles() {
        return tiles;
    }


    public int getFlagsRemaining() {
        return flagsRemaining;
    }

    /**
     * Method to clear a single tile from the board.
     * Utilized in tile clicking functionality.
     * TO-DO use in bot functionality
     * @param row the row of the tile to clear
     * @param column the column of the tile to clear
     */
    public void clear(int row, int column) {
        if (playing) {
            Tile currentTile = tiles[row][column];
            if (!currentTile.isCleared() && !currentTile.isFlagged()) {
                if (numTilesCleared == 0) {
                    startingRow = row;
                    startingColumn = column;
                    startGame();
                }
                playing = currentTile.clearSurroundingTiles();
                if (numTilesCleared == rows * columns - numMines) {
                    won = true;
                    playing = false;
                }
            }
        }
    }

    /**
     * Method to flag a tile
     * Utilized in tile clicking functionality.
     * TO-DO use in bot functionality
     * @param row the row of the tile to flag
     * @param column the column of the tile to flag
     */
    public void flag(int row, int column) {
        if (playing) {
            Tile currentTile = tiles[row][column];
            if (!currentTile.isCleared()) {
                if (!currentTile.isFlagged()) {
                    flagsRemaining--;
                    currentTile.setFlag(true);
                } else {
                    flagsRemaining++;
                    currentTile.setFlag(false);
                }
            }
        }
    }

    private boolean hasWon() {
        return !playing && won;
    }

    private boolean hasLost() {
        return !playing && !won;
    }

    public Status status() {
        if (hasWon()) {
            return Status.WIN;
        } else if (hasLost()) {
            return Status.LOSE;
        } else {
            return Status.PLAYING;
        }
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
}
