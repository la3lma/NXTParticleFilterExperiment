package no.rmz.robotics.sensors;

public interface SensorModel {

    
    public double probabilityOfMeasuredResultGivenExpectedValue(
            final SensorInput expectedSensorInput, 
            final SensorInput actualSensorInput);
}
