package bench;

import dp.EditDistance;
import dp.LCS;
import matching.KMP;
import matching.Naive;
import matching.RabinKarp;
import suffix.Kasai;
import suffix.SuffixArray;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class Benchmark {

    static final String alphabet = "abcd";
    static final long seed = 42;
    static final int runs = 5;
    static final int warmups = 20;
    static final int patternLength = 20;
    static long sink = 0;

    interface Task {
        void run();
    }

    static String randomString(Random rnd, int n) {
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            sb.append(alphabet.charAt(rnd.nextInt(alphabet.length())));
        }
        return sb.toString();
    }

    static double averageMs(Task task) {
        for (int w = 0; w < warmups; w++) {
            task.run();
        }
        long total = 0;
        for (int r = 0; r < runs; r++) {
            long start = System.nanoTime();
            task.run();
            total += System.nanoTime() - start;
        }
        return total / (double) runs / 1_000_000.0;
    }

    static String fmt(double x) {
        return String.format(Locale.US, "%.4f", x);
    }

    static void runDp(List<String> rows) {
        int[] sizes = {100, 500, 1000};
        Random rnd = new Random(seed);
        for (int n : sizes) {
            String a = randomString(rnd, n);
            String b = randomString(rnd, n);
            double lcs = averageMs(() -> sink += LCS.length(a, b));
            double ed = averageMs(() -> sink += EditDistance.distance(a, b));
            rows.add("LCS," + n + "," + fmt(lcs));
            rows.add("EditDistance," + n + "," + fmt(ed));
        }
    }

    static void measure(List<String> rows, String kind, String text, String pattern) {
        List<Integer> expected = Naive.search(text, pattern);
        if (!expected.equals(KMP.search(text, pattern))
                || !expected.equals(RabinKarp.search(text, pattern))) {
            throw new IllegalStateException("algorithms disagree");
        }
        int matches = expected.size();
        int n = text.length();

        double nv = averageMs(() -> sink += Naive.search(text, pattern).size());
        long nvc = Naive.comparisons;
        double kmp = averageMs(() -> sink += KMP.search(text, pattern).size());
        long kmpc = KMP.comparisons;
        double rk = averageMs(() -> sink += RabinKarp.search(text, pattern).size());
        long rkc = RabinKarp.comparisons;
        long rkcol = RabinKarp.collisions;

        rows.add("Naive," + kind + "," + n + "," + fmt(nv) + "," + nvc + ",0," + matches);
        rows.add("KMP," + kind + "," + n + "," + fmt(kmp) + "," + kmpc + ",0," + matches);
        rows.add("RabinKarp," + kind + "," + n + "," + fmt(rk) + "," + rkc + "," + rkcol + "," + matches);
    }

    static void runMatching(List<String> rows) {
        int[] sizes = {10000, 50000, 100000};
        Random rnd = new Random(seed);
        for (int n : sizes) {
            String text = randomString(rnd, n);
            String present = text.substring(n / 2, n / 2 + patternLength);
            String absent = randomString(rnd, patternLength);
            while (!Naive.search(text, absent).isEmpty()) {
                absent = randomString(rnd, patternLength);
            }
            measure(rows, "present", text, present);
            measure(rows, "absent", text, absent);
        }
    }

    static void runKasai(List<String> rows) {
        int[] sizes = {1000, 5000, 10000};
        Random rnd = new Random(seed);
        for (int n : sizes) {
            String s = randomString(rnd, n);
            int[] sa = SuffixArray.buildSimple(s);
            double ms = averageMs(() -> sink += Kasai.buildLcp(s, sa)[n - 1]);
            rows.add("Kasai," + n + "," + fmt(ms) + "," + Kasai.comparisons);
        }
    }

    static void save(String file, String header, List<String> rows) throws IOException {
        try (FileWriter w = new FileWriter(file)) {
            w.write(header + "\n");
            for (String row : rows) {
                w.write(row + "\n");
            }
        }
        System.out.println(file);
        System.out.println(header);
        for (String row : rows) {
            System.out.println(row);
        }
        System.out.println();
    }

    public static void runAll() throws IOException {
        List<String> dpRows = new ArrayList<>();
        List<String> matchRows = new ArrayList<>();
        List<String> kasaiRows = new ArrayList<>();

        runDp(dpRows);
        runMatching(matchRows);
        runKasai(kasaiRows);

        save("results/dp.csv", "algorithm,n,avg_ms", dpRows);
        save("results/matching.csv", "algorithm,pattern,n,avg_ms,comparisons,collisions,matches", matchRows);
        save("results/kasai.csv", "algorithm,n,avg_ms,comparisons", kasaiRows);
    }
}