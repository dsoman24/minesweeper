package src.main.java.bot.strategy.probabilistic;
public enum SimplifyResult {
    SIMPLIFIED,
    NEGATIVE,
    TOO_BIG,
    NO_EFFECT;

    public boolean isFailure() {
        return this == TOO_BIG || this == NEGATIVE;
    }
}
