import edu.princeton.cs.algs4.TrieSET;

import java.util.HashSet;

public class BoggleSolver {

    private final TrieSET dictionary = new TrieSET();

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        for (String word : dictionary) this.dictionary.add(word);
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        return new HashSet<>();
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        int len = word.length();
        if (len >= 8) return 11;
        if (len >= 7) return 5;
        if (len >= 6) return 3;
        if (len >= 5) return 2;
        if (len >= 3) return 1;
        return 0;
    }

    public static void main(String[] args) {

    }
}
