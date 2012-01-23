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

package no.rmz.robotic.particlefilter;

import no.rmz.robotics.particlefilter.Particle;
import no.rmz.robotics.particlefilter.PositionEstimation;
import no.rmz.robotics.particlefilter.geometry.PolarCoordinate;
import org.junit.*;
import static org.junit.Assert.*;


public class PositionEstimationTest {
    
    private final static double DELTA = 0.000001;
  
   
    @Test
    public void estimateNewParticleTest() {
        final Particle destination = new Particle();
        final Particle origin = new Particle();
        final PolarCoordinate sensedSpeed = new PolarCoordinate(0, 0);

        PositionEstimation.estimateNewParticle(destination, origin, sensedSpeed);
        final double distance = destination.getPosition().distanceSquared(origin.getPosition());
        
        assertEquals("With no speed, expect no movement", 0, distance, DELTA);
    }
}
