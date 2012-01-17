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

package no.rmz.robotics.particlefilter;

import no.rmz.robotics.arrays.Comparator;
import no.rmz.robotics.arrays.Weighted;


public final class ComparatorAccordingToWeight implements Comparator<Weighted> {

    private int sign(final double x) {
        if (x < 0) {
            return -1;
        } else if (x == 0) {
            return 0;
        } else {
            return 1;
        }
    }
  
    @Override
    public int compare(final Weighted t, final Weighted t1) {
        return sign(t.getWeight() - t1.getWeight());
    }
}
