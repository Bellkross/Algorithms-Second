import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.MinPQ;

import java.util.Arrays;
import java.util.HashMap;

public class BurrowsWheeler {

    // if args[0] is '-', apply Burrows-Wheeler transform
    // if args[0] is '+', apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            transform();
        }
        else if (args[0].equals("+")) {
            inverseTransform();
        }
        else {
            throw new IllegalArgumentException("args[0] must be + or -");
        }
    }

    // apply Burrows-Wheeler transform, reading from standard input and writing to standard output
    public static void transform() {
        String s = BinaryStdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(s);
        StringBuilder res = new StringBuilder();
        int first = -1;
        for (int i = 0; i < s.length(); ++i) {
            if (csa.index(i) == 0) first = i;
            res.append(csa.getSuffixAt(i).charAt(s.length() - 1));
        }
        BinaryStdOut.write(first + "\n");
        BinaryStdOut.write(res.toString());
        BinaryStdOut.flush();
        BinaryStdOut.close();
        BinaryStdIn.close();
    }

    // apply Burrows-Wheeler inverse transform, reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        char[] t = BinaryStdIn.readString().toCharArray();
        char[] sorted = new char[t.length];
        System.arraycopy(t, 0, sorted, 0, t.length);
        Arrays.sort(sorted);

        HashMap<Character, MinPQ<Integer>> smap = new HashMap<>();
        HashMap<Character, MinPQ<Integer>> tmap = new HashMap<>();
        for (int i = 0; i < t.length; i++) {
            char schar = sorted[i];
            if (!smap.containsKey(schar))
                smap.put(schar, new MinPQ<>());
            smap.get(schar).insert(i);

            char tchar = t[i];
            if (!tmap.containsKey(tchar))
                tmap.put(tchar, new MinPQ<>());
            tmap.get(tchar).insert(i);
        }

        int[] next = new int[t.length];
        for (char key : smap.keySet()) {
            MinPQ<Integer> sq = smap.get(key);
            MinPQ<Integer> tq = tmap.get(key);
            while (!sq.isEmpty()) {
                int smin = sq.delMin();
                int tmin = tq.delMin();
                next[smin] = tmin;
            }
        }
        smap.clear();
        tmap.clear();
        StringBuilder sb = new StringBuilder();
        int curr = first;
        do {
            sb.append(sorted[curr]);
            curr = next[curr];
        } while (curr != first);

        BinaryStdOut.write(sb.toString());
        BinaryStdOut.flush();
        BinaryStdOut.close();
        BinaryStdIn.close();
    }
}