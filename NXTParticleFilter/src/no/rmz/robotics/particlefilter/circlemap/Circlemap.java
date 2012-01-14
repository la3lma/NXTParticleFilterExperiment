/**
 *  Copyright 2012 Bjørn Remseth
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

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
