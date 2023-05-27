package src.main.java.minesweeper.ui.leaderboard;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import src.main.java.minesweeper.logic.Difficulty;

public class LeaderboardEntry implements Comparable<LeaderboardEntry> {
    private String date;
    private String name;
    private double time;
    private Difficulty difficulty;

    /**
     * 6-arg constructor.
     * @param date the date of this entry
     * @param name the name of this entry
     * @param difficulty the difficulty of this entry
     * @param time the time of this entry
     */
    public LeaderboardEntry(String date, String name, Difficulty difficulty, double time) {
        this.date = date;
        this.name = "";
        for (int i = 0; i < name.length(); i++) { // replace all commas in name with spaces
            if (name.charAt(i) == ',') {
                this.name += " ";
            } else {
                this.name += name.charAt(i);
            }
        }
        this.time = time;
        this.difficulty = difficulty;
    }

    /**
     * 3-arg constructor
     */
    public LeaderboardEntry(String name, Difficulty difficulty, double time) {
        this(
            DateTimeFormatter.ofPattern("yyyy/MM/dd").format(LocalDateTime.now()),
            name, difficulty, time
        );
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%f", date, name, difficulty.name(), time);
    }

    @Override
    public int compareTo(LeaderboardEntry other) {
        return (int) ((this.time - other.time) * 1000);
    }

    public String getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public double getTime() {
        return time;
    }

    public Difficulty getDifficulty() {
        return this.difficulty;
    }

    /**
     * Method to parse a line from the leaderboard file into an entry object
     * @param line the line to parse
     * @return the LeaderBoardEntry object
     */
    public static LeaderboardEntry parseLine(String line) {
        String[] parts = line.split(",");

        Map<String, Difficulty> difficultyMap = new HashMap<>();
        for (Difficulty difficulty : Difficulty.values()) {
            difficultyMap.put(difficulty.name(), difficulty);
        }

        Difficulty difficulty = difficultyMap.get(parts[2]);

        return new LeaderboardEntry(
            parts[0],
            parts[1],
            difficulty,
            Double.parseDouble(parts[3])
        );
    }
}