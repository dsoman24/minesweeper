package src.main.java.minesweeper.logic;

/**
 * Stores information about a GameState, used for data collection.
 * A game summary holds information about:
 * <ul>
 * <li> the number of rows
 * <li> the number of columns
 * <li> the initial density
 * <li> the number of mines
 * <li> the current status
 * <li> the number of moves
 * <li> the number of tiles cleared
 * <li> the elapsed time
 * </ul>
 */
public class GameSummary {

    private int numRows;
    private int numColumns;
    private double initialDensity;
    private int numMines;
    private Status status;
    private int numMoves;
    private int numTilesCleared;
    private long elapsedMillis;
    private Difficulty difficulty;

    /**
     * 1-arg constructor
     * @param minesweeper the minesweeper game to get information about
     */
    public GameSummary(Minesweeper minesweeper) {
        this(
            minesweeper.getNumRows(),
            minesweeper.getNumberOfColumns(),
            minesweeper.getInitialDensity(),
            minesweeper.getNumMines(),
            minesweeper.status(),
            minesweeper.getNumberOfMoves(),
            minesweeper.getNumberOfTilesCleared(),
            minesweeper.getElapsedMillis()
        );
    }

    /**
     * Private constructor to populate fields.
     */
    private GameSummary(int numRows, int numColumns, double initialDensity, int numMines, Status status, int numMoves, int numTilesCleared, long elapsedMillis) {
        this.numRows = numRows;
        this.numColumns = numColumns;
        this.initialDensity = initialDensity;
        this.numMines = numMines;
        this.status = status;
        this.numMoves = numMoves;
        this.numTilesCleared = numTilesCleared;
        this.elapsedMillis = elapsedMillis;
        this.difficulty = Difficulty.matchDifficulty(numRows, numColumns, initialDensity);
    }

    @Override
    public String toString() {
        return String.format("%s,%d,%d,%f,%d,%s,%d,%d,%d",
            difficulty.name(), numRows, numColumns, initialDensity, numMines, status, numMoves, numTilesCleared, elapsedMillis
        );
    }

    /**
     * @param line a comma separated string to parse.
     * @return the game summary object that corresponds to the line.
     */
    public static GameSummary parseLine(String line) {
        String[] items = line.strip().split(",");
        int numRows = Integer.parseInt(items[1]);
        int numColumns = Integer.parseInt(items[2]);
        double initialDensity = Double.parseDouble(items[3]);
        int numMines = Integer.parseInt(items[4]);
        Status status;
        if (items[5].equals("WIN")) {
            status = Status.WIN;
        } else {
            status = Status.LOSE;
        }
        int numMoves = Integer.parseInt(items[6]);
        int numTilesCleared = Integer.parseInt(items[7]);
        long elapsedMillis = Long.parseLong(items[8]);

        return new GameSummary(
            numRows, numColumns, initialDensity, numMines, status, numMoves, numTilesCleared, elapsedMillis
        );
    }

    public Status getStatus() {
        return status;
    }

    public long getElapsedMillis() {
        return elapsedMillis;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }
}