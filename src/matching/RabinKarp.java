package matching;

import java.util.ArrayList;
import java.util.List;

public class RabinKarp {

    public static long comparisons;
    public static long collisions;

    private static final long base = 256;
    private static final long mod = 1_000_000_007L;

    public static List<Integer> search(String text, String pattern) {
        comparisons = 0;
        collisions = 0;
        List<Integer> result = new ArrayList<>();
        int n = text.length();
        int m = pattern.length();

        if (m == 0 || m > n) {
            return result;
        }

        long pow = 1;
        for (int k = 0; k < m - 1; k++) {
            pow = (pow * base) % mod;
        }

        long ph = 0;
        long th = 0;
        for (int k = 0; k < m; k++) {
            ph = (ph * base + pattern.charAt(k)) % mod;
            th = (th * base + text.charAt(k)) % mod;
        }

        for (int i = 0; i <= n - m; i++) {
            if (ph == th) {
                int j = 0;
                while (j < m) {
                    comparisons++;
                    if (text.charAt(i + j) != pattern.charAt(j)) {
                        break;
                    }
                    j++;
                }
                if (j == m) {
                    result.add(i);
                } else {
                    collisions++;
                }
            }
            if (i < n - m) {
                th = (th - text.charAt(i) * pow % mod + mod) % mod;
                th = (th * base + text.charAt(i + m)) % mod;
            }
        }
        return result;
    }
}