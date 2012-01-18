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
package no.rmz.robotics.arrays;

import java.util.Random;
import no.rmz.robotics.particlefilter.ComparatorAccordingToWeight;

/**
 * A pool that holds particles, and can do some things on them to make sure they
 * are sorted and easy to search, etc.
 */
public class WeightedPool<T extends Weighted> {

    private final Object monitor = new Object();
    /**
     * Randomness used to add noise to position estimates.
     */
    static final Random RANDOMNESS = new Random();
    /**
     * The comparator we use to compare particles where the weights has been
     * updated.
     */
    public static final ComparatorAccordingToWeight PARTICLE_COMPARATOR_ACCORDING_TO_WEIGHT =
            new ComparatorAccordingToWeight();
    
    /**
     * The objects we get in an array when creating an instance
     * of WeightedPool.
     */
    private final T[] objects;
    
    /**
     * True iff the content is known to be sorted, which it only is
     * after it has been sorted.
     */
    private boolean sorted = false;
    
    /**
     * The name of the pool.
     */
    private final String name;

    /**
     * Create a new instance.  The objects array may be of any size, but
     * it is assumed not to be shared with anything else.  It is not
     * created by the WeightedPool itself because I don't know how to
     * make Java create a new generic array, on an NXT brick ;-)
     * 
     * @param objects 
     */
    public WeightedPool(final String name, final T[] objects) {
        this.name = name;
        this.objects = objects;
    }

    /**
     * Mark the pool as unsorted.
     */
    public void unsort() {
        synchronized (monitor) {
            sorted = false;
        }
    }
    
    
    /**
     * Return true iff the pool is sorted.
     *
     * @return
     */
    public boolean isSorted() {
        synchronized (monitor) {
            return sorted;
        }
    }
    
    public int getSize() {
        return objects.length;
    }

    public String getName() {
        return name;
    }
    
    
    /**
     * Get object with index 'i' in the internal ordering.
     * @param i
     * @return 
     */
    public T get(final int i) {
        synchronized (monitor) {
            return objects[i];
        }
    }

    /**
     * Put an object at index 'i' in the internal ordering.   Will
     * mark the pool as unsorted.
     * 
     * @param i
     * @param p 
     */
    public void put(final int i, final T p) {
        synchronized (monitor) {
            unsort();
            objects[i] = p;
        }
    }

    T binarySearchForNumber(final double r) {
        synchronized (monitor) {
            int min = 0;
            int max = objects.length - 1;
            
            while (min + 1  < max) {
                
                final int center = min + (max - min)/2;
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

    /**
     * Interpret the weights as cumulative probabilities, then
     * pick an instance according to its (cumulative) probability
     * weight.  (XXX that's a bit unclear, it should be less unclear :-)
     * @return 
     */
    public T pickInstanceAccordingToProbability() {
        synchronized (monitor) {
            if (!sorted) {
                sortThenCumulateWeights();
            }

            final double r = RANDOMNESS.nextDouble();
            return binarySearchForNumber(r);
        }
    }

    private void sortAccordingToWeight() {
        Arrays.sort(objects, PARTICLE_COMPARATOR_ACCORDING_TO_WEIGHT);
    }

    public void normalizeWeights(final double sumOfWeights) {
        if (sumOfWeights <=0 ) {
            throw new IllegalArgumentException("Can't normalize with non-positive number: " + sumOfWeights);
        }
        for (int i = 0; i < objects.length; i++) {
            final Weighted p = objects[i];
            p.setWeight(p.getWeight() / sumOfWeights);
        }
    }
    
    public double getSumOfWeights() {
        synchronized (monitor) {
            
            double sum = 0;
            for (int i = 0; i < objects.length; i++) {
                sum += get(i).getWeight();
            }
            return sum;
        } 
    }

    public void sortThenCumulateWeights() {
        synchronized (monitor) {
            sortAccordingToWeight();

            double cumulatedWeights = 0;
            for (int i = 0; i < objects.length; i++) {
                cumulatedWeights += get(i).getWeight();
                get(i).setWeight(cumulatedWeights);
            }
            sorted = true;
        }
    }
}
