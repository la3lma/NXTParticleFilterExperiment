package no.rmz.robotics.particlefilter;


public interface ParticleFieldConsumer {

    void consumeParticles(Particle[] particles);

    void consume(Particle[] newParticles); 
}
