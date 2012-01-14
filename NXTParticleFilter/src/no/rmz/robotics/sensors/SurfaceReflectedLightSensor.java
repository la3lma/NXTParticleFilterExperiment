
package no.rmz.robotics.sensors;

import no.rmz.robotics.particlefilter.PolarCoordinate;

/**
 * Use the reflected light sensor to detect
 * surface reflectivity.  The idea being that a black surface
 * reflects less than a light one.  This sensor class includes
 * its own calibration, so the priorProbability... method
 * needs to reflect actual measurements and codify them as
 * a double between zero and one
 */
public class SurfaceReflectedLightSensor implements Sensor, SensorModel {

    
    @Override
    public SensorInput sense() {
        return new SensorInput(
                (byte) 0,
                new PolarCoordinate(0, 50)); // XXX Wild guesstimates
    }
    
    // XXX This is just a wild guesstimate.  For real number, use
    //     actual calibration data.
    @Override
    public double probabilityOfMeasuredResultGivenExpectedValue(
            final SensorInput expectedSensorInput, 
            final SensorInput actualSensorInput) {
        int d = (expectedSensorInput.getBrightness() - actualSensorInput.getBrightness());
        if (d < 0) {
            d = -d; // Absolute value
        }
        double result = 1 - (d/255);  // 1 minus normalized difference between expected and measured
        return result;  
    } 
}
