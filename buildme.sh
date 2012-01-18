#!/bin/sh
 (cd NXTParticleFilter/ && ant compile) && (cd ParticleFilterTester/ && ant test )

