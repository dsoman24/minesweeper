package src.bot;

/**
 * Class for data collection
 */
public class DataCollection {

    public static void main(String[] args) {
        BotTester tester = new BotTester(100);
        String filename = "test.csv";
        tester.updateFile(filename);
    }
}
