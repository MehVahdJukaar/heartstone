package net.mehvahdjukaar.heartstone.forge;

import net.minecraft.client.particle.FireworkParticles;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;

public class HeartstoneFirework {

    public static RandomSource rand = RandomSource.create();
    public static void createParticles(FireworkParticles.Starter starter, boolean trail,
                              boolean flicker, int[] colors, int[] fadeColors) {

        var array =  new double[][]
                {
                        {0.0000, 0.2500}, {0.0053, 0.2806}, {0.0395, 0.3563}, {0.1182, 0.4370}, {0.2361, 0.4795}, {0.3678, 0.4585}, {0.4771, 0.3749}, {0.5310, 0.2507}, {0.5125, 0.1134}, {0.4276, -0.0184}, {0.3024, -0.1391}, {0.1735, -0.2503}, {0.0731, -0.3509}, {0.0174, -0.4315}, {0.0007, -0.4773}, {-0.0007, -0.4773}, {-0.0174, -0.4315}, {-0.0731, -0.3509}, {-0.1735, -0.2503}, {-0.3024, -0.1391}, {-0.4276, -0.0184}, {-0.5125, 0.1134}, {-0.5310, 0.2507}, {-0.4771, 0.3749}, {-0.3678, 0.4585}, {-0.2361, 0.4795}, {-0.1182, 0.4370}, {-0.0395, 0.3563}, {-0.0053, 0.2806}, {-0.0000, 0.2500}
                };
        createExactShape  (starter, 0.5, array, colors, fadeColors,
                trail, flicker, true);
    }


    private static void createExactShape(FireworkParticles.Starter starter,
                                 double speed, double[][] shape, int[] colours, int[] fadeColours, boolean trail, boolean twinkle, boolean creeper) {
        double shapeX = shape[0][0];
        double shapeY = shape[0][1];
        Vec3 pos = starter.getPos();
        starter.createParticle(pos.x, pos.y, pos.z, shapeX * speed, shapeY * speed, 0.0, colours, fadeColours, trail, twinkle);
        float randAngle = rand.nextFloat() * 3.1415927F;
        double spreadAngle = creeper ? 0.034 : 0.34;

        for(int i = 0; i < 3; ++i) {
            double angle = randAngle + (i * 3.1415927F) * spreadAngle;
            double d4 = shapeX;
            double d5 = shapeY;

            for(int j = 1; j < shape.length; ++j) {
                double d6 = shape[j][0];
                double d7 = shape[j][1];

                float segPerLineOff = 1;
                for(double d8 = segPerLineOff; d8 <= 1.0; d8 += segPerLineOff) {
                    double d9 = Mth.lerp(d8, d4, d6) * speed;
                    double d10 = Mth.lerp(d8, d5, d7) * speed;
                    double d11 = d9 * Math.sin(angle);
                    d9 *= Math.cos(angle);

                    for(double d12 = -1.0; d12 <= 1.0; d12 += 2.0) {
                        starter.createParticle(pos.x, pos.y, pos.z, d9 * d12, d10, d11 * d12, colours, fadeColours, trail, twinkle);
                    }
                }

                d4 = d6;
                d5 = d7;
            }
        }

    }
}
