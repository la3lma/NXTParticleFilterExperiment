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
package no.rmz.robotic.arrays;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import no.rmz.robotics.arrays.WeightedPool;
import org.junit.*;
import static org.junit.Assert.*;

public class WeightedPoolTest {

    private final Collection<WeightedPool<SimpleWeighted>> pools =
            new ArrayList<WeightedPool<SimpleWeighted>>();
    private final static double DELTA = 0.0000000001;
    private final static Random RND = new Random();
    private final static int RANGE_TO_SELECT_RANDOM_NUMBERS_FROM = 10000;
    private final static int NO_OF_SAMPLES_PER_ITEM = 100000;


    private WeightedPool<SimpleWeighted> fourElementPool;


    private void initializePoolWithWeightsEqualToIndexes(final WeightedPool<SimpleWeighted> pool) {
        for (int i = 0; i < pool.getSize(); i++) {
            pool.put(i, new SimpleWeighted(i));
        }
    }

    private void printPool(final WeightedPool<SimpleWeighted> pool) {
        for (int i = 0; i < pool.getSize(); i++) {
            System.out.println("pool(" + i + ") = " + pool.get(i).getWeight());
        }
    }



    @Before
    public void setUp() {
        pools.add(new WeightedPool<SimpleWeighted>("empty pool", new SimpleWeighted[0]));
        pools.add(new WeightedPool<SimpleWeighted>("one element pool", new SimpleWeighted[1]));
        pools.add(new WeightedPool<SimpleWeighted>("three element pool", new SimpleWeighted[3]));
        fourElementPool = new WeightedPool<SimpleWeighted>("four element pool", new SimpleWeighted[4]);
        pools.add(fourElementPool);
        pools.add(new WeightedPool<SimpleWeighted>("five element pool", new SimpleWeighted[5]));
        pools.add(new WeightedPool<SimpleWeighted>("ten element pool", new SimpleWeighted[10]));

         for (final WeightedPool<SimpleWeighted> p : pools) {
            initializePoolWithWeightsEqualToIndexes(p);
        }

        final WeightedPool<SimpleWeighted> rndpool =
                new WeightedPool<SimpleWeighted>("hundred  element randomized pool",
                new SimpleWeighted[100]);
        pools.add(rndpool);

        // Add a bunch of randomly distributed, possibly equal numbers.
        for (int i = 0 ; i < rndpool.getSize() ; i++) {
            rndpool.put(i, new SimpleWeighted(RND.nextDouble() * RANGE_TO_SELECT_RANDOM_NUMBERS_FROM));
        }
    }

    @Test
    public void testGet() {
        for (int i = 0; i < fourElementPool.getSize(); i++) {
            assertEquals((double) i, fourElementPool.get(i).getWeight(), DELTA);
        }
    }

    @Test
    public void testPut() {
        for (final WeightedPool<SimpleWeighted> p : pools) {
            for (int i = 0; i < p.getSize(); i++) {
                p.put(i, new SimpleWeighted(p.getSize() - i));
            }
        }

        for (final WeightedPool<SimpleWeighted> p : pools) {
            for (int i = 0; i < p.getSize(); i++) {
                assertEquals((double) (p.getSize() - i), p.get(i).getWeight(), DELTA);
            }
        }
    }

    @Test
    public void testNormalizeWeights() {

        for (final WeightedPool<SimpleWeighted> p : pools) {
            final double sum = p.getSumOfWeights();
            if (sum > 0) {
                p.normalizeWeights(sum);
                assertEquals(p.getSumOfWeights(), 1.0, DELTA);
            }
        }
    }

    // XXX   Perhaps the picker is working the wrong direction (from
    //       top to bottom instead of bottom to top).
    @Test
    public void testBinarySearchForNumber() {
        System.out.println("B: testBinarySearchForNumber");

        // XXX This is clumsy, it shouldn't have to be like this.
        fourElementPool.normalizeWeights(fourElementPool.getSumOfWeights());
        fourElementPool.sortThenCumulateWeights();

        for (int i = 0; i < fourElementPool.getSize() ; i++) {
            final SimpleWeighted item = fourElementPool.get(i);
            System.out.println("item " + i + " = " + item.getWeight());
        }

        assertEquals(fourElementPool.get(1), fourElementPool.binarySearchForNumber(0.15));
        assertNotSame(fourElementPool.get(1),fourElementPool.get(2));
        assertEquals(fourElementPool.get(2), fourElementPool.binarySearchForNumber(0.19));
        assertEquals(fourElementPool.get(3), fourElementPool.binarySearchForNumber(0.66));
        assertEquals(fourElementPool.get(3), fourElementPool.binarySearchForNumber(1.0));

        System.out.println("E: testBinarySearchForNumber");
    }

    @Test
    public void testSortThenCumulateWeights() {
    }

    @Test
    public void testUnsort() {
    }


    @Test
    public void testPickInstanceAccordingToProbability () {

        // The three-element pool is the smallest
        // one we can use for doing a (meaningful, non-corner case)
        // statistical test, so
        // we start using it and then perhaps make the test more generic
        // afterwards.


        for (final WeightedPool<SimpleWeighted> pool : pools) {

            System.out.println("Randomized test over pool named '" + pool.getName() +"'");

            final int noOfSamples = pool.getSize() * NO_OF_SAMPLES_PER_ITEM;
            final Double zero = 0.0;

            final Map<SimpleWeighted, Double> counters =
                    new HashMap<SimpleWeighted, Double>();
            
            final Map<SimpleWeighted, Double> probabilities =
                    new HashMap<SimpleWeighted, Double>();
            
             final double sumOfWeights = pool.getSumOfWeights();
             
             if (sumOfWeights < DELTA) {
                continue;
            }
             
            pool.normalizeWeights(sumOfWeights);
            for (int i = 0 ; i < pool.getSize() ; i++) {
                probabilities.put(pool.get(i), pool.get(i).getWeight());
            }
            

            pool.sortThenCumulateWeights();
            printPool(pool);



            assertNotNull("Expect counters to be non-null", counters);

            // Sample (with replacement)
            for (int i = 0; i < noOfSamples; i++) {
                final SimpleWeighted pick =
                        pool.pickInstanceAccordingToProbability();

                assertNotNull("Expect the pick to be non null", pick);
                final Double oldValue = (counters.containsKey(pick)) ? counters.get(pick) : zero;
                assertNotNull("Expect old value to be non-null for", oldValue);
                counters.put(pick, oldValue + 1);
            }


            // Normalize and compare counts
            for (final SimpleWeighted w : counters.keySet()) {
                final double normalizedSample = counters.get(w) / noOfSamples;

                // Compare to weights, the normalized counts should be
                // very similar to the weights of the key elements.
                // If we're within 1% we're ok.
                System.out.println("Expected = " + w.getWeight() + " normalized = " + normalizedSample);
                assertEquals(probabilities.get(w), normalizedSample, 0.01);
                System.out.println("trallala " + w);
            }
        }
    }
}
