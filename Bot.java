/**
 * Bot which will play one game of Minesweeper
 */
public class Bot {
    /** Minesweeper instance to play on */
    private Minesweeper minesweeper;
    /** The strategy this bot uses */
    private BotStrategy strategy;

    public Bot(Minesweeper minesweeper, BotStrategy strategy) {
        this.minesweeper = minesweeper;
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
        return strategy.move(minesweeper);
    }

    /**
     * Runs all moves until completion.
     */
    public void runGame() {
        while (minesweeper.status() == Status.PLAYING) {
            System.out.println(minesweeper.gameStateSummary());
            move();
        }
        System.out.println(minesweeper.gameStateSummary());
    }
}
