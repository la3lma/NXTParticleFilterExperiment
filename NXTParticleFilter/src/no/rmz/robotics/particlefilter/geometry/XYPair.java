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
package no.rmz.robotics.particlefilter.geometry;

public final class XYPair {
    private  double x;
    private  double y;

    public XYPair(final double x, final double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }
    
    
    private static double square(final double s) {
        return s * s;
    }

    /**
     * The euclidian distance between this point and another.
     * @param other
     * @param b
     * @return the euclidian distance between the points, as a double
     *         precision floating point number.
     */
    public final double distanceSquared(final XYPair other) {
        return square(x - other.x) + square(y - other.y);
    }

  

    // Rename to "add?"
    public void move(final PolarCoordinate speed) {
       setX(speed.getXcoord() + getX());
       setY(speed.getYcoord() + getY());
    }

    public void copyTo(final XYPair destination) {
        destination.x = x;
        destination.y = y;
    }
}
