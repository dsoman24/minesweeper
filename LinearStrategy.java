public class LinearStrategy extends BotStrategy {

    private int row = 0;
    private int column = 0;


    public LinearStrategy(Minesweeper minesweeper) {
        super(minesweeper);
    }

    @Override
    public Status move() {
        while (minesweeper.getTileAt(row, column).isCleared() || minesweeper.getTileAt(row, column).isFlagged()) {
            updatePosition();
        }
        minesweeper.clear(row, column);
        updatePosition();
        return minesweeper.status();
    }

    private void updatePosition() {
        row++;
        if (row >= minesweeper.getNumRows()) {
            row = 0;
            column++;
        }
    }
}
