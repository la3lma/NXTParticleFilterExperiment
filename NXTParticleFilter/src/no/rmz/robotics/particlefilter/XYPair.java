package no.rmz.robotics.particlefilter;

public final class XYPair {
    private byte x;
    private byte y;

    public XYPair(final byte x, final byte y) {
        this.x = x;
        this.y = y;
    }

    public byte getX() {
        return x;
    }

    public byte getY() {
        return y;
    }

    public void setX(byte x) {
        this.x = x;
    }

    public void setY(byte y) {
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
}
