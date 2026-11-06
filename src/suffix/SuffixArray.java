package suffix;

import java.util.Arrays;

public class SuffixArray {

    public static int[] buildSimple(String s) {
        int n = s.length();
        Integer[] idx = new Integer[n];
        for (int i = 0; i < n; i++) {
            idx[i] = i;
        }

        Arrays.sort(idx, (a, b) -> {
            int i = a;
            int j = b;
            while (i < n && j < n) {
                if (s.charAt(i) != s.charAt(j)) {
                    return s.charAt(i) - s.charAt(j);
                }
                i++;
                j++;
            }
            return (n - a) - (n - b);
        });

        int[] sa = new int[n];
        for (int i = 0; i < n; i++) {
            sa[i] = idx[i];
        }
        return sa;
    }
}