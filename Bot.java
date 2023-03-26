/**
 * Bot which will play Minesweeper
 */

import java.util.Random;

 public class Bot {
    private Minesweeper minesweeper;
    private int rows;
    private int columns;
    private int numMines;
    private Random random;
    private int turns;
    // clear tile on GUI with MouseButton.PRIMARY on GUI

    public Bot(Difficulty difficulty) {
        this.random = new Random();
        this.minesweeper = new Minesweeper(difficulty);
        this.rows = minesweeper.getNumRows();
        this.columns = minesweeper.getNumColumns();
        this.numMines = minesweeper.getNumMines();
    }

    public Bot() {
        this(Difficulty.EASY);
    }


    public void run() {
        while (minesweeper.status() == Status.PLAYING) {
            int row = random.nextInt(rows);
            int column = random.nextInt(columns);
            minesweeper.clear(row, column);
            turns++;
        }
        // System.out.println(minesweeper.status());
        // System.out.println(turns);
    }

    public static void main(String[] args) {
        int sum = 0;
        int trials = 10;
        for (int i = 0; i < trials; i++) {
            Bot bot = new Bot();
            bot.run();
            sum += bot.turns;
        }
        System.out.println(String.format("Average number of turns until game end: %f", (double) sum / trials));
    }


}
