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

import no.rmz.robotics.particlefilter.Particle;
import no.rmz.robotics.particlefilter.PositionEstimation;
import no.rmz.robotics.particlefilter.geometry.PolarCoordinate;
import static org.junit.Assert.assertEquals;
import org.junit.Test;


public final class PositionEstimationTest {

    private final static double DELTA = 0.000001;


    //
    //  First  a bunch of tests where the initial speed is
    //  zero, and then we get a sensed speed, and that is all that
    //  counts.
    //


    @Test
    public void zeroMovementTest() {
        final Particle destination = new Particle();
        final Particle origin = new Particle();
        final PolarCoordinate sensedSpeed = new PolarCoordinate(0, 0);

        PositionEstimation.estimateNewParticle(destination, origin, sensedSpeed);
        final double distance = destination.getPosition().distanceSquared(origin.getPosition());

        assertEquals("With no speed, expect no movement", 0, distance, DELTA);
    }


    @Test
    public void oneUnitNorthTest() {
        final Particle destination = new Particle();
        final Particle origin = new Particle();
        final PolarCoordinate sensedSpeed = new PolarCoordinate(Math.PI / 2, 1);

        PositionEstimation.estimateNewParticle(destination, origin, sensedSpeed);
        final double distance = destination.getPosition().distanceSquared(origin.getPosition());

        assertEquals("With no speed, expect no movement", 1, distance, DELTA);

        assertEquals("X coordinate should be zero", 0, destination.getPosition().getX(),  DELTA);
        assertEquals("Y coordinate should be one",  1, destination.getPosition().getY(),  DELTA);
    }


    @Test
    public void oneUnitSouthTest() {
        final Particle destination = new Particle();
        final Particle origin = new Particle();
        final PolarCoordinate sensedSpeed = new PolarCoordinate(Math.PI * 3 / 2, 1);

        PositionEstimation.estimateNewParticle(destination, origin, sensedSpeed);
        final double distance = destination.getPosition().distanceSquared(origin.getPosition());

        assertEquals("With no speed, expect no movement", 1, distance, DELTA);

        assertEquals("X coordinate should be zero",  0, destination.getPosition().getX(),  DELTA);
        assertEquals("Y coordinate should be one",  -1, destination.getPosition().getY(),  DELTA);
    }

    @Test
    public void oneUnitWestTest() {
        final Particle destination = new Particle();
        final Particle origin = new Particle();
        final PolarCoordinate sensedSpeed = new PolarCoordinate(Math.PI , 1);

        PositionEstimation.estimateNewParticle(destination, origin, sensedSpeed);
        final double distance = destination.getPosition().distanceSquared(origin.getPosition());

        assertEquals("With no speed, expect no movement", 1, distance, DELTA);

        assertEquals("X coordinate should be zero",-1, destination.getPosition().getX(),  DELTA);
        assertEquals("Y coordinate should be one",  0, destination.getPosition().getY(),  DELTA);
    }


    //
    // Now for something different, we add initial movement as a
    // condition.  This means that the sensed speed  will be combined with
    // the initial (nonzero) speed to produce an actual speed over the
    // terrain.
    //

    @Test
    public void oneUnitDeltaToNorthWithInitialWestSpeed() {
        final Particle destination = new Particle();
        final Particle origin = new Particle();
        final PolarCoordinate sensedSpeed = new PolarCoordinate(Math.PI , 1);
        final PolarCoordinate originalSpeed = origin.getSpeed();

        originalSpeed.setRadius(1);
        originalSpeed.setTheta(Math.PI / 2);

        PositionEstimation.estimateNewParticle(destination, origin, sensedSpeed);

        final double distance = destination.getPosition().distanceSquared(origin.getPosition());

        assertEquals("With no speed, expect no movement", 1, distance, DELTA);

        final double estimatedTheta  = Math.PI * 3/2;
        final double estimatedRadius = sensedSpeed.getRadius();
        final PolarCoordinate pc = new PolarCoordinate(estimatedTheta, estimatedRadius);

        assertEquals("X coordinate should be zero", pc.getXcoord(), destination.getPosition().getX(),  DELTA);
        assertEquals("Y coordinate should be one",  pc.getYcoord(), destination.getPosition().getY(),  DELTA);
    }


    //
    // Now assume that we're not starting at the origin, but
    // at (1,1), and that we have some sensed movement
    // that is different than zero
    // (the most complex case, which also happens to be the most
    //  common case).
    //
    @Test
    public void oneUnitDeltaToNorthWithInitialWestSpeedOriginAtOneOne() {


        // Set the initial state (position etc.)
        final Particle destination = new Particle();
        final Particle origin = new Particle();
        final PolarCoordinate originalSpeed = origin.getSpeed();


        // Location is (1,1)
        origin.getPosition().setX(1);
        origin.getPosition().setY(1);

        // Speed is 1, direction straight up.
        originalSpeed.setRadius(1);
        originalSpeed.setTheta(Math.PI / 2);

        // Then set the sensed speed, relative to the
        // vehicle speed to be  west magnitude 1.
        final PolarCoordinate sensedSpeed = new PolarCoordinate(Math.PI / 2 , 1);

        // and estimate the new position, which should be (0,1), since
        // (0,1) = (1,1) + (-1, 0)
        PositionEstimation.estimateNewParticle(destination, origin, sensedSpeed);

        // Extract the coordinates
        final double x = destination.getPosition().getX();
        final double y = destination.getPosition().getY();

        // and check that they are where the are suposed to be.
        assertEquals("X coordinate had unexpected value",  0.0, destination.getPosition().getX(),  DELTA);
        assertEquals("Y coordinate had unexpected value",  1.0, destination.getPosition().getY(),  DELTA);
    }
}
