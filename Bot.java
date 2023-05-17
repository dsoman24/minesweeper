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
     * @return the row and column number of the tile to clear
     */
    public Tile tileToClear() {
        return strategy.tileToClear();
    }

    /**
     * Run a game until completion
     */
    public Status runGame() {
        return strategy.runGame();
    }
}
