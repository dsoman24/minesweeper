import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LeaderboardEntry implements Comparable<LeaderboardEntry> {
    private String date;
    private String name;
    private int rows;
    private int columns;
    private int numMines;
    private double time;
    private String difficulty;

    /**
     * 6-arg constructor.
     * @param date the date of this entry
     * @param name the name of this entry
     * @param rows the number of rows in this entry
     * @param columns the number of columns of this entry
     * @param numMines the number of mines of this entry
     * @param time the time of this entry
     */
    public LeaderboardEntry(String date, String name, int rows, int columns, int numMines, double time) {
        this.date = date;
        this.name = name;
        this.rows = rows;
        this.columns = columns;
        this.numMines = numMines;
        this.time = time;
        if (rows == 9 && columns == 9 && numMines == 10) {
            difficulty = "Easy (9x9, 10 mines)";
        } else if (rows == 16 && columns == 16 && numMines == 40) {
            difficulty = "Medium (16x16, 40 mines)";
        } else if (rows == 16 && columns == 30 && numMines == 99) {
            difficulty = "Hard (16x30, 99 mines)";
        } else if (rows == 30 && columns == 30 && numMines == 180) {
            difficulty = "Expert (24x30, 180 mines)";
        } else {
            difficulty = String.format("Custom (%dx%d, %d mines", rows, columns, numMines);
        }
    }

    /**
     * 5-arg constructor.
     * Fills in date with current date
     * @param name the name of this entry
     * @param rows the rows of this entry
     * @param columns the columns of this entry
     * @param numMines the number of mines of this entry
     * @param time the time of this entry
     */
    public LeaderboardEntry(String name, int rows, int columns, int numMines, double time) {
        this(
            DateTimeFormatter.ofPattern("yyyy/MM/dd").format(LocalDateTime.now()),
            name, rows, columns, numMines, time
        );
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%d,%d,%d,%f", date, name, rows, columns, numMines, time);
    }

    @Override
    public int compareTo(LeaderboardEntry other) {
        return (int) ((this.time - other.time) * 1000);
    }


    /**
     * name getter method.
     * @return name
     */
    public String getName() {
        return this.name;
    }

    /**
     * rows getter method.
     * @return rows
     */
    public int getRows() {
        return this.rows;
    }

    /**
     * columns getter method.
     * @return columns
     */
    public int getColumns() {
        return this.columns;
    }

    /**
     * numMines getter method.
     * @return numMines
     */
    public int getNumMines() {
        return this.numMines;
    }

    /**
     * time getter method.
     * @return time
     */
    public double getTime() {
        return this.time;
    }

    /**
     * date getter method.
     * @return date
     */
    public String getDate() {
        return this.date;
    }

    /**
     * difficulty getter method.
     * @return difficulty
     */
    public String getDifficulty() {
        return this.difficulty;
    }

    /**
     * Method to parse a line from the leaderboard file into an entry object
     * @param line the line to parse
     * @return the LeaderBoardEntry object
     */
    public static LeaderboardEntry parseLine(String line) {
        String[] parts = line.split(",");
        return new LeaderboardEntry(
            parts[0],
            parts[1],
            Integer.parseInt(parts[2]),
            Integer.parseInt(parts[3]),
            Integer.parseInt(parts[4]),
            Double.parseDouble(parts[5])
        );
    }
}
