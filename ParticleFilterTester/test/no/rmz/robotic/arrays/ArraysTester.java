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


import java.util.Random;
import no.rmz.robotics.arrays.Arrays;
import org.junit.*;
import static org.junit.Assert.*;

public class ArraysTester {

    private Random rnd = new Random();


    @Test
    public void sortEmptyArray() {
        final Integer[]  array = new Integer[0];
        Arrays.sort(array, ComparatorTester.INTEGER_COMPARATOR);
        isSorted(array);
    }

    @Test
    public void sortSingleElementArray() {
        final Integer[]  array = new Integer[1];
        array[0] = 1;
        Arrays.sort(array, ComparatorTester.INTEGER_COMPARATOR);
        isSorted(array);
    }

    @Test
    public void sortSortedArray() {
        final Integer[] array = new Integer[3];
        array[0] = 1;
        array[1] = 2;
        array[2] = 3;
        Arrays.sort(array, ComparatorTester.INTEGER_COMPARATOR);
        isSorted(array);
    }

    @Test
    public void sortReversedArray() {
        final Integer[] array = new Integer[3];
        array[0] = 3;
        array[1] = 2;
        array[2] = 1;
        Arrays.sort(array, ComparatorTester.INTEGER_COMPARATOR);
        isSorted(array);
    }


    @Test
    public void sortWithDupes() {
        final Integer[] array = new Integer[3];
        array[0] = 3;
        array[1] = 2;
        array[2] = 2;
        Arrays.sort(array, ComparatorTester.INTEGER_COMPARATOR);
        isSorted(array);
    }




    @Test
    public void testSemiBigRandomizedArray() {
        final int siz = 50;
        final Integer[] array = new Integer[50];
        for (int i = 0 ; i < siz; i++) {
            array[i] = rnd.nextInt();
        }
        Arrays.sort(array, ComparatorTester.INTEGER_COMPARATOR);
        isSorted(array);
    }

    /**
     * If the array isn't sorted according to  ComparatorTester.INTEGER_COMPARATOR
     * in a (non-strictly) increasing order, the isSorted method will fail
     * the test that calls it.
     * @param array
     */
    private void isSorted(final Integer[] array) {
        for (int i = 1 ; i < array.length - 1; i++) {
            final Integer a = array[i - 1];
            final Integer b = array[i];
            final int cmp = ComparatorTester.INTEGER_COMPARATOR.compare(a, b);
            assertTrue("expected that cmp <= 0", cmp <= 0);
        }
    }
}
