/**
 * Bot which will play one game of Minesweeper
 */
public class Bot {
    /** The strategy this bot uses */
    private BotStrategy strategy;

    public Bot(BotStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(BotStrategy strategy) {
        this.strategy = strategy;
    }

    public BotStrategy getStrategy() {
        return strategy;
    }

    /**
     * Perform one individual move based on the current minesweeper state.
     */
    public Status move() {
        return strategy.move();
    }

    /**
     * Run a game until completion
     */
    public void runGame() {
        strategy.runGame();
    }
}
