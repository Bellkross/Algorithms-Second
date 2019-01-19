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
        boolean[][][] marked = new boolean[rows * cols][rows][cols];
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rows; ++i) {
            for (int j = 0, k = i * cols + j; j < cols; k = i * cols + (++j)) {
                dfs(new Point(i, j), marked[k], sb);
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
                if (!marked[i][j])
                    dfs(point, marked, sb);
            }
        }

        marked[i][j] = false;
        sb.deleteCharAt(str.length() - 1);
    }

    private Iterable<Point> adj(Point p, int rows, int cols) {
        int x = p.i();
        int y = p.j();
        ArrayList<Point> points = new ArrayList<Point>();

        boolean c1 = x > 0;
        boolean c2 = y > 0;
        boolean c3 = x < (rows - 1);
        boolean c4 = y < (cols - 1);

        if (c2) points.add(new Point(x, y - 1));
        if (c1 && c2) points.add(new Point(x - 1, y - 1));
        if (c1) points.add(new Point(x - 1, y));
        if (c1 && c4) points.add(new Point(x - 1, y + 1));
        if (c4) points.add(new Point(x, y + 1));
        if (c3) points.add(new Point(x + 1, y));
        if (c4) points.add(new Point(x, y + 1));
        if (c3 && c4) points.add(new Point(x + 1, y + 1));
        return points;
    }
}
