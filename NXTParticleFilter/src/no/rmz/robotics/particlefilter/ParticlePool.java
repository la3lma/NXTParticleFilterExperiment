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

import java.util.Random;
import no.rmz.robotics.arrays.Arrays;

/**
 * A pool that holds particles, and can do some things on them
 * to make sure they are sorted and easy to search, etc.
 */
public class ParticlePool {
    /**
     * Randomness used to add noise to position estimates.
     */
    /**
     * Randomness used to add noise to position estimates.
     */
    static final Random RANDOMNESS = new Random();
    /**
     * The comparator we use to compare particles where the weights
     * has been updated.
     */
    /**
     * The comparator we use to compare particles where the weights
     * has been updated.
     */
    public static final ParticleComparatorAccordingToWeight PARTICLE_COMPARATOR_ACCORDING_TO_WEIGHT = new ParticleComparatorAccordingToWeight();
    private Particle[] particles;

    public ParticlePool(final int noOfParticles) {
        this.particles = new Particle[noOfParticles];
    }

    public Particle get(final int i) {
        return particles[i];
    }

    Particle pickParticleAccordingToProbability() {
        // Pick a random number in the interval [0, 1]
        // Pick a random number in the interval [0, 1]
        final double r = RANDOMNESS.nextDouble();
        // Then find the particle in the old-particle set tha
        // is currently assigned a "weight" that is equal or larger
        // than  r, but where the "weight" of the next entry in the
        // array is strictly smaller than the next entry.  For the
        // purposes of this algorithm we assume that the NO_OF_PARTICLES
        // entry has the weight 1. (XXX IS this an off by one error? It may
        // in fact be!)
        // Then find the particle in the old-particle set tha
        // is currently assigned a "weight" that is equal or larger
        // than  r, but where the "weight" of the next entry in the
        // array is strictly smaller than the next entry.  For the
        // purposes of this algorithm we assume that the NO_OF_PARTICLES
        // entry has the weight 1. (XXX IS this an off by one error? It may
        // in fact be!)
        int min = 0;
        int max = particles.length - 1;
        while (min < max) {
            final int center = min + (max - min);
            final Particle centerp = get(center);
            if (centerp.getWeight() <= r) {
                min = center;
            } else {
                max = center;
            }
        }
        // At this point max == min == the number we want.
        // At this point max == min == the number we want.
        return get(max);
    }

    private void sortAccordingToWeight() {
        Arrays.sort(particles, PARTICLE_COMPARATOR_ACCORDING_TO_WEIGHT);
    }

    void sortThenCumulateWeights() {
        // Sort w.r.t. weights (hightest weights first)
        // Sort w.r.t. weights (hightest weights first)
        sortAccordingToWeight();
        // Then set weights to be the cumulated weights lower than
        // itself including itself.  This will allow us to use binary
        // searh when doing resampling
        // Then set weights to be the cumulated weights lower than
        // itself including itself.  This will allow us to use binary
        // searh when doing resampling
        double cumulatedWeights = 0;
        for (int i = 0; i < particles.length; i++) {
            cumulatedWeights += get(i).getWeight();
            get(i).setWeight(cumulatedWeights);
        }
    }
    
}
