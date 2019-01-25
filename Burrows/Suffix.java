public class Suffix implements Comparable<Suffix> {

    private final String s;
    private final int index;

    public Suffix(String s, int index) {
        this.s = s;
        this.index = index;
    }

    public char getCharAt(int i) {
        return s.charAt((index + i) % s.length());
    }

    public int getIndex() {
        return index;
    }

    @Override
    public int compareTo(Suffix that) {
        for (int i = 0; i < s.length(); i++) {
            if (this.getCharAt(i) > that.getCharAt(i))
                return 1;
            else if (this.getCharAt(i) < that.getCharAt(i))
                return -1;
        }
        return 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); ++i)
            sb.append(this.getCharAt(i));
        return sb.toString();
    }
}
