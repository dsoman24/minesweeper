import java.util.Random;

public class RandomStrategy extends BotStrategy {

    private Random rand;

    public RandomStrategy(Minesweeper minesweeper, Random rand) {
        super(minesweeper);
        this.rand = rand;
    }

    public RandomStrategy(Minesweeper minesweeper) {
        this(minesweeper, new Random());
    }

    @Override
    public Tile tileToClear() {
        int row = rand.nextInt(minesweeper.getNumRows());
        int column = rand.nextInt(minesweeper.getNumColumns());
        while (minesweeper.getTileAt(row, column).isCleared() || minesweeper.getTileAt(row, column).isFlagged()) {
            row = rand.nextInt(minesweeper.getNumRows());
            column = rand.nextInt(minesweeper.getNumColumns());
        }
        return minesweeper.getTileAt(row, column);
    }
}
