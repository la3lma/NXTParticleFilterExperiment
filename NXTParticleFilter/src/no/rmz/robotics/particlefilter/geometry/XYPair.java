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

    public XYPair copy() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void perturb() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void move(PolarCoordinate speed) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
