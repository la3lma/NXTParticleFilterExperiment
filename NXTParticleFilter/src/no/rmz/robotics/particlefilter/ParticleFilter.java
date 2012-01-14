package no.rmz.robotics.particlefilter;

import no.rmz.robotics.particlefilter.circlemap.NavigationMap;
import no.rmz.robotics.sensors.SensorInput;
import no.rmz.robotics.sensors.Sensor;
import no.rmz.robotics.sensors.SensorModel;
import no.rmz.robotics.arrays.Arrays;
import java.util.Random;

/**
 * This class implements a particle filter localization estimation
 * algorithm.
 *
 * It has two sets of particles, old and new, both implemented as arrays
 * of particle.  The basic flow of the algorithm is:
 *
 * Start with  a set of particles randomly spread out over the
 * area where we suspect the vehicle is located in.
 *
 * Then, given the current sensor input, filter and resample, then
 * then add movement based on estimated speed (with Gaussian noise)
 * and repeat.
 *
 * XXX Refactor to niceness before continuing.
 *
 */
public final class ParticleFilter {

    private final static int NO_OF_PARTICLES = 50;
    
    private final int REPLACEMENT_FACTOR = 3;
    
    private final int PARTICLE_SPEED_ERROR = 5;
   
    private final Sensor sensor;
    private final SensorModel sensorModel;
    private final ParticleFieldConsumer particleFieldConsumer;
    private final NavigationMap navigationMap;
    
    
    private Particle[] oldParticles = new Particle[NO_OF_PARTICLES];
    private Particle[] newParticles = new Particle[NO_OF_PARTICLES];
   
    private boolean runStatus;
    
    final Random randomness = new Random();

    public ParticleFilter(
            Sensor sensor, 
            SensorModel sensorModel, 
            ParticleFieldConsumer particleFieldConsumer,
            NavigationMap navigationMap) {
        this.sensor = sensor;
        this.sensorModel = sensorModel;
        this.particleFieldConsumer = particleFieldConsumer;
        this.navigationMap = navigationMap;
    }
    
    
    // XXX This algorithm has not bee tested!!!
    private Particle pickParticleAccordingToProbability() {
      
        // Pick a random number in the interval [0, 1]
        
        final double r = randomness.nextDouble();
        
        // Then find the particle in the old-particle set tha
        // is currently assigned a "weight" that is equal or larger
        // than  r, but where the "weight" of the next entry in the
        // array is strictly smaller than the next entry.  For the
        // purposes of this algorithm we assume that the NO_OF_PARTICLES
        // entry has the weight 1. (XXX IS this an off by one error? It may
        // in fact be!)
        
        int min = 0;
        int max = NO_OF_PARTICLES  - 1;
        
        while (min < max) {
            final int center = min + (max - min);
            final Particle centerp = oldParticles[center];
            
            if (centerp.getWeight() <= r)  {
                min = center;
            } else {
                max = center;
            }
        }
        
        // At this point max == min == the number we want.
        
        return oldParticles[max];
    }

    /**
     * This represents a simple kinematic model of the
     * robot, and then applies movement as measured to the
     * assumed starting point and puts the new particle
     * (with speed and position) in the target particle.
     * 
     * @param target  The address of the new target particle
     * @param startingPoint The origi particle.
     * @param sensedSpeed Speed for the two wheels!
     */
    private void estimateNewParticle(
            final int target,
            final Particle startingPoint,
            final PolarCoordinate sensedSpeed) {
        
        
        // Get copies of the speed and location from
        // respectively the sensor and the starting point.
        final PolarCoordinate speed    = sensedSpeed.copy();
        final XYPair          location = startingPoint.getPosition().copy();
        
        // First perturb both the speed and the location,
        // then apply the movement to the location.  The time is
        // normalized to one, but that is something we need to look into.
        speed.perturb();
        location.perturb();
        
        // The speed from the sensor is in a coordinate system relative
        // to the robot, but we need map speed and direction, hence
        // we need to convert the coordinates, and we do that
        // using the previously known speed of the robot and then
        // perturbing that wrt the vehicle-relative speed.
        speed.convertToMapSpeed(startingPoint.getSpeed());
        location.move(speed);
        
        // Then finally set destination particle's speed
        // and location.
        final Particle destination =  newParticles[target];
        destination.setPosition(location);
        destination.setSpeed(speed);
    }

    private int getPositionError() {
        return randomness.nextInt(PARTICLE_SPEED_ERROR) - (PARTICLE_SPEED_ERROR/2);
    }
    
    
    public final static ParticleComparatorAccordingToWeight particleCompratorAccordingToWeight =
            new ParticleComparatorAccordingToWeight();

    /**
     * Initialize the particles using a two dimensional Gaussian
     * centered at x, y with a standard deviation of sdev.
     * @param x
     * @param y
     * @param sdev
     */
    public void initialize(byte x, byte y, byte sdev) {
        throw new RuntimeException("Not implemented");
    }

    private boolean getRunStatus() {
        return runStatus;
    }
    
    

    public void mainLoop() {
        while (getRunStatus()) {
            
            
            // Switch old and new data
            
            final Particle tmp[] = newParticles;
            newParticles = oldParticles;
            oldParticles = tmp;     

            ///
            /// Sensing phase
            ///

            final SensorInput sensorInput = sensor.sense();


            ///
            /// Estimating posterior probabilities wrt sensor input
            ///

            // We'll use this to normalize later
            double sumOfWeights = 0;

            // Calculate un-normalized weights.
            for (int i = 0; i < NO_OF_PARTICLES; i++) {
                final Particle p = oldParticles[i];
                final double w =
                        sensorModel.probabilityOfMeasuredResultGivenExpectedValue(
                            navigationMap.getExpectedSensorValue(p),
                            sensorInput);
                p.setWeight(w);
                sumOfWeights += w;
            }

            // Normalize weights
            for (int i = 0; i < NO_OF_PARTICLES; i++) {
                final Particle p = oldParticles[i];
                p.setWeight(p.getWeight() / sumOfWeights);
            }

            ///
            /// Resampling phase
            ///

            // Sort w.r.t. weights (hightest weights first)
            Arrays.sort(oldParticles, particleCompratorAccordingToWeight);
            
            // Then set weights to be the cumulated weights lower than
            // itself including itself.  This will allow us to use binary
            // searh when doing resampling
            
            double cumulatedWeights = 0;
            for (int i = 0; i < NO_OF_PARTICLES; i++) {
                cumulatedWeights += oldParticles[i].getWeight();
                oldParticles[i].setWeight(cumulatedWeights);
            }
           
            // Resample (with replacement)
            // with probabilities of being basis
            // for resampling being based on normalized weights
            
            final PolarCoordinate speed = sensorInput.getSpeed();
            for (int i = 0; i < NO_OF_PARTICLES; i++) {
                final Particle startingPoint = pickParticleAccordingToProbability();
                for (int j = 0; j < REPLACEMENT_FACTOR && i < NO_OF_PARTICLES; j++, i++) {
                    estimateNewParticle(i, startingPoint,  speed);
                }
            }
            
            // At this point newData contains the best guess at the
            // present position
            
            particleFieldConsumer.consume(newParticles);
        }
    }
}
