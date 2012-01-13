package no.rmz.robotics.particlefilter;

/**
 * A particle is an hypothesis of where the robot is located and what its
 * speed is.
 * 
 * There is also a "weight" field that is used by the particle filter
 * algorithm to calculate the probability that this particular particle
 * represents the real location/speed of the robot.
 */
public final class Particle {
    
    /**
     * The position encoded as an XY pair in map coordinates.
     */
    private XYPair position;
    
    
    /**
     * The speed encoded as an XY pair in map coordinates.  This 
     * encodes both magnitude and direction.
     */
    private XYPair direction;
    
    
    /**
     * The weight of this particle.  The weight represents a probability
     * for this particle to be the correct one.   It is manipulated
     * by the particle filter several times during the inner filter loop.
     */
    double weight;

    public void setWeight(final double w) {
        this.weight = w;
    }

    public double getWeight() {
        return weight;
    }

    public XYPair getPosition() {
        return position;
    }

    public XYPair getDirection() {
        return direction;
    }

    public void setDirection(XYPair direction) {
        this.direction = direction;
    }
}
