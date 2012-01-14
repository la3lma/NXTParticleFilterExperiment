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

package no.rmz.robotics.arrays;

/**

 * This is a helper class inspired by the Arrays helper
 * class in java...  
 * 
 * It provides a sorting method for arrays.  It is currently 
 * implented using quicksort.
 */
public final class Arrays {

    
    
    /**
     * An impllementation of quicksort
     * @param <T> The type of the values in the array that is being sorted
     * @param values  An array of T values
     * @param comparator A comparator of T elements
     * @param low The lowest element in the array that will be sorted in
     *        the current invocation.
     * @param high The highest element that will be sorted by the
     *        current invocation.
     */
    private  static <T> void quicksort(
            final T[] values,
            final Comparator<T> comparator,
            final int low,
            final int high) {

        int i = low, j = high;
        
        // Get the pivot element from the middle of the list
        T pivot = values[low + (high - low) / 2];

        // Divide into two lists
        while (i <= j) {
            // If the current value from the left list is smaller then the pivot
            // element then get the next element from the left list
            while (comparator.compare(values[i], pivot) < 0) {
                i++;
            }
            // If the current value from the right list is larger then the pivot
            // element then get the next element from the right list
            while (comparator.compare(values[j], pivot) > 0) {
                j--;
            }

            // If we have found a values in the left list which is larger then
            // the pivot element and if we have found a value in the right list
            // which is smaller then the pivot element then we exchange the
            // values.
            // As we are done we can increase i and j
            if (i <= j) {
                exchange(values, i, j);
                i++;
                j--;
            }
        }
        // Recursion
        if (low < j) {
            quicksort(values, comparator, low, j);
        }
        if (i < high) {
            quicksort(values, comparator, i, high);
        }
    }

    /**
     * Swap two value i and j in the values array.
     * @param <T> Type of the elements in the array
     * @param values The array where the exchange takes place
     * @param i
     * @param j 
     */
    private static <T> void exchange(final T[] values, final int i, final int j) {
        final T temp = values[i];
        values[i] = values[j];
        values[j] = temp;
    }

    /**
     * Sort an array using a comprator.
     * @param <T> The type of the elements in the array
     * @param values The array
     * @param comparator  A comparator for elements in the array.
     */
    public static <T> void sort(
            final T[] values,
            final Comparator<T> comparator) {

        // Check for empty or null array
        if (values == null || values.length == 0) {
            return;
        }

        quicksort(values, comparator, 0, values.length - 1);
    }
}
