package src.main.java.bot.strategy.probabilistic;
import java.math.BigInteger;

/**
 * Source: https://stackoverflow.com/questions/25356014/calculate-cn-k-combinations-for-big-numbers-using-modinverse
 */
public class Combinatorics {
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
