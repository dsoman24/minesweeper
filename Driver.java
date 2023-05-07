/**
 * Driver class to test bots.
 */
public class Driver {
    public static void main(String[] args) {
        Minesweeper minesweeper = new Minesweeper(Difficulty.EASY);
        BotStrategy strategy = new RandomStrategy();
        Bot bot = new Bot(minesweeper, strategy);
        bot.runGame();
    }
}
