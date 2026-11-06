import dp.EditDistance;
import dp.LCS;
import matching.KMP;
import matching.RabinKarp;
import suffix.SuffixArray;
import suffix.Kasai;

public class Main {
    public static void main(String[] args) {
        System.out.println(LCS.length("ACD", "ABCD"));
        System.out.println(LCS.length("", "abc"));
        System.out.println(EditDistance.distance("kitten", "sitting"));
        System.out.println(EditDistance.distance("", "abc"));
        System.out.println(EditDistance.distance("abc", "abc"));
        System.out.println(KMP.search("abababa", "aba"));
        System.out.println(KMP.search("abababa", "xyz"));
        System.out.println(KMP.search("abc", "abcd"));
        System.out.println(KMP.search("abc", ""));
        System.out.println(java.util.Arrays.toString(KMP.buildLps("ababaca")));
        System.out.println(RabinKarp.search("abababa", "aba"));
        System.out.println(RabinKarp.search("abababa", "xyz"));
        System.out.println(RabinKarp.search("abc", "abcd"));
        System.out.println(RabinKarp.search("abc", ""));

        int[] sa = SuffixArray.buildSimple("banana");
        System.out.println(java.util.Arrays.toString(sa));
        System.out.println(java.util.Arrays.toString(Kasai.buildLcp("banana", sa)));
    }
}