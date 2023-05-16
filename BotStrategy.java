/**
 * Strategy abstract class.
 */
public abstract class BotStrategy {

    protected Minesweeper minesweeper;

    public BotStrategy(Minesweeper minesweeper) {
        this.minesweeper = minesweeper;
    }

    /**
     * Run moves until completion
     */
    public Status runGame() {
        while (minesweeper.status() == Status.PLAYING) {
            move();
        }
        return minesweeper.status();
    }

    public Minesweeper getMinesweeper() {
        return minesweeper;
    }


    /**
     * Play an individual move.
     * @return Status the status of the game after the move
     */
    public abstract Status move();
}