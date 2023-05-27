package src.main.java.minesweeper.bot.strategy.probabilistic;
import java.math.BigInteger;

/**
 * Class to calculate the number of combinations.
 * Source: https://stackoverflow.com/questions/25356014/calculate-cn-k-combinations-for-big-numbers-using-modinverse
 */
public class Combinatorics {

    /**
     * Cannot be instantiated.
     */
    private Combinatorics() {}

    /**
     * Computes the number of combinations of n choose k.
     * @param n the size of the set
     * @param k the size of the subsets
     * @return number of subsets of size k from a set of size n.
     */
    public static BigInteger combinations(int n, int k) {
        BigInteger N = BigInteger.valueOf(n);
        BigInteger K = BigInteger.valueOf(k);
        // (n C k) and (n C (n-k)) are the same, so pick the smaller as k:
        if (K.compareTo(N.subtract(K)) > 0) {
            K = N.subtract(K);
        }
        BigInteger result = BigInteger.ONE;
        for (BigInteger i = BigInteger.ONE; i.compareTo(K) <= 0; i = i.add(BigInteger.ONE)) {
            result = result.multiply(N.subtract(K).add(i));
            result = result.divide(i);
        }
        return result;
    }
}
