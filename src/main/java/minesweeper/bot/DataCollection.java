package src.main.java.minesweeper.bot;

import java.util.Scanner;

import src.main.java.minesweeper.bot.strategy.StrategyTypeOnTile;

/**
 * Driver class for data collection.
 */
public class DataCollection {

    public static void main(String[] args) throws InterruptedException {
        Scanner scan = new Scanner(System.in);
        int numTrials;
        while (true) {
            try {
                System.out.print("Enter a number of trials: ");
                numTrials = scan.nextInt();
                if (numTrials < 0) {
                    throw new IllegalArgumentException();
                }
                scan.nextLine();
                break;
            } catch (Exception e) {
                System.out.println("Invalid input");
                scan.nextLine();
            }
        }
        int numRows;
        while (true) {
            try {
                System.out.print("Enter a number of rows: ");
                numRows = scan.nextInt();
                if (numRows < 0) {
                    throw new IllegalArgumentException();
                }
                scan.nextLine();
                break;
            } catch (Exception e) {
                System.out.println("Invalid input");
                scan.nextLine();
            }
        }
        int numCols;
        while (true) {
            try {
                System.out.print("Enter a number of columns: ");
                numCols = scan.nextInt();
                if (numCols < 0) {
                    throw new IllegalArgumentException();
                }
                scan.nextLine();
                break;
            } catch (Exception e) {
                System.out.println("Invalid input");
                scan.nextLine();
            }
        }
        String filename;
        while (true) {
            try {
                System.out.print("File name (excluding .csv): ");
                filename = scan.nextLine();
                break;
            } catch (Exception e) {
                System.out.println("Invalid input");
                scan.nextLine();
            }
        }
        scan.close();
        BotTester tester = new BotTester(StrategyTypeOnTile.PROBABILISTIC, numTrials);
        filename += ".csv";
        tester.runGamesAndUpdateFile(filename);
    }
}
