import java.util.Random;

public class RandomStrategy implements BotStrategy {

    private Random rand;

    public RandomStrategy(Random rand) {
        this.rand = rand;
    }

    public RandomStrategy() {
        this.rand = new Random();
    }

    public Status move(Minesweeper minesweeper) {
        int row = rand.nextInt(minesweeper.getNumRows());
        int col = rand.nextInt(minesweeper.getNumColumns());
        minesweeper.clear(row, col);
        return minesweeper.status();
    }
}
