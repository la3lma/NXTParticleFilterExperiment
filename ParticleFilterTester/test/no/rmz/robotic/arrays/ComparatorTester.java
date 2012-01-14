/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
