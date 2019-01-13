import edu.princeton.cs.algs4.Queue;

import java.util.HashSet;

public class BaseballEliminationTest {
    public static void main(String[] args) {
        Queue<Test> queue = new Queue<>();

        queue.enqueue(new Test("teams4.txt", "Philadelphia", new String[] { "Atlanta", "New_York" }));
        queue.enqueue(new Test("teams4.txt", "Montreal", new String[] { "Atlanta" }));
        queue.enqueue(new Test("teams4.txt", "Atlanta", null));
        queue.enqueue(new Test("teams4.txt", "New_York", null));
        queue.enqueue(new Test("teams5.txt", "Detroit", new String[] { "New_York", "Baltimore", "Boston", "Toronto" }));
        queue.enqueue(new Test("teams5.txt", "New_York", null));
        queue.enqueue(new Test("teams5.txt", "Baltimore", null));
        queue.enqueue(new Test("teams5.txt", "Boston", null));
        queue.enqueue(new Test("teams5.txt", "Toronto", null));

        while (!queue.isEmpty()) performEquals(queue.dequeue());

        queue.enqueue(new Test("teams4.txt", "Philadelphia"));
        queue.enqueue(new Test("teams4a.txt", "Ghaddafi"));
        queue.enqueue(new Test("teams5.txt", "Detroit"));
        queue.enqueue(new Test("teams7.txt", "Ireland"));
        queue.enqueue(new Test("teams24.txt", "Team13"));
        queue.enqueue(new Test("teams32.txt", "Team25"));
        queue.enqueue(new Test("teams32.txt", "Team29"));
        queue.enqueue(new Test("teams36.txt", "Team21"));
        queue.enqueue(new Test("teams42.txt", "Team6"));
        queue.enqueue(new Test("teams42.txt", "Team15"));
        queue.enqueue(new Test("teams42.txt", "Team25"));
        queue.enqueue(new Test("teams48.txt", "Team6"));
        queue.enqueue(new Test("teams48.txt", "Team23"));
        queue.enqueue(new Test("teams48.txt", "Team47"));
        queue.enqueue(new Test("teams54.txt", "Team3"));
        queue.enqueue(new Test("teams54.txt", "Team29"));
        queue.enqueue(new Test("teams54.txt", "Team37"));
        queue.enqueue(new Test("teams54.txt", "Team50"));

        while (!queue.isEmpty()) performNontrivial(queue.dequeue());

    }

    private static void performNontrivial(final Test test) {
        boolean passed = certificateNontrivial(test.path(), test.team());
        String passedStr = "[passed]";
        String failedStr = "[failed]";
        System.out.println((passed ? passedStr : failedStr) + " " + test.toString());
    }

    private static boolean certificateNontrivial(final String path, final String team) {
        BaseballElimination baseballElimination = new BaseballElimination(path);
        Iterable<String> cert = baseballElimination.certificateOfElimination(team);
        int i = 0;
        if (cert == null) return false;
        for (String ignored : cert) if (++i >= 2) return true;
        return false;
    }

    private static void performEquals(final Test test) {
        boolean passed = certificateEquals(test.path(), test.team(), test.result());
        String passedStr = "[passed]";
        String failedStr = "[failed]";
        System.out.println((passed ? passedStr : failedStr) + " " + test.toString());
    }

    private static boolean certificateEquals(final String path, final String team, final HashSet<String> result) {
        BaseballElimination baseballElimination = new BaseballElimination(path);
        Iterable<String> cert = baseballElimination.certificateOfElimination(team);
        if (result.isEmpty()) {
            return cert == null && !baseballElimination.isEliminated(team);
        } else {
            if (cert == null) return false;
            for (String s : cert) {
                if (!result.contains(s)) return false;
                result.remove(s);
            }
            return result.isEmpty();
        }
    }
}
