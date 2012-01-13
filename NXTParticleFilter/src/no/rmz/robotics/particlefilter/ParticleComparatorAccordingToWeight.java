package no.rmz.robotics.particlefilter;

import no.rmz.robotics.arrays.Comparator;


public final class ParticleComparatorAccordingToWeight implements Comparator<Particle> {

    private int sign(final double x) {
        if (x < 0) {
            return -1;
        } else if (x == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public int compare(final Particle t, final Particle t1) {
        return sign(t.weight - t1.weight);
    }
    
}
