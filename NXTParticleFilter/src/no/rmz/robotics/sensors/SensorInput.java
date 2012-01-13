package no.rmz.robotics.sensors;

// I'm ad-hocking this for brightness right now, that may or may

import no.rmz.robotics.particlefilter.XYPair;

// not be a good idea for the future, but we'll see when we start
// to include ultrasonics.

public class SensorInput {
    byte brightness;
    XYPair speed;

    public SensorInput(byte brightness, XYPair speed) {
        this.brightness = brightness;
        this.speed = speed;
    }

   

    public byte getBrightness() {
        return brightness;
    }
    
    public XYPair getSpeed(){
        return speed;
    }
}
