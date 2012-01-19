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
     * @param target The address of the new target particle
     * @param startingPoint The origi particle.
     * @param sensedSpeed Speed for the two wheels!
     */
    public static void estimateNewParticle(
            final Particle destination,
            final Particle startingPoint,
            final PolarCoordinate sensedSpeed) {

        // Get copies of the speed and location from
        // respectively the sensor and the starting point.
        final PolarCoordinate speed = sensedSpeed.copy();
        final XYPair location = startingPoint.getPosition().copy();

        // First perturb both the speed and the location,
        // then apply the movement to the location.  The time is
        // normalized to one, but that is something we need to look into.
        speed.perturb();
        location.perturb();

        // The speed from the sensor is in a coordinate system relative
        // to the robot, but we need map speed and direction, hence
        // we need to convert the coordinates, and we do that
        // using the previously known speed of the robot and then
        // perturbing that wrt the vehicle-relative speed.
        speed.convertToMapSpeed(startingPoint.getSpeed());
        location.move(speed);

        // Then finally set destination particle's speed
        // and location.

        destination.setPosition(location);
        destination.setSpeed(speed);
    }
}
