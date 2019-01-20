import edu.princeton.cs.algs4.TrieSET;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Paths {

    private final TrieSET dictionary;
    private final BoggleBoard board;
    private final HashSet<String> paths;
    private final HashMap<Point, Iterable<Point>> adj;
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

        for (Point p : adj.keySet()) {
            dfs(p, marked[p.row() * cols + p.col()], sb);
        }
    }

    public Iterable<String> getPaths() {
        return paths;
    }

    private void dfs(Point p, boolean[][] marked, StringBuilder sb) {
        int row = p.row();
        int col = p.col();
        char ch = board.getLetter(row, col);
        String str = sb.append(ch == 'Q' ? "QU" : ch).toString();
        if (dictionary.keysWithPrefix(str).iterator().hasNext()) {
            if (str.length() > 2 && dictionary.contains(str)) paths.add(str);
            marked[row][col] = true;
            for (Point point : adj.get(p))
                if (!marked[point.row()][point.col()])
                    dfs(point, marked, sb);
        }

        marked[row][col] = false;
        if (ch == 'Q') sb.delete(str.length() - 2, str.length());
        else sb.deleteCharAt(str.length() - 1);
    }

    private Iterable<Point> adj(Point p, int rows, int cols) {
        if (adj.containsKey(p)) return adj.get(p);
        int row = p.row();
        int column = p.col();
        ArrayList<Point> pts = new ArrayList<Point>();

        boolean a = row > 0,
                b = column > 0,
                c = row < (rows - 1),
                d = column < (cols - 1);

        if (a) pts.add(upper(p));
        if (d && a) pts.add(upperRight(p));
        if (d) pts.add(right(p));
        if (c && d) pts.add(bottomRight(p));
        if (c) pts.add(bottom(p));
        if (b && c) pts.add(bottomLeft(p));
        if (b) pts.add(left(p));
        if (a && b) pts.add(upperLeft(p));

        return pts;
    }

    private Point upper(Point p) { return points[(p.row() - 1) * board.cols() + p.col()]; }
    private Point bottom(Point p) { return points[(p.row() + 1) * board.cols() + p.col()]; }
    private Point left(Point p) { return points[p.row() * board.cols() + (p.col() - 1)]; }
    private Point right(Point p) { return points[p.row() * board.cols() + (p.col() + 1)]; }
    private Point upperLeft(Point p) { return points[(p.row() - 1) * board.cols() + (p.col() - 1)]; }
    private Point bottomLeft(Point p) { return points[(p.row() + 1) * board.cols() + (p.col() - 1)]; }
    private Point upperRight(Point p) { return points[(p.row() - 1) * board.cols() + (p.col() + 1)]; }
    private Point bottomRight(Point p) { return points[(p.row() + 1) * board.cols() + (p.col() + 1)]; }
}
