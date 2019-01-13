import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {

    private final WordNet wordnet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }

    // see test client below
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int[] dist = new int[nouns.length];

        for (int i = 0; i < nouns.length; ++i)
            for (int j = i + 1; j < nouns.length; ++j) {
                int d = wordnet.distance(nouns[i], nouns[j]);
                dist[i] += d;
                dist[j] += d;
            }

        int outcast = -1;
        int maxSum = -1;

        for (int i = 0; i < nouns.length; i++) {
            if (dist[i] >= maxSum) {
                maxSum = dist[i];
                outcast = i;
            }
        }

        return nouns[outcast];
    }
}
