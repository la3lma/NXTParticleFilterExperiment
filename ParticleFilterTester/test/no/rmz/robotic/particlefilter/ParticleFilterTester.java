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


import no.rmz.robotics.arrays.WeightedPool;
import no.rmz.robotics.particlefilter.NavigationMap;
import no.rmz.robotics.particlefilter.Particle;
import no.rmz.robotics.particlefilter.ParticleFieldConsumer;
import no.rmz.robotics.particlefilter.ParticleFilter;
import no.rmz.robotics.particlefilter.geometry.PolarCoordinate;
import no.rmz.robotics.sensors.Sensor;
import no.rmz.robotics.sensors.SensorInput;
import no.rmz.robotics.sensors.SensorModel;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import static org.mockito.Mockito.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

// XXX Use mockito tester which probably isn't this.
@RunWith(MockitoJUnitRunner.class)
public class ParticleFilterTester {

    private final static int NO_OF_PARTICLES = 5;

    private ParticleFilter pf;
    private SensorInput    si;

    @Mock
    private Sensor sensor;

    @Mock
    private SensorModel sensorModel;

    @Mock
    private ParticleFieldConsumer particleFieldConsumer;

    @Mock
    private NavigationMap navigationMap;

    @Before
    public void setUp() {
        pf = new ParticleFilter(
                NO_OF_PARTICLES,
                sensor,
                sensorModel,
                particleFieldConsumer,
                navigationMap);
        si = new SensorInput((byte)125, new PolarCoordinate(0, 1.0));
        when(sensor.sense()).thenReturn(si);
    }


    @Test
    public void testSanityOfFilter(){
        final WeightedPool<Particle> newParticles = pf.getNewParticles();
        final WeightedPool<Particle> oldParticles = pf.getOldParticles();

        sanityCheckPool("newParticles", newParticles);
        sanityCheckPool("oldParticles", oldParticles);

    }


    @Test
    public void singleRoundOfSenseEstimate() {
        pf.senseEstimate();
    }

    private void sanityCheckPool(final String poolName, final WeightedPool<Particle> pool) {
       assertNotNull("poolName can't be null", poolName);
       assertNotNull("pool " + poolName + " can't be null", pool);

       assertTrue("size of pool can't be zero", pool.getSize() > 0);
       final Particle[] particles = pool.getParticles();
       assertTrue("size of particles array can't be zero", particles.length > 0);
       assertNotNull("First element in particles array can't be null", particles[0]);
    }

}
