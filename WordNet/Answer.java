public class Answer {
    private int length;
    private int ancestor;

    public Answer(int len, int a) {
        length = len;
        ancestor = a;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getAncestor() {
        return ancestor;
    }

    public void setAncestor(int ancestor) {
        this.ancestor = ancestor;
    }
}
