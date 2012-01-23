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

    public void convertToMapSpeed(PolarCoordinate speed) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void copyTo(PolarCoordinate destination) {
        destination.radius = radius;
        destination.theta  = theta;
    }

    public double getXcoord() {
        return radius * Math.cos(theta);
    }
    
      public double getYcoord() {
         return radius * Math.sin(theta);
    }
}
