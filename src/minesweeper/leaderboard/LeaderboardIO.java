package src.minesweeper.leaderboard;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import src.minesweeper.Difficulty;

/**
 * Class to write entries to leaderboard
 */
public class LeaderboardIO {

    public static void write(LeaderboardEntry entry) {
        File leaderboard = new File("leaderboard.csv");
        PrintWriter writer;
        try {
            boolean newFile = !leaderboard.exists();
            writer = new PrintWriter(
                new FileOutputStream(leaderboard, true)
            );
            if (newFile) {
                writer.println("date,name,difficulty,time");
            }
            writer.println(entry);
            writer.close();
        } catch (FileNotFoundException fnfe) {
            System.out.println("File not found");
        }
    }

    public static List<LeaderboardEntry> read(String path, Difficulty difficulty) {
        File file = new File("leaderboard.csv");
        List<LeaderboardEntry> entries = new ArrayList<>();
        try {
            Scanner scan = new Scanner(file);
            scan.nextLine();
            while (scan.hasNextLine()) {
                LeaderboardEntry entry = LeaderboardEntry.parseLine(scan.nextLine());
                if (difficulty.equals(entry.getDifficulty())) {
                    entries.add(entry);
                }
            }
            scan.close();
        } catch (FileNotFoundException fnfe) {
            System.out.println("File not found");
        }
        return entries;
    }
}
