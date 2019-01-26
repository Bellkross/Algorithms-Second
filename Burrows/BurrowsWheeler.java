import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

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
        /*
            1. Подай файл на вход
            2. Найди как найти ту самую строку
         */
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

    }
}