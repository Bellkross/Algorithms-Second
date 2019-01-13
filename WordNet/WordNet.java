import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Topological;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;

public class WordNet {

    private final Digraph digraph;
    private final SAP sap;
    private final HashMap<String, HashSet<Integer>> nouns = new HashMap<>();
    private final HashMap<Integer, HashSet<String>> synset = new HashMap<>();

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null)
            throw new IllegalArgumentException("Nullable parameter problem.");
        In inSynsets = new In(synsets);
        while (true) {
            String line = inSynsets.readLine();
            if (line == null) break;
            String[] arr = line.split(",");
            putSynset(arr[0], arr[1]);
        }
        digraph = new Digraph(synset.size());
        In inHypernyms = new In(hypernyms);
        while (true) {
            String line = inHypernyms.readLine();
            if (line == null) break;
            String[] arr = line.split(",");
            putHypernym(arr[0], Arrays.copyOfRange(arr, 1, arr.length));
        }
        inSynsets.close();
        inHypernyms.close();

        int root = hasOneRoot(digraph);
        Topological topological = new Topological(digraph);
        if (root == -1 || !topological.hasOrder())
            throw new IllegalArgumentException("Input is not rooted DAG");
        sap = new SAP(digraph);
    }

    // put key and values represented as String, String[] into synset
    private void putSynset(String sId, String set) {
        int id = Integer.parseInt(sId);
        String[] arr = set.split(" ");
        HashSet<String> hs = Arrays.stream(arr)
                                   .collect(Collectors.toCollection(HashSet::new));
        synset.put(id, hs);
        for (String a : arr) {
            if (!nouns.containsKey(a)) nouns.put(a, new HashSet<Integer>());
            nouns.get(a).add(id);
        }
    }

    // put key and values represented as String, String[] into hypernyms
    private void putHypernym(String sId, String[] set) {
        if (set.length == 0) return;
        int v = Integer.parseInt(sId);
        for (String i : set) {
            int w = Integer.parseInt(i);
            digraph.addEdge(v, w);
        }
    }

    private int hasOneRoot(Digraph dg) {
        if (dg.V() == 0) return -1;
        if (dg.V() == 1) return 0;
        int root = 0;
        int count = 0;
        for (int v = 0; v < dg.V(); v++)
            if (dg.outdegree(v) == 0 &&
                    dg.indegree(v) >= 1) {
                ++count;
                root = v;
            }
        return (count == 1) ? root : -1;
    }

    public static void main(String[] args) {
        WordNet wn = new WordNet("synsets.txt", "hypernyms.txt");
        System.out.println("synset size: " + wn.synset.size());
        printAncestor(wn, "municipality", "region");
        printDistance(wn, "municipality", "region");
        System.out.println("Info: " + wn.distance("municipality", "region"));
        System.out.println("Info: " + wn.distance("municipality", "region"));
        System.out.println("Testing: ");
        checkEquals(wn.nouns.size(), 119188);
        checkDistance(wn, "worm", "bird", 5);
        checkDistance(wn, "white_marlin", "mileage", 23);
        checkDistance(wn, "Black_Plague", "black_marlin", 33);
        checkDistance(wn, "American_water_spaniel", "histology", 27);
        checkDistance(wn, "Brown_Swiss", "barrel_roll", 29);
        checkDistance(wn, "municipality", "region", 3);
        checkAncestor(wn, "individual", "edible_fruit", "physical_entity");
        checkDistance(wn, "individual", "edible_fruit", 7);
    }

    private static void printAncestor(WordNet wn, String a, String b) {
        System.out.println("Distance for " + a + " and " + b + " = " + wn.sap(a, b));
    }

    private static void printDistance(WordNet wn, String a, String b) {
        System.out.println("Distance for " + a + " and " + b + " = " + wn.distance(a, b));
    }

    private static void checkAncestor(WordNet wn, String a, String b, String ancestor) {
        System.out.print("Check ancestor for " + a + " and " + b);
        String anc = wn.sap(a, b);
        if (!anc.equals(ancestor)) System.out.println("[Incorrect ancestor: " + anc + "]");
        else System.out.println("[passed]");
    }

    private static void checkDistance(WordNet wn, String a, String b, int distance) {
        System.out.print("Check distance for " + a + " and " + b);
        int d = wn.distance(a, b);
        if (d != distance) System.out.println("[Incorrect distance: " + d + "]");
        else System.out.println("[passed]");
    }
    private static void checkEquals(Object a, Object b) {
        System.out.print(a.toString() + ".equals(" + b.toString() + ")");
        if (a.equals(b)) System.out.println("[passed]");
        else System.out.println("[failed]");
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException("Not WordNet noun");

        return sap.length(nouns.get(nounA), nouns.get(nounB));
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null)
            throw new IllegalArgumentException("Noun can't be null");
        return nouns.containsKey(word);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nouns.keySet();
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException("Not WordNet noun");
        return synset.get(
                sap.ancestor(
                        nouns.get(nounA),
                        nouns.get(nounB)
                )
        ).iterator().next();
    }

}
