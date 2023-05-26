package src.main.java.bot;

import src.main.java.bot.strategy.BotStrategy;
import src.main.java.minesweeper.Difficulty;
import src.main.java.minesweeper.GameSummary;
import src.main.java.minesweeper.Minesweeper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Class to generate data and test bot
 */
public class BotTester {

    /** Number of trials per density */
    private int numTrials;
    private int rows;
    private int columns;

    private BotStrategy strategy;

    /** Data to write */
    private List<GameSummary> data;

    public BotTester(BotStrategy strategy, int numTrials, int rows, int columns) {
        this.strategy = strategy;
        this.numTrials = numTrials;
        this.rows = rows;
        this.columns = columns;
        this.data = new ArrayList<>();
    }

    /**
     * Default to 9x9 board.
     */
    public BotTester(BotStrategy strategy, int numTrials) {
        this(strategy, numTrials, 9, 9);
    }

    /**
     * Plays one game to completion with a given density.
     * @return a summary of the game
     */
    private GameSummary playToCompletion(double density) {
        Difficulty difficulty = Difficulty.customDifficulty(rows, columns, density);
        Minesweeper minesweeper = new Minesweeper(difficulty);
        Bot bot = new Bot(minesweeper, strategy);
        bot.runGame();
        return minesweeper.getSummary();
    }

    private GameSummary playToCompletion(int numMines) {
        double density = numMines > (rows * columns) ? 1.0 : (double) numMines / (rows * columns);
        return playToCompletion(density);
    }

    private void collectDataForNumMines(int numMines) {
        System.out.println("Collecting data for " + numMines);
        for (int i = 0; i < numTrials; i++) {
            data.add(playToCompletion(numMines));
        }
    }

    private void collectDataInRangeOfMines(int low, int high) {
        for (int numMines = low; numMines <= high; numMines++) {
            collectDataForNumMines(numMines);
        }
    }

    private void collectData() {
        collectDataInRangeOfMines(0, rows * columns);
    }

    public void updateFile(String filname) {
        String path = "data/" + filname;
        File file = new File(path);
        read(file);
        collectData();
        write(file);
    }

    /**
     * Updates the data List field of this class with the data in the given file.
     */
    private void read(File file) {
        try {
            Scanner scan = new Scanner(file);
            scan.nextLine(); // eat the header line;
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                GameSummary summary = GameSummary.parseLine(line);
                data.add(summary);
            }
            scan.close();
        } catch (FileNotFoundException fnfe) {
            System.out.println("Failed to read: File not found.");
        }
    }

    /**
     * Write the contents of data List field to the given file.
     */
    private void write(File file) {
        try {
            boolean newFile = !file.exists();
            PrintWriter writer = new PrintWriter(
                new FileOutputStream(file, true)
            );
            if (newFile) {
                writer.println(
                    "numRows,numColumns,initialDensity,numMines,status,numMoves,numTilesCleared,elapsedMillis"
                );
            }
            for (GameSummary summary : data) {
                writer.println(summary);
            }
            writer.close();
        } catch (FileNotFoundException fnfe) {
            System.out.println("Failed to write: File not found.");
        }
    }

}
