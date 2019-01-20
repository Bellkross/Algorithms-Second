import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stopwatch;
import edu.princeton.cs.algs4.TST;

public class BoggleSolver {

    private final TST<Integer> dictionary = new TST<Integer>();

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        for (String word : dictionary) this.dictionary.put(word, 0);
    }

    public static void main(String[] args) {
        scores();
        perfomance();
    }

    private static void perfomance() {
        System.out.println("PerfomanceTest");
        int times, n = 4, m = 4;
        Stopwatch stopwatch;

        // test 1
        times = 10;
        stopwatch = new Stopwatch();
        perfomance(times);
        System.out.println(times + " times " + m + " x " + n + " board");
        System.out.println("time: " + stopwatch.elapsedTime());

        // test 2
        times = 100;
        stopwatch = new Stopwatch();
        perfomance(times);
        System.out.println(times + " times " + m + " x " + n + " board");
        System.out.println("time: " + stopwatch.elapsedTime());

        // test 3
        times = 1000;
        stopwatch = new Stopwatch();
        perfomance(times);
        System.out.println(times + " times " + m + " x " + n + " board");
        System.out.println("time: " + stopwatch.elapsedTime());

    }

    private static void perfomance(int times) {
        In in = new In("dictionary-yawl.txt");
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        for (int i = 0; i < times; ++i) solver.getAllValidWords(new BoggleBoard());
    }

    private static void scores() {
        System.out.println("Scores test");
        String dict = "dictionary-yawl.txt";
        String bp = "board-points";
        String[] testCases = new String[] {
                "board-points0.txt", "board-points1.txt", "board-points100.txt",
                "board-points1000.txt", "board-points1111.txt", "board-points1250.txt",
                "board-points13464.txt", "board-points1500.txt", "board-points2.txt",
                "board-points200.txt", "board-points2000.txt", "board-points26539.txt",
                "board-points3.txt", "board-points300.txt", "board-points4.txt",
                "board-points400.txt", "board-points4410.txt", "board-points4527.txt",
                "board-points4540.txt", "board-points5.txt", "board-points500.txt",
                "board-points750.txt", "board-points777.txt"
        };
        Stopwatch stopwatch = new Stopwatch();
        int failure = 0;
        int success = 0;
        for (String tc : testCases) {
            int points = Integer.parseInt(tc.substring(bp.length(), tc.indexOf('.')));
            if (!scoreTest(dict, tc, points)) {
                System.out.println("[ failed ] " + tc);
                ++failure;
            }
            else ++success;
        }
        System.out.println("passed: " + success + " \\ " + testCases.length);
        System.out.println("failed: " + failure + " \\ " + testCases.length);
        System.out.println("time: " + stopwatch.elapsedTime());
    }

    private static boolean scoreTest(String dictionaryName, String boardName, int res) {
        In in = new In(dictionaryName);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(boardName);
        int score = 0;
        Iterable<String> validWords = solver.getAllValidWords(board);
        for (String word : validWords) {
            // StdOut.println(word);
            score += solver.scoreOf(word);
        }
        // StdOut.println("Score = " + score);
        return score == res;
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        Paths paths = new Paths(dictionary, board);
        return paths.getPaths();
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (!dictionary.contains(word)) return 0;
        int len = word.length();
        if (len >= 8) return 11;
        if (len >= 7) return 5;
        if (len >= 6) return 3;
        if (len >= 5) return 2;
        if (len >= 3) return 1;
        return 0;
    }
}
