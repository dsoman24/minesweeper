package src.main.java.minesweeper.bot.strategy.probabilistic;
public enum SimplifyResult {
    SIMPLIFIED,
    NEGATIVE,
    TOO_BIG,
    NO_EFFECT;

    /**
     * @retrn true if the result of the simplification is a failure (too big or negative).
     */
    public boolean isFailure() {
        return this == TOO_BIG || this == NEGATIVE;
    }
}
