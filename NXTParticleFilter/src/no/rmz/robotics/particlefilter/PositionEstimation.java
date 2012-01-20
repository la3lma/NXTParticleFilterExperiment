/**
 * Copyright 2012 Bjørn Remseth
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package no.rmz.robotics.particlefilter;

import no.rmz.robotics.particlefilter.geometry.PolarCoordinate;
import no.rmz.robotics.particlefilter.geometry.XYPair;

public class PositionEstimation {
    /**
     * This represents a simple kinematic model of the robot, and then applies
     * movement as measured to the assumed starting point and puts the new
     * particle (with speed and position) in the target particle.
     * 
     * A sight error is added to both the speed and the location
     *
     * @param destination The address of the new target particle
     * @param startingPoint The origi particle.
     * @param sensedSpeed Speed for the two wheels!
     */
    public static void estimateNewParticle(
            final Particle destination,
            final Particle startingPoint,
            final PolarCoordinate sensedSpeed) {

        // XXX This method  is perhaps not necessary, it's so small.
     
        startingPoint.copyTo(destination);
        applyMovement(destination.getPosition(), destination.getSpeed(), sensedSpeed);
        
        // XXX Perturbations are not added yet
    }
    
    
   
    /**
     * Apply the applicationSpeed 
     * @param position
     * @param speed
     * @param applicationSpeed 
     */
    public static void applyMovement(
            final XYPair position,
            final PolarCoordinate speed,
            final PolarCoordinate applicationSpeed) {
        
        // First modify the speed to map coordinate speed
        speed.setTheta(speed.getTheta() + applicationSpeed.getTheta());
        speed.setRadius(applicationSpeed.getRadius());
        
        // Then apply that movement
        position.move(speed);
    }
}
