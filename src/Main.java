import dp.EditDistance;
import dp.LCS;

public class Main {
    public static void main(String[] args) {
        System.out.println(LCS.length("ACD", "ABCD"));
        System.out.println(LCS.length("", "abc"));
        System.out.println(EditDistance.distance("kitten", "sitting"));
        System.out.println(EditDistance.distance("", "abc"));
        System.out.println(EditDistance.distance("abc", "abc"));
    }
}