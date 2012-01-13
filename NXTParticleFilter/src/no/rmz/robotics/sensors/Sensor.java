package no.rmz.robotics.sensors;

import no.rmz.robotics.particlefilter.XYPair;


public interface Sensor {
    public SensorInput sense();
}
