package no.rmz.robotics.particlefilter.circlemap;

import no.rmz.robotics.particlefilter.XYPair;


/**
 * A class implementing a map.  Given a position, denoted
 * as an XYPair, the circlemap will return an int that
 * gives the color of the surface at that point.  It is either
 * black (0) or true (255).
 */
public final class Circlemap {
    
    
    final XYPair  center;
    final int     innerRadiusSquared;
    final int     outerRadiusSquared;

    
    /**
     * The circle has an inner radius and an outer radius.  
     * The locations betwen the inner and outer radius
     * are black, the rest is white.
     * 
     * @param center
     * @param innerRadius
     * @param outerRadius 
     */
    public Circlemap(XYPair center, int innerRadius, int outerRadius) {
        this.center = center;
        this.innerRadiusSquared = innerRadius^2;
        this.outerRadiusSquared = outerRadius^2;
    }
    
    
    /**
     * Check if a point is inside a circle.
     * @param point the point we're checking for "insideness".
     * @param center the center of the circle.
     * @param radius The radius of the circle.
     * @return 
     */
    private  boolean insideCircle(
            final XYPair point,
            final XYPair center, 
            final int radiusSquared) {
        return point.distanceSquared(center) < radiusSquared;
    }
    
    public int getColorAt(final XYPair position) {
        if (!insideCircle(position, center, innerRadiusSquared) &&
             insideCircle(position, center, outerRadiusSquared)) {
            return Colors.WHITE;
        } else {
            return Colors.BLACK;
        }
    }
}
