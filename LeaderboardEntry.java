import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LeaderboardEntry {
    private String name;
    private int rows;
    private int columns;
    private int numMines;
    private double time;
    private String date;


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
}
