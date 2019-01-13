import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class BaseballElimination {

    private final HashMap<String, Integer> teamIndex;
    private final String[] teams;
    private final int[] w;
    private final int[] l;
    private final int[] r;
    private final int[][] g;
    // answer cache
    private final HashMap<Integer, Iterable<String>> eliminationCertificate;
    private final HashMap<Integer, Boolean> eliminated;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In input = new In(filename);
        int teamsNumber = input.readInt();

        teamIndex = new HashMap<>(teamsNumber);
        teams = new String[teamsNumber];
        w = new int[teamsNumber];
        l = new int[teamsNumber];
        r = new int[teamsNumber];
        g = new int[teamsNumber][teamsNumber];

        for (int i = 0; i < teamsNumber; ++i) {
            teams[i] = input.readString();
            teamIndex.put(teams[i], i);
            w[i] = input.readInt();
            l[i] = input.readInt();
            r[i] = input.readInt();
            for (int j = 0; j < teamsNumber; ++j)
                g[i][j] = input.readInt();
        }

        // answer cache
        eliminated = new HashMap<>(teamsNumber);
        eliminationCertificate = new HashMap<>(teamsNumber);
    }

    // number of teams
    public int numberOfTeams() {
        return teams.length;
    }

    // all teams
    public Iterable<String> teams() {
        return Arrays.asList(teams);
    }

    // number of wins for given team
    public int wins(String team) {
        return w[teamIndex.get(team)];
    }

    // number of losses for given team
    public int losses(String team) {
        return l[teamIndex.get(team)];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        return r[teamIndex.get(team)];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        return g[teamIndex.get(team1)][teamIndex.get(team2)];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        int x = teamIndex.get(team);
        if (!eliminated.containsKey(x))
            eliminated.put(x, certificateOfElimination(team) != null);
        return eliminated.get(x);
    }

    private Iterable<String> trivialCertificate(int x) {
        Stack<String> certificate = new Stack<>();
        for (int i = 0; i < teams.length; i++)
            if (w[x] + r[x] < w[i]) certificate.push(teams[i]);
        return certificate;
    }

    private Iterable<String> nonTrivialCertificate(int x) {
        Stack<String> certificate = new Stack<>();

        int tlen = teams.length;
        int networkCapacity = tlen; // add teams, s will be at x's position

        int pairs = 0; // count pairs amount
        int i = networkCapacity - 1 - 1; // remove x, remove s
        while (i >= 1) pairs += i--;

        networkCapacity += pairs + 1; // add pairs, add t

        FlowNetwork fn = new FlowNetwork(networkCapacity);
        int s = x;
        int t = fn.V() - 1;
        for (int team = 0; team < tlen; ++team) // fill from team to t edges
            if (team != s) {
                int cap = w[x] + r[x] - w[team];
                fn.addEdge(new FlowEdge(team, t, cap > 0 ? cap : 0, 0));
            }

        assert (tlen + pairs == t);
        int pair = tlen;
        int lastPair = tlen - 1;
        for (int from = 0; from <= lastPair; from++) {
            if (from == s) continue;
            for (int to = from + 1; to <= lastPair; to++) {
                if (to == s) continue;
                fn.addEdge(new FlowEdge(s /* from */, pair /* to */, g[from][to],
                                        0)); // fill s -> pair
                fn.addEdge(
                        new FlowEdge(pair, from, Double.POSITIVE_INFINITY, 0)); // pair -> from
                fn.addEdge(new FlowEdge(pair, to, Double.POSITIVE_INFINITY, 0)); // pair -> to
                ++pair;
            }
        }
        FordFulkerson ff = new FordFulkerson(fn, s, t);

        for (int v = 0; v < teams.length; ++v)
            if (v != s && ff.inCut(v))
                certificate.push(teams[v]);

        return certificate;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String teamName) {
        int x = teamIndex.get(teamName);
        if (!eliminationCertificate.containsKey(x)) {
            HashSet<String> set = new HashSet<>();
            for (String team : trivialCertificate(x)) set.add(team);
            if (set.isEmpty()) {
                for (String team : nonTrivialCertificate(x)) set.add(team);
            }
            eliminationCertificate.put(x, (set.isEmpty() ? null : set));
        }
        return eliminationCertificate.get(x);
    }
}