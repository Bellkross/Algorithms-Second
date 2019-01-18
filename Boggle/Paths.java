import edu.princeton.cs.algs4.TrieSET;

import java.util.ArrayList;
import java.util.HashSet;

public class Paths {

    private final TrieSET dictionary;
    private final BoggleBoard board;
    private final HashSet<String> paths;

    Paths(TrieSET dictionary, BoggleBoard board) {
        this.board = board;
        this.dictionary = dictionary;
        this.paths = new HashSet<>();
        int rows = board.rows();
        int cols = board.cols();
        boolean[][][] marked = new boolean[rows*cols][rows][cols];
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                sb.delete(0, sb.toString().length() - 1);
                dfs(new Point(i, j), marked[i*cols + j], sb);
            }
        }
    }

    public Iterable<String> getPaths() {
        return paths;
    }

    private void dfs(Point p, boolean[][] marked, StringBuilder sb) {
        int i = p.i();
        int j = p.j();
        int rows = board.rows();
        int cols = board.cols();
        char ch = board.getLetter(i, j);
        String str = sb.append(ch).toString();
        if (dictionary.keysWithPrefix(str).iterator().hasNext()) {
            if (dictionary.contains(str)) paths.add(str);
            marked[i][j] = true;
            for (Point point : adj(p, rows, cols)) {
                i = point.i();
                j = point.j();
                dfs(p, marked, sb);
            }
        }

        marked[i][j] = false;
        sb.deleteCharAt(str.length() - 1);
    }

    private Iterable<Point> adj(Point p, int rows, int cols) {
        int i = p.i();
        int j = p.j();
        ArrayList<Point> points = new ArrayList<Point>();
        boolean c1 = i > 0;
        boolean c2 = j > 0;
        if (c1) points.add(new Point(i - 1, j));
        if (c2) points.add(new Point(i, j - 1));
        if (c1 && c2) points.add(new Point(i - 1, j - 1));
        c1 = i < (rows - 1);
        c2 = j < (cols - 1);
        if (c1) points.add(new Point(i + 1, j));
        if (c2) points.add(new Point(i, j + 1));
        if (c1 && c2) points.add(new Point(i + 1, j + 1));
        return points;
    }
}
