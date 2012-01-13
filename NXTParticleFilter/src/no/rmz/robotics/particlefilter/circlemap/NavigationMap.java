package no.rmz.robotics.particlefilter.circlemap;

import no.rmz.robotics.particlefilter.Particle;
import no.rmz.robotics.sensors.SensorInput;

public interface NavigationMap {
    public SensorInput getExpectedSensorValue(Particle p);
}
