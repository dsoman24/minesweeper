package src.main.java.minesweeper.bot;

import java.util.Scanner;

import src.main.java.minesweeper.bot.strategy.StrategyTypeOnTile;

/**
 * Driver class for data collection.
 */
public class DataCollection {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int numTrials;
        while (true) {
            try {
                System.out.print("Enter a number of trials: ");
                numTrials = scan.nextInt();
                scan.nextLine();
                scan.close();
                break;
            } catch (Exception e) {
                System.out.println("Please enter a number!");
                scan.nextLine();
            }
        }

        BotTester tester = new BotTester(StrategyTypeOnTile.PROBABILISTIC, numTrials);
        String filename = "test.csv";
        tester.updateFile(filename);
    }
}
