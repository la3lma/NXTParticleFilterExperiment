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

package no.rmz.robotics.particlefilter;

import no.rmz.robotics.particlefilter.geometry.PolarCoordinate;
import no.rmz.robotics.particlefilter.geometry.XYPair;
import no.rmz.robotics.arrays.Weighted;

/**
 * A particle is an hypothesis of where the robot is located and what its
 * speed is.
 *
 * There is also a "weight" field that is used by the particle filter
 * algorithm to calculate the probability that this particular particle
 * represents the real location/speed of the robot.
 */
public final class Particle  implements Weighted {

    /**
     * The position encoded as an XY pair in map coordinates.
     */
    private XYPair position;


    /**
     * The speed encoded as an XY pair in map coordinates.  This
     * encodes both magnitude and direction.
     */
    private PolarCoordinate speed;


    /**
     * The weight of this particle.  The weight represents a probability
     * for this particle to be the correct one.   It is manipulated
     * by the particle filter several times during the inner filter loop.
     */
    double weight;

    public Particle(final XYPair position, final PolarCoordinate speed, final double weight) {
        this.position = position;
        this.speed = speed;
        this.weight = weight;

        if (weight <= 0.0) {
            throw new IllegalArgumentException("Weight must be positive, but was: " + weight);
        }
    }

    
    @Deprecated
    public Particle() {
        this(new XYPair(0, 0), new PolarCoordinate(0.0, 0.0), 0.1);
    }
    
    
    

    @Override
    public void setWeight(final double w) {
        if (w <= 0.0) {
            throw new IllegalArgumentException("Attempt to set weight that wasn't positive, was: " +w);
        }
        this.weight = w;
    }

    @Override
    public double getWeight() {
        return weight;
    }

    public XYPair getPosition() {
        return position;
    }

    public PolarCoordinate getSpeed() {
        return speed;
    }

    public void setPosition(final XYPair position) {
        this.position = position;
    }

    public void setSpeed(final PolarCoordinate speed) {
        this.speed = speed;
    }

    // Copy everything, except weight, which is set to 
    // zero
    void copyTo(final Particle destination) {
        destination.weight = 0;
        position.copyTo(destination.getPosition());
        speed.copyTo(destination.getSpeed());
    }
}
