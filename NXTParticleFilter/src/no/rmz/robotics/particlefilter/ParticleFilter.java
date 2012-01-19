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

import no.rmz.robotics.arrays.WeightedPool;
import no.rmz.robotics.sensors.SensorInput;
import no.rmz.robotics.sensors.Sensor;
import no.rmz.robotics.sensors.SensorModel;

/**
 * This class implements a particle filter localization estimation algorithm.
 *
 * It has two sets of particles, old and new, both implemented as arrays of
 * particle. The basic flow of the algorithm is:
 *
 * Start with a set of particles randomly spread out over the area where we
 * suspect the vehicle is located in.
 *
 * Then, given the current sensor input, filter and resample, then then add
 * movement based on estimated speed (with Gaussian noise) and repeat.
 *
 * XXX Refactor to niceness before continuing.
 *
 */
public final class ParticleFilter {

    /**
     * The number of particles that should replace a single selected particle.
     */
    private final int REPLACEMENT_FACTOR = 3;
    
    /**
     * A fudge factor that is used to add error to the speed estimates.
     */
    private final int PARTICLE_POSITION_ERROR = 5;
    
    /**
     * The number of particles in the particle pools. The actual number of
     * particles managed by the filter is two times the noOfParticles, since we
     * have an old and a new pool.
     */
    private final int noOfParticles;
    
    /**
     * A map that is used to calculate probable locations.
     */
    private final NavigationMap navigationMap;
    
    /**
     * A sensor with possibly a multitude of measurements.
     */
    private final Sensor sensor;
    
    /**
     * A model that based on statistics will give the probability that a sensor
     * input consistent with assumptions about consequences of a map model.
     */
    private final SensorModel sensorModel;
    
    /**
     * The object that will do something about the particle field once it has
     * been calculated.
     */
    private final ParticleFieldConsumer particleFieldConsumer;
    
    /**
     * The old particle pool. The pool that is evaluated for probabilities of
     * locations based on knowledge about maps and sensors' reactions to them.
     */
    private WeightedPool<Particle> oldParticles;
    
    /**
     * The new particle pool. The set of particles that is being built based on
     * the old particle pool.
     */
    private WeightedPool<Particle> newParticles;
    
    
    /**
     * As long as this variable is true, the filter will continue to run.
     */
    private boolean runStatus;

    public ParticleFilter(
            final int noOfParticles,
            final Sensor sensor,
            final SensorModel sensorModel,
            final ParticleFieldConsumer particleFieldConsumer,
            final NavigationMap navigationMap) {
        this.noOfParticles = noOfParticles;
        this.sensor = sensor;
        this.sensorModel = sensorModel;
        this.particleFieldConsumer = particleFieldConsumer;
        this.navigationMap = navigationMap;

        oldParticles = new WeightedPool<Particle>("a", new Particle[noOfParticles]);
        newParticles = new WeightedPool<Particle>("b", new Particle[noOfParticles]);
    }

    /**
     * This represents a simple kinematic model of the robot, and then applies
     * movement as measured to the assumed starting point and puts the new
     * particle (with speed and position) in the target particle.
     *
     * @param target The address of the new target particle
     * @param startingPoint The origi particle.
     * @param sensedSpeed Speed for the two wheels!
     */
    private void estimateNewParticle(
            final int target,
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

        final Particle destination = newParticles.get(target);
        destination.setPosition(location);
        destination.setSpeed(speed);
    }


    private boolean getRunStatus() {
        return runStatus;
    }

    public void mainLoop() {
        while (getRunStatus()) {


            // Switch old and new data

            final WeightedPool tmp = newParticles;
            newParticles = oldParticles;
            oldParticles = tmp;

            ///
            /// Sensing phase
            ///

            final SensorInput sensorInput = sensor.sense();


            ///
            /// Estimating posterior probabilities wrt sensor input
            ///

            // We'll use this to normalize later
            double sumOfWeights = 0;

            // Calculate un-normalized weights.
            for (int i = 0; i < noOfParticles; i++) {
                final Particle p = oldParticles.get(i);
                final double w =
                        sensorModel.probabilityOfMeasuredResultGivenExpectedValue(
                        navigationMap.getExpectedSensorValue(p),
                        sensorInput);
                p.setWeight(w);
                sumOfWeights += w;
            }

            oldParticles.normalizeWeights(sumOfWeights);


            ///
            /// Resampling phase
            ///


            oldParticles.sortThenCumulateWeights();

            // Resample (with replacement)
            // with probabilities of being basis
            // for resampling being based on normalized weights

            final PolarCoordinate speed = sensorInput.getSpeed();
            for (int i = 0; i < noOfParticles; i++) {
                final Particle startingPoint = oldParticles.pickInstanceAccordingToProbability();
                for (int j = 0; j < REPLACEMENT_FACTOR && i < noOfParticles; j++, i++) {
                    estimateNewParticle(i, startingPoint, speed);
                }
            }

            // At this point newData contains the best guess at the
            // present position

            particleFieldConsumer.consumeParticles(newParticles);
        }
    }
}
