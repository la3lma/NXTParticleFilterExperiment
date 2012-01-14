package no.rmz.robotics.particlefilter;

public final class PolarCoordinate {
    
    private double theta;
    private double radius;

    public PolarCoordinate(final double theta, final double radius) {
        this.theta = theta;
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }

    public double getTheta() {
        return theta;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public void setTheta(double theta) {
        this.theta = theta;
    }

    // XXX Change the coordinate a little bit
    void perturb() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    PolarCoordinate copy() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    void convertToMapSpeed(PolarCoordinate speed) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
