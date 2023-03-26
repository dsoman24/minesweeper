import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class LeaderboardWriter {

    public void write(LeaderboardEntry entry) {
        File leaderboard = new File("leaderboard.csv");
        PrintWriter writer;
        try {
            boolean newFile = !leaderboard.exists();
            writer = new PrintWriter(
                new FileOutputStream(leaderboard, true)
            );
            if (newFile) {
                writer.println("date,name,rows,columns,numMines,time");
            }
            writer.println(entry);
            writer.close();
        } catch (FileNotFoundException fnfe) {
            System.out.println("File not found");
        }
    }
}
