package src.minesweeper;

public class GameSummary {

    private int numRows;
    private int numColumns;
    private double initialDensity;
    private int numMines;
    private Status status;
    private int numMoves;
    private int numTilesCleared;

    public GameSummary(Minesweeper minesweeper) {
        numRows = minesweeper.getNumRows();
        numColumns = minesweeper.getNumColumns();
        initialDensity = minesweeper.getInitialDensity();
        numMines = minesweeper.getNumMines();
        status = minesweeper.status();
        numMoves = minesweeper.getNumMoves();
        numTilesCleared = minesweeper.getNumTilesCleared();
    }

    @Override
    public String toString() {
        return String.format("%d, %d, %f, %d, %s, %d, %d",
            numRows, numColumns, initialDensity, numMines, status, numMoves, numTilesCleared
        );
    }
}