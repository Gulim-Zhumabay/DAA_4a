package dp;

public class EditDistance {

    public static int distance(String a, String b) {
        int n = a.length();
        int m = b.length();
        int[][] t = new int[n + 1][m + 1];

        for (int i = 0; i <= n; i++) {
            t[i][0] = i;
        }
        for (int j = 0; j <= m; j++) {
            t[0][j] = j;
        }

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    t[i][j] = t[i - 1][j - 1];
                } else {
                    int del = t[i - 1][j];
                    int ins = t[i][j - 1];
                    int rep = t[i - 1][j - 1];
                    t[i][j] = 1 + Math.min(rep, Math.min(del, ins));
                }
            }
        }
        return t[n][m];
    }
}