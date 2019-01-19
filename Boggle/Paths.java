import edu.princeton.cs.algs4.TrieSET;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Paths {

    private final TrieSET dictionary;
    private final BoggleBoard board;
    private final HashSet<String> paths;
    private final HashMap<Point, ArrayList<Point>> adj;
    private final Point[] points;

    Paths(TrieSET dictionary, BoggleBoard board) {
        int rows = board.rows();
        int cols = board.cols();

        this.board = board;
        this.dictionary = dictionary;
        this.paths = new HashSet<>();
        this.adj = new HashMap<>();
        this.points = new Point[rows * cols];

        for (int row = 0; row < rows; ++row)
            for (int col = 0; col < cols; ++col)
                points[row * cols + col] = new Point(row, col);

        for (int row = 0; row < rows; ++row) {
            for (int col = 0; col < cols; ++col) {
                Point p = points[row * cols + col];
                adj.put(p, adj(p, rows, cols));
            }
        }

        boolean[][][] marked = new boolean[rows * cols][rows][cols];
        StringBuilder sb = new StringBuilder();

        for (Point p : adj.keySet())
            dfs(p, marked[p.row() * cols + p.col()], sb);
    }

    public Iterable<String> getPaths() {
        return paths;
    }

    private void dfs(Point p, boolean[][] marked, StringBuilder sb) {
        int row = p.row();
        int col = p.col();
        char ch = board.getLetter(row, col);
        String str = sb.append(ch).toString();
        if (dictionary.keysWithPrefix(str).iterator().hasNext()) {
            if (str.length() > 2 && dictionary.contains(str)) paths.add(str);
            marked[row][col] = true;
            for (Point point : adj.get(p)) {
                row = point.row();
                col = point.col();
                if (!marked[row][col])
                    dfs(point, marked, sb);
            }
        }

        marked[row][col] = false;
        sb.deleteCharAt(str.length() - 1);
    }

    private ArrayList<Point> adj(Point p, int rows, int cols) {
        if (adj.containsKey(p)) return adj.get(p);
        int row = p.row();
        int column = p.col();
        ArrayList<Point> pts = new ArrayList<Point>();

        boolean a = row > 0,
                b = column > 0,
                c = row < (rows - 1),
                d = column < (cols - 1);

        int i;
        int j;

        i = row;
        j = column - 1;
        if (b) pts.add(points[i * cols + j]);

        i = row - 1;
        j = column - 1;
        if (a && b) pts.add(points[i * cols + j]);

        i = row - 1;
        j = column;
        if (a) pts.add(points[i * cols + j]);

        i = row - 1;
        j = column + 1;
        if (a && d) pts.add(points[i * cols + j]);

        i = row;
        j = column + 1;
        if (d) pts.add(points[i * cols + j]);

        i = row + 1;
        j = column + 1;
        if (c && d) pts.add(points[i * cols + j]);

        i = row + 1;
        j = column;
        if (c) pts.add(points[i * cols + j]);

        i = row + 1;
        j = column - 1;
        if (b && c) pts.add(points[i * cols + j]);

        return pts;
    }
}
