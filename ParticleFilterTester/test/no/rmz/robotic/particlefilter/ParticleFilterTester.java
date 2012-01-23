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


import no.rmz.robotics.particlefilter.NavigationMap;
import no.rmz.robotics.particlefilter.ParticleFieldConsumer;
import no.rmz.robotics.particlefilter.ParticleFilter;
import no.rmz.robotics.sensors.Sensor;
import no.rmz.robotics.sensors.SensorModel;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnit44Runner;

// XXX Use mockito tester which probably isn't this.
@RunWith(MockitoJUnit44Runner.class)
public class ParticleFilterTester {
    
    private final static int NO_OF_PARTICLES = 5;
    
    
    private ParticleFilter pf;
    
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
        // MockitoAnnotations.initMocks(this);
        
     
        
        pf = new ParticleFilter(
                NO_OF_PARTICLES,
                sensor,
                sensorModel,
                particleFieldConsumer,
                navigationMap);
    }
    
    @Test
    public void trallala() {
        
    }
    
  
}
