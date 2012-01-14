package no.rmz.robotics.particlefilter;

public final class XYPair {
    private int x;
    private int y;

    public XYPair(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
    
    /**
     * The euclidian distance between this point and another.
     * @param other 
     * @param b
     * @return the euclidian distance between the points, as a double
     *         precision floating point number.
     */
    public final int distanceSquared(final XYPair other) {
        return ((int)x - (int)other.x)^2 + ((int)y - (int)other.y)^2;
    }

    XYPair copy() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    void perturb() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    void move(PolarCoordinate speed) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
