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

import no.rmz.robotics.arrays.WeightedPool;
import org.junit.*;
import static org.junit.Assert.*;



public class WeightedPoolTest {
    
   
    
    private final WeightedPool<SimpleWeighted> emptyPool = 
            new WeightedPool<SimpleWeighted>(new SimpleWeighted[0]);
    
    private final WeightedPool<SimpleWeighted> oneElementPool = 
            new WeightedPool<SimpleWeighted>(new SimpleWeighted[1]);
    
    private final WeightedPool<SimpleWeighted> tenElementPool = 
            new WeightedPool<SimpleWeighted>(new SimpleWeighted[10]);
    
    
    
    private void initializePool(final WeightedPool<SimpleWeighted> pool) {
        for (int i = 0; i < pool.getSize() ; i++) {
            pool.put(i, new SimpleWeighted(i));
        }
    }
    
    private WeightedPool<SimpleWeighted>  pools [];
    
    
    @Before
    public void setUp() {
        initializePool(emptyPool);
        initializePool(oneElementPool);
        initializePool(tenElementPool);

        pools =  new no.rmz.robotics.arrays.WeightedPool[]{
            emptyPool , oneElementPool , tenElementPool
        };
        
        for (final WeightedPool<SimpleWeighted> p: pools) {
            initializePool(p);
        }
    }
    
    
    
    @Test
    public void testUnsort() {
       
    }

    @Test
    public void testGet() {
        for (final WeightedPool<SimpleWeighted> p : pools) {
            for (int i = 0; i < p.getSize(); i++) {
                assertEquals((double) i, p.get(i).getWeight(), 0.0000000001);
            }
        }
    }

    
    @Test
    public void testPut() {
        for (final WeightedPool<SimpleWeighted> p : pools) {
            for (int i = 0; i < p.getSize(); i++) {
               p.put(i, new SimpleWeighted(p.getSize()- i));
            }
        }
        
        for (final WeightedPool<SimpleWeighted> p : pools) {
            for (int i = 0; i < p.getSize(); i++) {
                assertEquals((double) (p.getSize()- i), p.get(i).getWeight(), 0.0000000001);
            }
        } 
    }
    
    
    @Test
    public void testBinarySearchForNumber(){
        
    } 
    
    @Test
    public void testPickInstanceAccordingToProbability() {
    }

    
    
    @Test
    public void testNormalizeWeights() {
    }

    @Test
    public void testSortThenCumulateWeights() {
    }
}
