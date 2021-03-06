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
package no.rmz.robotics.sensors;

// I'm ad-hocking this for brightness right now, that may or may

import no.rmz.robotics.particlefilter.geometry.PolarCoordinate;

// not be a good idea for the future, but we'll see when we start
// to include ultrasonics.

public class SensorInput {
    byte brightness;
    PolarCoordinate speed;

    public SensorInput(final byte brightness, final PolarCoordinate speed) {
        this.brightness = brightness;
        this.speed = speed;
    }   

    public byte getBrightness() {
        return brightness;
    }
    
    public PolarCoordinate getSpeed(){
        return speed;
    }
}
