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
import no.rmz.robotics.arrays.Weighted;

/**
 * A pool that holds particles, and can do some things on them
 * to make sure they are sorted and easy to search, etc.
 */
public class WeightedPool <T extends Weighted>{
    
    private final Object monitor = new Object();
  
    /**
     * Randomness used to add noise to position estimates.
     */
    static final Random RANDOMNESS = new Random();
    
    /**
     * The comparator we use to compare particles where the weights
     * has been updated.
     */
    public static final ComparatorAccordingToWeight PARTICLE_COMPARATOR_ACCORDING_TO_WEIGHT = 
            new ComparatorAccordingToWeight();
    
    private final T[] particles;

    public WeightedPool(final T[] particles) {
        this.particles = particles;
    }

    public T get(final int i) {
        synchronized(monitor) {
            return particles[i];
        }
    }
    
    public void put(final int i, final T p) {
        synchronized (monitor) {
            isSorted = false;
            particles[i] = p;
        }
    }
   
    private boolean isSorted = false;
    
    
    T binarySearchForNumber(final double r) {
        synchronized (monitor) {
            int min = 0;
            int max = particles.length - 1;
            while (min < max) {
                final int center = min + (max - min);
                final T centerp = get(center);
                if (centerp.getWeight() <= r) {
                    min = center;
                } else {
                    max = center;
                }
            }
            return get(max);
        }
    }

    T pickParticleAccordingToProbability() {
        synchronized (monitor) {
            if (!isSorted) {
                sortThenCumulateWeights();
                isSorted = true;
            }

           
            final double r = RANDOMNESS.nextDouble();
            return binarySearchForNumber(r);
        }
    }

    private void sortAccordingToWeight() {
        Arrays.sort(particles, PARTICLE_COMPARATOR_ACCORDING_TO_WEIGHT);
    }
    
    
      
    public void normalizeWeights(final double sumOfWeights) {
        // Normalize weights
        for (int i = 0; i < particles.length; i++) {
            final Weighted p = particles[i];
            p.setWeight(p.getWeight() / sumOfWeights);
        }
    }

    void sortThenCumulateWeights() {
        synchronized (monitor) {
            sortAccordingToWeight();

            double cumulatedWeights = 0;
            for (int i = 0; i < particles.length; i++) {
                cumulatedWeights += get(i).getWeight();
                get(i).setWeight(cumulatedWeights);
            }
        }
    } 
}
