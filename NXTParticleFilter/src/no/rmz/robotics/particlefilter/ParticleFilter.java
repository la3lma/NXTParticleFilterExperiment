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

        if (noOfParticles < 1) {
            throw new IllegalArgumentException("noOfParticles must be positive");
        }

        if (sensor == null) {
            throw new IllegalArgumentException("Sensor can't be null");
        }

        if (sensorModel == null) {
            throw new IllegalArgumentException("sensorModel can't be null");
        }

        if (particleFieldConsumer == null) {
            throw new IllegalArgumentException("particleFieldConsumer can't be null");
        }

        if (navigationMap == null) {
            throw new IllegalArgumentException("Navigation map can't be null");
        }

        oldParticles = new WeightedPool<Particle>("a", new Particle[noOfParticles]);
        newParticles = new WeightedPool<Particle>("b", new Particle[noOfParticles]);
    }



    private boolean getRunStatus() {
        return runStatus;
    }

    /**
     * A single round of sense/estimate. It is factored out as a method to be
     * easier to test, hence it is also public although there is no intrinsic
     * reason for it to be public.
     */
    public void senseEstimate() {

        // Switch old and new data

        final WeightedPool tmp = newParticles;
        newParticles = oldParticles;
        oldParticles = tmp;

        ///
        /// Sensing phase
        ///
        if (sensor == null) {
            throw new RuntimeException("Sensor is null");
        }
        final SensorInput sensorInput = sensor.sense();


        ///
        /// Estimating posterior probabilities wrt sensor input
        ///

        // We'll use this to normalize later
        double sumOfWeights = 0;

        if (oldParticles == null) {
            throw new RuntimeException("oldParticles is null");
        }

        if (sensorModel == null) {
            throw new RuntimeException("sensorModel is null");
        }

        if (navigationMap == null) {
            throw new RuntimeException("navigationMap is null");
        }

        for (final Particle p: oldParticles.getParticles()) {
            final double w =
                    sensorModel.probabilityOfMeasuredResultGivenExpectedValue(
                        navigationMap.getExpectedSensorValue(p),
                        sensorInput);

            if (p == null) {
                throw new RuntimeException("p is null");
            }
            p.setWeight(w);
            sumOfWeights += w;
        }

        if (oldParticles == null) {
            throw new RuntimeException("oldParticles is null");
        }

        oldParticles.normalizeWeights(sumOfWeights);


        ///
        /// Resampling phase
        ///


        oldParticles.sortThenCumulateWeights();

        // Resample (with replacement)
        // with probabilities of being basis
        // for resampling being based on normalized weights

         if (sensorInput == null) {
            throw new RuntimeException("sensorInput is null");
        }

        final PolarCoordinate speed = sensorInput.getSpeed();
        for (int i = 0; i < noOfParticles; i++) {
            final Particle startingPoint = oldParticles.pickInstanceAccordingToProbability();
            for (int j = 0; j < REPLACEMENT_FACTOR && i < noOfParticles; j++, i++) {
                final Particle destination = newParticles.get(j);
                if (destination == null) {
                    throw new RuntimeException("destination is null");
                }
                PositionEstimation.estimateNewParticle(destination, startingPoint, speed);
            }
        }
    }

    public void mainLoop() {
        while (getRunStatus()) {
            senseEstimate();

            // At this point newData contains the best guess at the
            // present position
            particleFieldConsumer.consumeParticles(newParticles);
        }
    }

    public WeightedPool<Particle> getNewParticles() {
        return newParticles;
    }

    public WeightedPool<Particle> getOldParticles() {
        return oldParticles;
    }

   
}
