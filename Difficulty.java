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

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public int getNumMines() {
        return numMines;
    }
}
