package src.main.java.minesweeper.logic;
/**
 * Difficulty Enum.
 * Defines num rows, num columns, num mines of a minesweeper game.
 * Also defines string format for menu selection
 */
public enum Difficulty {
    EASY(9, 9, (10.0)/(9*9)),
    MEDIUM(16, 16, (40.0)/(16*16)),
    HARD(16, 30, (99.0)/(16*30)),
    EXPERT(24, 30, (180.0)/(24*30)),
    CUSTOM(1, 1, 10);

    private int rows;
    private int columns;
    private int numMines;
    private double density;

    private Difficulty(int rows, int columns, double density) {
        this.rows = rows;
        this.columns = columns;
        this.density = density;
        updateNumMines();
    }

    private Difficulty() {}

    /**
     * Returns a custom difficulty object.
     * @param rows the number of rows
     * @param columns the number of columns
     * @param density the density of this difficulty
     */
    public static Difficulty customDifficulty(int rows, int columns, double density) {
        Difficulty difficulty = CUSTOM;
        difficulty.rows = rows;
        difficulty.columns = columns;
        difficulty.density = density;
        difficulty.updateNumMines();
        return difficulty;
    }

    /**
     * @return which difficulty is characterized by this input
     */
    public static Difficulty matchDifficulty(int rows, int columns, double density) {
        Difficulty difficulty = customDifficulty(rows, columns, density);
        if (difficulty.equalDifficulties(EASY)) {
            return EASY;
        } else if (difficulty.equalDifficulties(MEDIUM)) {
            return MEDIUM;
        } else if (difficulty.equalDifficulties(HARD)) {
            return HARD;
        } else if (difficulty.equalDifficulties(EXPERT)) {
            return EXPERT;
        } else {
            return difficulty;
        }
    }

    public boolean equalDifficulties(Difficulty other) {
        return numMines == other.numMines && rows == other.rows && columns == other.columns;
    }

    private void updateNumMines() {
        this.numMines = (int) (density * rows * columns);
    }

    @Override
    public String toString() {
        return String.format("%s (%dx%d, %d mines)", name(), rows, columns, numMines);
    }

    /**
     * @return the number of rows corresponding to this difficulty.
     */
    public int getNumberOfRows() {
        return rows;
    }

    /**
     * @return the number of columns corresponding to this difficulty.
     */
    public int getNumberOfColumns() {
        return columns;
    }

    /**
     * @return the number of mines corresponding to this difficulty.
     */
    public int getNumberOfMines() {
        return numMines;
    }

    /**
     * @return the density corresponding to this difficulty.
     */
    public double getDensity() {
        return density;
    }

    public boolean equalDifficulty(Difficulty other) {
        return rows == other.rows && columns == other.columns && numMines == other.numMines;
    }
}
