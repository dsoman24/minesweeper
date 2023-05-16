import java.math.BigInteger;

/**
 * Source: https://stackoverflow.com/questions/25356014/calculate-cn-k-combinations-for-big-numbers-using-modinverse
 */
public class Combinatorics {

    // public static long combinations(int n, int r) {
    //     System.out.println(factorial(n - r));
    //     return factorial(n)/((factorial(r)) * (factorial(n - r)));
    // }

    // public static long factorial(int k) {
    //     if (k <= 1) {
    //         return 1;
    //     }
    //     return k * factorial(k - 1);
    // }

    public static int[] factorials = new int[100001];
    public static int mod = 1000000007;
    public static BigInteger MOD = BigInteger.valueOf(1000000007);

    public static void calculateFactorials() {

        long f = 1;

        for (int i = 1; i < factorials.length; i++) {
            f = (f * i) % mod;
            factorials[i] = (int) f;
        }

    }

    // Choose(n, k) = n! / (k! * (n-k)!)
    public static long combinations(int n, int k) {
        if (n < k) {
            return 0;
        }

        long a = BigInteger.valueOf(factorials[k]).modInverse(MOD).longValue();
        long b = BigInteger.valueOf(factorials[n - k]).modInverse(MOD).longValue();

        // Left to right associativity between * and %
        return factorials[n] * a % mod * b % mod;
    }
}
