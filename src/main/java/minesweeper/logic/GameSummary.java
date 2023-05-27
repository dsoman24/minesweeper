package src.main.java.minesweeper.logic;

/**
 * Stores information about a GameState, used for data collection.
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

    public GameSummary(Minesweeper minesweeper) {
        this(
            minesweeper.getNumRows(),
            minesweeper.getNumColumns(),
            minesweeper.getInitialDensity(),
            minesweeper.getNumMines(),
            minesweeper.status(),
            minesweeper.getNumMoves(),
            minesweeper.getNumTilesCleared(),
            minesweeper.getElapsedMillis()
        );
    }

    private GameSummary(int numRows, int numColumns, double initialDensity, int numMines, Status status, int numMoves, int numTilesCleared, long elapsedMillis) {
        this.numRows = numRows;
        this.numColumns = numColumns;
        this.initialDensity = initialDensity;
        this.numMines = numMines;
        this.status = status;
        this.numMoves = numMoves;
        this.numTilesCleared = numTilesCleared;
        this.elapsedMillis = elapsedMillis;
    }

    @Override
    public String toString() {
        return String.format("%d,%d,%f,%d,%s,%d,%d,%d",
            numRows, numColumns, initialDensity, numMines, status, numMoves, numTilesCleared, elapsedMillis
        );
    }

    public static GameSummary parseLine(String line) {
        String[] items = line.strip().split(",");
        int numRows = Integer.parseInt(items[0]);
        int numColumns = Integer.parseInt(items[1]);
        double initialDensity = Double.parseDouble(items[2]);
        int numMines = Integer.parseInt(items[3]);
        Status status;
        if (items[4].equals("WIN")) {
            status = Status.WIN;
        } else {
            status = Status.LOSE;
        }
        int numMoves = Integer.parseInt(items[5]);
        int numTilesCleared = Integer.parseInt(items[6]);
        long elapsedMillis = Long.parseLong(items[7]);

        return new GameSummary(
            numRows, numColumns, initialDensity, numMines, status, numMoves, numTilesCleared, elapsedMillis
        );
    }
}