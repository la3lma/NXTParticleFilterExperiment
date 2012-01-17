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

    @Override
    public void setWeight(final double w) {
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

    public void setPosition(XYPair position) {
        this.position = position;
    }

    public void setSpeed(PolarCoordinate speed) {
        this.speed = speed;
    }   
}
