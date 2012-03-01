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

    /**
     * The delta allowed when comparing weights and sums of weights.
     */
    private final static double DELTA = 0.0000000001;

    /**
     * A source of randomness.
     */
    private final static Random RND = new Random();


    /**
     * When creating random numbers to put in pools, they will
     * be created in a range from zero up to and including this
     * number.
     */
    private final static int RANGE_TO_SELECT_RANDOM_NUMBERS_FROM = 10000;

    /**
     * When sampling, this is the number of samples we will
     * collect per item in a pool.
     */
    private final static int NO_OF_SAMPLES_PER_ITEM = 100000;

    /**
     * 1/100 == one percent
     */
    private final static double ONE_PERCENT = 0.01;


    private WeightedPool<SimpleWeighted> fourElementPool;

    /**
     * If you want more verbose output, fix this method.
     * @param s
     */
    private void log(final String s) {
        // System.out.println(s);
    }

    private void initializePoolWithWeightsEqualToIndexes(final WeightedPool<SimpleWeighted> pool) {
        assertTrue("Expected pool size greater than zero", pool.getSize() > 0);
        pool.put(0, new SimpleWeighted(DELTA / 1000 ));
        for (int i = 1; i < pool.getSize(); i++) {
            pool.put(i, new SimpleWeighted(i ));
        }
    }

    private void printPool(final WeightedPool<SimpleWeighted> pool) {
        for (int i = 0; i < pool.getSize(); i++) {
           log("pool(" + i + ") = " + pool.get(i).getWeight());
        }
    }



    @Before
    public void setUp() {
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
                assertEquals(1.0, p.getSumOfWeights(),  DELTA);
            }
        }
    }


    @Test
    public void testBinarySearchForNumber() {
        log("B: testBinarySearchForNumber");


        fourElementPool.normalizeWeights(fourElementPool.getSumOfWeights());
        fourElementPool.sortThenCumulateWeights();

        for (int i = 0; i < fourElementPool.getSize(); i++) {
            final SimpleWeighted item = fourElementPool.get(i);
            log("item " + i + " = " + item.getWeight());
        }

        assertEquals(fourElementPool.get(1), fourElementPool.binarySearchForNumber(0.15));
        assertNotSame(fourElementPool.get(1),fourElementPool.get(2));
        assertEquals(fourElementPool.get(2), fourElementPool.binarySearchForNumber(0.19));
        assertEquals(fourElementPool.get(3), fourElementPool.binarySearchForNumber(0.66));
        assertEquals(fourElementPool.get(3), fourElementPool.binarySearchForNumber(1.0));

        log("E: testBinarySearchForNumber");
    }


    // XXX Should or shouldn't I add tests here?  It may in fact be
    //     better to refactor and avoid the possibility/necessity of tests.
    @Test
    public void testSortThenCumulateWeights() {
    }

    @Test
    public void testUnsort() {
    }




    @Test
    public void testPickInstanceAccordingToProbability() {

        // The three-element pool is the smallest
        // one we can use for doing a (meaningful, non-corner case)
        // statistical test, so
        // we start using it and then perhaps make the test more generic
        // afterwards.


        for (final WeightedPool<SimpleWeighted> pool : pools) {

            log("Randomized test over pool named '" + pool.getName() +"'");

            final int noOfSamples = pool.getSize() * NO_OF_SAMPLES_PER_ITEM;
            final Double zero = 0.0;

            // The probabilities that a SimpleWeighted should be picked
            // by the pickInstanceAccordingToProbability method.
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

            // Used to count how many times the various SimpleWeighted
            // instances are picked by pickInstanceAccordingToProbability
            final Map<SimpleWeighted, Double> counters =
                    new HashMap<SimpleWeighted, Double>();

            // Sample (with replacement) and count.
            for (int i = 0; i < noOfSamples; i++) {
                final SimpleWeighted pick =
                        pool.pickInstanceAccordingToProbability();

                assertNotNull("Expect the pick to be non null", pick);
                final Double oldValue = (counters.containsKey(pick)) ? counters.get(pick) : zero;
                assertNotNull("Expect old value to be non-null for", oldValue);
                counters.put(pick, oldValue + 1);
            }

            // Normalize and compare counts with probabilities
            for (final SimpleWeighted w : counters.keySet()) {
                final double normalizedSampleProbability =
                        counters.get(w) / noOfSamples;

                // Compare to weights, the normalized counts should be
                // very similar to the weights of the key elements.
                // If we're within 1% we're ok.
                log("Expected = " + probabilities.get(w) + " normalized = " + normalizedSampleProbability);
                assertEquals(probabilities.get(w), normalizedSampleProbability, ONE_PERCENT);
            }
        }
    }
}
