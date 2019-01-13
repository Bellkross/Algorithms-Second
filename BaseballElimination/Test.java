import java.util.Collections;
import java.util.HashSet;

public class Test {
    private final String path;
    private final String team;
    private final HashSet<String> result;

    public Test(String path, String team, String[] result) {
        this.path = path;
        this.team = team;
        this.result = new HashSet<>();
        if (result != null)
            Collections.addAll(this.result, result);
    }

    public Test(String path, String team) {
        this.path = path;
        this.team = team;
        this.result = null;
    }

    public String path() {
        return path;
    }

    public String team() {
        return team;
    }

    public HashSet<String> result() {
        return new HashSet<>(result);
    }

    @Override
    public String toString() {
        return "path: "
                + path
                + ", team: "
                + team +
                (result == null ? "" : ", result: " + this.result.toString());
    }
}
