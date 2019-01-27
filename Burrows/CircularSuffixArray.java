import java.util.Arrays;

public class CircularSuffixArray {

    private final String s;
    private final Suffix[] sorted;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null)
            throw new IllegalArgumentException("Nullable strings are forbidden!");
        this.s = s;
        sorted = new Suffix[s.length()];
        for (int i = 0; i < s.length(); ++i) {
            sorted[i] = new Suffix(s, i);
        }
        Arrays.sort(sorted);
    }

    // unit testing (required)
    public static void main(String[] args) {
        String s = "ABRACADABRA!";
        int[] arr = new int[] { 11, 10, 7, 0, 3, 5, 8, 1, 4, 6, 9, 2 };
        CircularSuffixArray csa = new CircularSuffixArray(s);
        if (csa.length() != s.length())
            System.out.println("length is wrong");
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != csa.index(i))
                System.out.println("index is wrong");
        }
    }

    // length of s
    public int length() {
        return s.length();
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i > s.length())
            throw new IllegalArgumentException("Incorrect index");
        return sorted[i].getIndex();
    }
}