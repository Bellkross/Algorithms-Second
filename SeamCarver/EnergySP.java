import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;

import java.util.Arrays;

public class EnergySP {

    private int minFrom;
    private int minTo;
    private final int energyWidth;
    private final int energyHeight;
    private int[][] edgeTo;
    private double[][] distTo;
    private int[] shortestPath;

    public EnergySP(double[] energy, int energyWidth, int energyHeight) {
        this.edgeTo = new int[energyWidth][energy.length];
        this.distTo = new double[energyWidth][energy.length];
        this.energyWidth = energyWidth;
        this.energyHeight = energyHeight;

        for (int from = 0; from < energyWidth; ++from) {
            Arrays.fill(distTo[from], Double.POSITIVE_INFINITY);
            distTo[from][from] = 1000.0;
            edgeTo[from][from] = from;
        }

        for (int from = 0; from < energyWidth; ++from)
            findPath(energy, from);

        double minDist = Double.POSITIVE_INFINITY;
        for (int from = 0; from < energyWidth; ++from) {
            for (int to = coordinate(0, energyHeight - 1); to < energyWidth * energyHeight; to++) {
                double dist = distTo[from][to];
                if (dist < minDist) {
                    minFrom = from;
                    minTo = to;
                    minDist = dist;
                }
            }
        }

        fillShortestPath(coordinatesPath(minFrom, minTo));
    }

    private void findPath(double[] energy, int from) {
        boolean[] marked = new boolean[energy.length];
        Queue<Integer> queue = new Queue<>();
        queue.enqueue(from);
        marked[from] = true;
        while (!queue.isEmpty()) {
            int v = queue.dequeue();
            for (int w : adj(v)) {
                if (!marked[w]) {
                    marked[w] = true;
                    queue.enqueue(w);
                }
                relax(energy, from, v, w);
            }
        }
    }

    private void relax(double[] energy, int s, int v, int w) {
        double dv = distTo[s][v];
        double dw = distTo[s][w];
        double dist = dv + energy[w];
        if (dist < dw) {
            distTo[s][w] = dist;
            edgeTo[s][w] = v;
        }
    }

    private Stack<Integer> coordinatesPath(int v1, int v2) {
        Stack<Integer> stack = new Stack<>();
        int curr = v2;
        stack.push(curr);
        while (curr != v1) {
            curr = edgeTo[v1][curr];
            stack.push(curr);
        }
        return stack;
    }

    public Iterable<Integer> adj(int v) {
        int x = getX(v);
        int y = getY(v);
        Bag<Integer> bag = new Bag<>();
        int tmpX = x - 1, tmpY = y + 1;
        if (!isOut(tmpX, tmpY))
            bag.add(coordinate(tmpX, tmpY));
        tmpX = x;
        if (!isOut(tmpX, tmpY))
            bag.add(coordinate(tmpX, tmpY));
        tmpX = x + 1;
        if (!isOut(tmpX, tmpY))
            bag.add(coordinate(tmpX, tmpY));
        return bag;
    }

    private boolean isOut(int x, int y) {
        return x < 0 || y < 0 || y >= energyHeight || x >= energyWidth;
    }

    public int coordinate(int x, int y) {
        return y * energyWidth + x;
    }

    public int getX(int v) {
        return v % energyWidth;
    }

    public int getY(int v) {
        return (v - getX(v)) / energyWidth;
    }

    private void fillShortestPath(Stack<Integer> stack) {
        int i = 0;
        shortestPath = new int[stack.size()];
        while (!stack.isEmpty())
            shortestPath[i++] = getX(stack.pop());
    }

    public int[] path() {
        return Arrays.copyOf(shortestPath, shortestPath.length);
    }

    public double distance() {
        return distTo[minFrom][minTo];
    }

}