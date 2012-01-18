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
 * A comparator.
 * @param <T>
 */
public interface Comparator<T> {
    /**
     * Return -1 if t < t1, +1 if t1 > t2, 0 if they are equal.
     * @param t
     * @param t1
     * @return
     */
     public int compare(final T t, final T t1);
}
