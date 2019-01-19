public class Point {
    private int row;
    private int col;

    public Point(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int row() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int col() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }
}
