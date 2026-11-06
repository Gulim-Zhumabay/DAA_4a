import dp.EditDistance;
import dp.LCS;
import matching.KMP;
import matching.Naive;
import matching.RabinKarp;
import suffix.Kasai;
import suffix.SuffixArray;

import java.util.Arrays;
import java.util.List;

public class AllTests {

    static int passed = 0;
    static int failed = 0;

    static void check(String name, Object expected, Object actual) {
        if (expected.equals(actual)) {
            passed++;
            System.out.println("pass: " + name);
        } else {
            failed++;
            System.out.println("fail: " + name + " expected " + expected + " got " + actual);
        }
    }

    static void checkMatch(String name, String text, String pattern, List<Integer> expected) {
        List<Integer> a = Naive.search(text, pattern);
        List<Integer> b = KMP.search(text, pattern);
        List<Integer> c = RabinKarp.search(text, pattern);
        check(name + " naive", expected, a);
        check(name + " kmp", expected, b);
        check(name + " rabin-karp", expected, c);
        check(name + " all equal", true, a.equals(b) && b.equals(c));
    }

    static void testLcs() {
        check("lcs identical", 3, LCS.length("abc", "abc"));
        check("lcs different", 0, LCS.length("abc", "xyz"));
        check("lcs empty first", 0, LCS.length("", "abc"));
        check("lcs empty second", 0, LCS.length("abc", ""));
        check("lcs example", 3, LCS.length("ACD", "ABCD"));
    }

    static void testEditDistance() {
        check("edit identical", 0, EditDistance.distance("abc", "abc"));
        check("edit empty first", 3, EditDistance.distance("", "abc"));
        check("edit empty second", 3, EditDistance.distance("abc", ""));
        check("edit substitution", 1, EditDistance.distance("abc", "abd"));
        check("edit insertion", 1, EditDistance.distance("abc", "abcd"));
        check("edit deletion", 1, EditDistance.distance("abcd", "abc"));
        check("edit kitten sitting", 3, EditDistance.distance("kitten", "sitting"));
    }

    static void testMatching() {
        checkMatch("pattern at start", "abcdef", "abc", List.of(0));
        checkMatch("pattern in middle", "abcdef", "cd", List.of(2));
        checkMatch("pattern at end", "abcdef", "ef", List.of(4));
        checkMatch("multiple occurrences", "abababa", "aba", List.of(0, 2, 4));
        checkMatch("overlapping", "aaaaa", "aa", List.of(0, 1, 2, 3));
        checkMatch("pattern absent", "abcdef", "xyz", List.of());
        checkMatch("pattern longer than text", "abc", "abcd", List.of());
        checkMatch("empty pattern", "abc", "", List.of());
        check("lps table", Arrays.toString(new int[]{0, 0, 1, 2, 3, 0, 1}),
                Arrays.toString(KMP.buildLps("ababaca")));
    }

    static void testKasai() {
        int[] sa = SuffixArray.buildSimple("banana");
        check("banana suffix array", Arrays.toString(new int[]{5, 3, 1, 0, 4, 2}), Arrays.toString(sa));
        check("banana lcp", Arrays.toString(new int[]{0, 1, 3, 0, 0, 2}),
                Arrays.toString(Kasai.buildLcp("banana", sa)));

        int[] sa2 = SuffixArray.buildSimple("aaaa");
        check("aaaa suffix array", Arrays.toString(new int[]{3, 2, 1, 0}), Arrays.toString(sa2));
        check("aaaa lcp", Arrays.toString(new int[]{0, 1, 2, 3}),
                Arrays.toString(Kasai.buildLcp("aaaa", sa2)));
    }

    public static void main(String[] args) {
        testLcs();
        testEditDistance();
        testMatching();
        testKasai();
        System.out.println("passed: " + passed + ", failed: " + failed);
        if (failed > 0) {
            System.exit(1);
        }
    }
}