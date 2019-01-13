import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

import java.util.HashMap;

public class SAP {
    private final Digraph digraph;
    private final HashMap<Pair, Answer> cache = new HashMap<>();

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null)
            throw new IllegalArgumentException();
        digraph = new Digraph(G);
    }

    // do unit testing of this class
    public static void main(String[] args) {
        lengthTest("digraph1.txt", 3, 8, 1);
        lengthTest("digraph2.txt", 3, 4, 1);
        lengthTest("digraph3.txt", 10, 7, 3);
        lengthTest("digraph4.txt", 1, 4, 3);
        lengthTest("digraph5.txt", 17, 21, 5);
        lengthTest("digraph9.txt", 7, 4, 3);
    }

    private static void lengthTest(String path, int v, int w, int length) {
        In in = new In(path);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);

        System.out.print("Length test with " + v + " and " + w + " ");
        int len = sap.length(v, w);
        if (len == length)
            System.out.println("[passed]");
        else
            System.out.println("[failed] with length = " + len);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        Pair p = new Pair(v, w);
        if (!cache.containsKey(p)) cache.put(p, bfs(v, w));
        return cache.get(p).getLength();
    }

    private Answer bfs(int v, int w) {
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(digraph, w);
        HashMap<Integer, Integer> hs = new HashMap<>();
        for (int i = 0; i < digraph.V(); i++)
            if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
                hs.put(i, bfsV.distTo(i) + bfsW.distTo(i));
            }
        Answer ans = new Answer(-1, -1);
        for (int key : hs.keySet()) {
            int d = hs.get(key);
            if (ans.getAncestor() == -1 || ans.getLength() > d) {
                ans.setAncestor(key);
                ans.setLength(d);
            }
        }

        return ans;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        Pair p = new Pair(v, w);
        if (!cache.containsKey(p)) cache.put(p, bfs(v, w));
        return cache.get(p).getAncestor();
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        return bfs(v, w).getLength();
    }

    private Answer bfs(Iterable<Integer> v, Iterable<Integer> w) {
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(digraph, w);
        HashMap<Integer, Integer> hs = new HashMap<>();
        for (int i = 0; i < digraph.V(); i++)
            if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
                hs.put(i, bfsV.distTo(i) + bfsW.distTo(i));
            }
        Answer ans = new Answer(-1, -1);
        for (int key : hs.keySet()) {
            int d = hs.get(key);
            if (ans.getAncestor() == -1 || ans.getLength() > d) {
                ans.setAncestor(key);
                ans.setLength(d);
            }
        }

        return ans;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        return bfs(v, w).getAncestor();
    }
}
