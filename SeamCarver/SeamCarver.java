import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.Stack;

// corner cases
public class SeamCarver {

    private Picture picture;
    private double[] energy;
    private int energyWidth;
    private int energyHeight;
    private boolean rotated = false;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        this.picture = new Picture(picture);
        this.energyWidth = picture.width();
        this.energyHeight = picture.height();
        this.energy = new double[energyWidth * energyHeight];
        for (int x = 0; x < energyWidth; x++)
            for (int y = 0; y < energyHeight; y++) {
                if (isCorner(x, y)) {
                    energy[coordinate(x, y)] = 1000;
                }
                else {
                    energy[coordinate(x, y)] = Math.sqrt(gradientX(x, y) +
                                                                 gradientY(x, y));
                }
            }
    }

    // energy API
    private boolean isCorner(int x, int y) {
        return x == 0 || y == 0 ||
                x == (energyWidth - 1) || y == (energyHeight - 1);
    }

    private int coordinate(int x, int y) {
        return y * energyWidth + x;
    }

    private double gradientX(int x, int y) {
        return gradient(
                picture.getRGB(x + 1, y), // rigth
                picture.getRGB(x - 1, y)  // left
        );
    }

    private double gradientY(int x, int y) {
        return gradient(
                picture.getRGB(x, y + 1), // high
                picture.getRGB(x, y - 1)  // low
        );
    }

    private double gradient(int next, int prev) {
        // int takes 4 bytes
        // rgb takes 3
        // red byte, green byte, blue byte
        // 0xRB|GB|BB
        int fst = 0xFF;
        double bx = (next & fst) - (prev & fst); // take first (BB) byte
        next >>= 8; // skip byte
        prev >>= 8; // skip byte
        double gx = (next & fst) - (prev & fst); // take first (GB) byte
        next >>= 8; // skip byte
        prev >>= 8; // skip byte
        double rx = (next & fst) - (prev & fst); // take first (RB) byte
        return Math.pow(bx, 2) + Math.pow(gx, 2) + Math.pow(rx, 2);
    }

    private double[] rotatedLeftEnergy() {
        int h = energyHeight;
        int w = energyWidth;
        double[] targ = new double[w * h];
        for (int xt = w - 1, ys = 0; xt >= 0; --xt, ++ys) {
            for (int yt = 0, xs = 0; yt < h; ++yt, ++xs) {
                int from = yt * w + xt;
                int to = ys * h + xs;
                targ[to] = energy[from];
            }
        }
        return targ;
    }

    private double[] rotatedRightEnergy() {
        int h = energyHeight;
        int w = energyWidth;
        double[] targ = new double[w * h];
        for (int xt = 0, ys = 0; xt < w; ++xt, ++ys) {
            for (int yt = h - 1, xs = 0; yt >= 0; --yt, ++xs) {
                int from = yt * w + xt;
                int to = ys * h + xs;
                targ[to] = energy[from];
            }
        }
        return targ;
    }

    private void rotateLeftEnergy() {
        int h = energyHeight;
        int w = energyWidth;
        double[] targ = new double[w * h];
        for (int xt = w - 1, ys = 0; xt >= 0; --xt, ++ys) {
            for (int yt = 0, xs = 0; yt < h; ++yt, ++xs) {
                int from = yt * w + xt;
                int to = ys * h + xs;
                targ[to] = energy[from];
            }
        }
        energyWidth = h;
        energyHeight = w;
        System.arraycopy(targ, 0, energy, 0, energy.length);
    }

    private void rotateRightEnergy() {
        int h = energyHeight;
        int w = energyWidth;
        double[] targ = new double[w * h];
        for (int xt = 0, ys = 0; xt < w; ++xt, ++ys) {
            for (int yt = h - 1, xs = 0; yt >= 0; --yt, ++xs) {
                int from = yt * w + xt;
                int to = ys * h + xs;
                targ[to] = energy[from];
            }
        }
        energyWidth = h;
        energyHeight = w;
        System.arraycopy(targ, 0, energy, 0, energy.length);
    }

    private boolean isOut(int x, int y) {
        return x < 0 || y < 0 || y >= energyHeight || x >= energyWidth;
    }

    private void removeSeamEnergy(int[] seam) {
        --energyWidth;
        Stack<Integer> recalculate = new Stack<>();
        for (int y = 0; y < seam.length; y++) {
            int x = seam[y];
            energy[coordinate(x, y)] = -1;
        }

        double[] buff = new double[energy.length - seam.length];
        for (int i = 0, j = 0; i < buff.length; ++j) {
            double e = energy[j];
            if (e != -1) {
                buff[i++] = e;
            }
            else {
                recalculate.push(i);
                if (i + 1 < energyWidth * energyHeight)
                    recalculate.push(i + 1);
            }
        }

        while (!recalculate.isEmpty()) {
            int v = recalculate.pop();
            calcucalte(buff, v);
        }

        energy = buff;
    }

    private int getX(int v) {
        return v % energyWidth;
    }

    private int getY(int v) {
        return (v - getX(v)) / energyWidth;
    }

    private void calcucalte(double[] buff, int v) {
        int x = getX(v);
        int y = getY(v);
        if (isCorner(x, y)) {
            buff[v] = 1000;
        } else {
            buff[v] = Math.sqrt(gradientX(x, y) +
                                        gradientY(x, y));
        }
    }

    // current picture
    public Picture picture() {
        if (rotated) {
            rotateLeft();
            rotateLeftEnergy();
            rotated = false;
        }
        return new Picture(picture);
    }

    // width of current picture
    public int width() {
        if (rotated) {
            rotateLeft();
            rotateLeftEnergy();
            rotated = false;
        }
        return picture.width();
    }

    // height of current picture
    public int height() {
        if (rotated) {
            rotateLeft();
            rotateLeftEnergy();
            rotated = false;
        }
        return picture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (isOut(x, y))
            throw new IllegalArgumentException("Incorrect coordinates!");
        return energy[coordinate(x, y)];
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        int[] seam = rotated ?
                     findSeam(energy, energyWidth, energyHeight) :
                     findSeam(rotatedRightEnergy(), energyHeight, energyWidth);
        transform(seam, rotated ? energyWidth : energyHeight);
        return seam;
    }

    private void transform(int[] seam, int width) {
        for (int i = 0; i < seam.length; i++)
            seam[i] = (width - 1) - seam[i];
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        return rotated ?
               findSeam(rotatedLeftEnergy(), energyHeight, energyWidth) :
               findSeam(energy, energyWidth, energyHeight);
    }

    private int[] findSeam(double[] e, int w, int h) {
        return new EnergySP(e, w, h).path();
    }

    private void rotateRight() {
        int w = picture.width();
        int h = picture.height();
        Picture targ = new Picture(h, w);
        for (int xt = 0, ys = 0; xt < w; ++xt, ++ys)
            for (int yt = h - 1, xs = 0; yt >= 0; --yt, ++xs)
                targ.setRGB(xs, ys, picture.getRGB(xt, yt));
        picture = targ;
    }

    private void rotateLeft() {
        int w = picture.width();
        int h = picture.height();
        Picture targ = new Picture(h, w);
        for (int xt = w - 1, ys = 0; xt >= 0; --xt, ++ys)
            for (int yt = 0, xs = 0; yt < h; ++yt, ++xs)
                targ.setRGB(xs, ys, picture.getRGB(xt, yt));
        picture = targ;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (rotated)
            checkSeam(seam, energyHeight, energyWidth);
        else
            checkSeam(seam, energyWidth, energyHeight);

        if (picture.height() <= 1)
            throw new IllegalArgumentException("Incorrect picture size");
        if (!rotated) {
            rotateRight();
            rotateRightEnergy();
            rotated = true;
        }
        transform(seam, picture.width());
        removeSeam(seam);
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (rotated)
            checkSeam(seam, energyWidth, energyHeight);
        else
            checkSeam(seam, energyHeight, energyWidth);

        if (picture.width() <= 1)
            throw new IllegalArgumentException("Incorrect picture size");
        if (rotated) {
            rotateLeft();
            rotateLeftEnergy();
            rotated = false;
        }
        removeSeam(seam);
    }

    private void checkSeam(int[] seam, int seamLength, int range) {
        if (seam == null)
            throw new IllegalArgumentException("Seam can't be null");
        if (seam.length != seamLength)
            throw new IllegalArgumentException("Incorrect seam size");
        int possibleDiff = 1;
        for (int i = 0; i < seamLength; i++) {
            if (seam[i] >= range || seam[i] < 0)
                throw new IllegalArgumentException("Seam item out of range");
        }
        for (int i = 0; i < seamLength - 1; i++) {
            if (Math.abs(seam[i] - seam[i + 1]) > possibleDiff)
                throw new IllegalArgumentException("Impossible seam distance");
        }
    }

    private void removeSeam(int[] seam) {
        Picture copy = new Picture(picture.width() - 1, picture.height());
        for (int y = 0; y < picture.height(); y++)
            for (int x = 0, xc = 0; x < picture.width(); x++)
                if (seam[y] != x)
                    copy.setRGB(xc++, y, picture.getRGB(x, y));
        picture = copy;
        removeSeamEnergy(seam);
    }
}