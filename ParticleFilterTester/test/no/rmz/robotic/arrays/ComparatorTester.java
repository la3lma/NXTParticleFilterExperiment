/**
 *  Copyright 2012 Bjørn Remseth
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package no.rmz.robotic.arrays;

import no.rmz.robotics.arrays.Comparator;
import org.junit.*;


public class ComparatorTester {
 
    Comparator<Integer> comparator;
    Integer i1 = 1;
    Integer i2 = 2;
    @Before
    public void setUp() {
        comparator = new Comparator<Integer>() {

            @Override
            public int compare(final Integer t, final Integer t1) {
               return t.compareTo(t1);
            }
        };
    }
    
  
    @Test
    public void hello() {
        org.junit.Assert.assertTrue("Expected comparison to return less than zero",
                comparator.compare(i1, i2) < 0);
    }
}
