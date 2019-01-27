import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class MoveToFront {

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            encode();
        }
        else if (args[0].equals("+")) {
            decode();
        }
        else {
            throw new IllegalArgumentException("args[0] must be + or -");
        }
    }

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        String s = BinaryStdIn.readString();
        LinkedList<Character> alpha = new LinkedList<>();
        for (int i = 0; i < 256; ++i) alpha.add((char) i);

        LinkedList<Integer> out = new LinkedList<>();
        for (char ch : s.toCharArray()) {
            Iterator<Character> it = alpha.iterator();
            int i = -1;
            while (it.hasNext()) {
                char curr = it.next();
                ++i;
                if (curr == ch) {
                    it.remove();
                    alpha.addFirst(curr);
                    out.add(i);
                    break;
                }
            }
        }
        for (int i : out) {
            BinaryStdOut.write(i, 8);
            BinaryStdOut.write(" ");
        }
        BinaryStdOut.flush();
        BinaryStdOut.close();
        BinaryStdIn.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        LinkedList<Character> alpha = new LinkedList<>();
        for (int i = 0; i < 256; ++i) alpha.add((char) i);

        ArrayList<Integer> arr = new ArrayList<>();
        while (!BinaryStdIn.isEmpty()) {
            arr.add(BinaryStdIn.readInt(8));
        }

        for (int i : arr) {
            byte j = -1;
            Iterator<Character> it = alpha.iterator();
            while (++j != i) it.next();
            char ch = it.next();
            it.remove();
            alpha.addFirst(ch);
            BinaryStdOut.write(ch);
        }

        BinaryStdOut.flush();
        BinaryStdOut.close();
        BinaryStdIn.close();
    }
}