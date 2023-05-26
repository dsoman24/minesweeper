package src.main.java.bot;

import src.main.java.bot.strategy.probabilistic.ProbabilisticStrategy;

/**
 * Driver class for data collection
 */
public class DataCollection {

    public static void main(String[] args) {
        BotTester tester = new BotTester(new ProbabilisticStrategy(), 1000);
        String filename = "test.csv";
        tester.updateFile(filename);
    }
}
