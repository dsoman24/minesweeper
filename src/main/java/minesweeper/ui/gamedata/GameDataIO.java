package src.main.java.minesweeper.ui.gamedata;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import src.main.java.minesweeper.logic.Difficulty;
import src.main.java.minesweeper.logic.GameSummary;

public class GameDataIO {

    private GameDataIO() {};

    private static final String MAIN_PATH = "src/main/java/minesweeper/ui/gamedata/";

    public static List<GameSummary> read(String fileName, Difficulty difficulty) {
        File file = new File(MAIN_PATH + fileName);
        List<GameSummary> entries = new ArrayList<>();
        try {
            Scanner scan = new Scanner(file);
            scan.nextLine(); // skip first line
            while (scan.hasNextLine()) {
                GameSummary entry = GameSummary.parseLine(scan.nextLine());
                if (entry.getDifficulty().equalDifficulty(difficulty)) {
                    entries.add(entry);
                }
            }
            scan.close();
        } catch (FileNotFoundException fnfe) {
            System.out.println("File not found");
        }
        return entries;
    }

    public static void write(String fileName, GameSummary gameSummary) {
        File gameData = new File(MAIN_PATH + fileName);
        PrintWriter writer;
        try {
            boolean newFile = !gameData.exists();
            writer = new PrintWriter(
                new FileOutputStream(gameData, true)
            );
            if (newFile) {
                writer.println("difficulty,numRows,numColumns,initialDensity,numMines,status,numMoves,numTilesCleared,elapsedMillis");
            }
            writer.println(gameSummary);
            writer.close();
        } catch (FileNotFoundException fnfe) {
            System.out.println("File not found");
        }
    }
}
