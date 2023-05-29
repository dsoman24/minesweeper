package src.main.java.minesweeper.bot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import src.main.java.minesweeper.bot.strategy.BotStrategy;
import src.main.java.minesweeper.bot.strategy.StrategyTypeOnTile;
import src.main.java.minesweeper.logic.Difficulty;
import src.main.java.minesweeper.logic.GameSummary;
import src.main.java.minesweeper.logic.Minesweeper;
import src.main.java.minesweeper.logic.Tile;

/**
 * Class to generate data and test bot
 */
public class BotTester {

    /** Number of trials per density */
    private int numTrials;
    private int rows;
    private int columns;

    private BotStrategy<Tile> strategy;

    /** Data to write */
    private List<GameSummary> data;

    public BotTester(StrategyTypeOnTile strategyType, int numTrials, int rows, int columns) {
        this.strategy = strategyType.getStrategy();
        this.numTrials = numTrials;
        this.rows = rows;
        this.columns = columns;
        this.data = new ArrayList<>();
    }

    /**
     * Default to 9x9 board.
     * @param strateyType the stratey type on Tile objects to evaluate.
     * @param numTrials the number of trials to evaluate.
     */
    public BotTester(StrategyTypeOnTile strategyType, int numTrials) {
        this(strategyType, numTrials, 9, 9);
    }

    /**
     * Plays one game to completion with a given density.
     * @parma density the density of the game to play
     * @return a summary of the game
     */
    private GameSummary playToCompletion(double density) {
        Difficulty difficulty = Difficulty.customDifficulty(rows, columns, density);
        Minesweeper minesweeper = new Minesweeper(difficulty);
        Bot<Tile> bot = new Bot<>(minesweeper.getTilingState(), strategy);

        BotController controller = new BotController(minesweeper, bot);

        while (controller.hasNext()) {
            controller.next();
        }

        return minesweeper.getSummary();
    }

    /**
     * Plays one game to completion based on number of mines.
     * @parma numMines the number of mines to evaluate the density of.
     * @return the game summary
     */
    private GameSummary playToCompletion(int numMines) {
        double density = numMines > (rows * columns) ? 1.0 : (double) numMines / (rows * columns);
        return playToCompletion(density);
    }

    /**
     * Performs all trials for a given number of mines.
     * @param numMines the number of mines to play trials for.
     */
    private void collectDataForNumMines(int numMines) {
        ProgressBar.updateProgressBar(numMines, rows * columns);
        for (int i = 0; i < numTrials; i++) {
            data.add(playToCompletion(numMines));
        }
    }

    /**
     * @param low the low number of mines to play trials for
     * @param high the high number of mines to play trials for
     */
    private void collectDataInRangeOfMines(int low, int high) {
        for (int numMines = low; numMines <= high; numMines++) {
            collectDataForNumMines(numMines);
        }
        System.out.println(); // progress bar done
    }

    private void collectData() {
        collectDataInRangeOfMines(0, rows * columns);
    }

    /**
     * Updates the file following a read-modify-write pattern.
     * @param filename the filename to update with the given number of trials.
     */
    public void runGamesAndUpdateFile(String filename) {
        String path = "data/" + filename;
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
        } catch (FileNotFoundException fnfe) {}
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
