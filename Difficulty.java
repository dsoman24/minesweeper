/**
 * Difficulty Enum.
 * Defines num rows, num columns, num mines of a minesweeper game.
 * Also defines string format for menu selection
 */
public enum Difficulty {
    EASY(9, 9, 10),
    MEDIUM(16, 16, 40),
    HARD(16, 30, 99),
    EXPERT(24, 30, 180);


    private int rows;
    private int columns;
    private int numMines;

    private Difficulty(int rows, int columns, int numMines) {
        this.rows = rows;
        this.columns = columns;
        this.numMines = numMines;
    }

    public String toString() {
        return String.format("%s (%dx%d, %d mines)", name(), rows, columns, numMines);
    }

    public int getNumRows() {
        return rows;
    }

    public int getNumColumns() {
        return columns;
    }

    public int getNumMines() {
        return numMines;
    }
}
