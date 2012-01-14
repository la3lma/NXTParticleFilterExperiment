package no.rmz.robotics.sensors;

// I'm ad-hocking this for brightness right now, that may or may

import no.rmz.robotics.particlefilter.PolarCoordinate;

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
