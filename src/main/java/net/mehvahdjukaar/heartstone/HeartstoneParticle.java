package net.mehvahdjukaar.heartstone;

import net.mehvahdjukaar.owls.client.ClientSetup;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Heartstone.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class HeartstoneParticle extends TextureSheetParticle {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void registerParticles(ParticleFactoryRegisterEvent event) {
        ParticleEngine particleManager = Minecraft.getInstance().particleEngine;
        particleManager.register(Heartstone.HEARTSTONE_PARTICLE.get(), HeartstoneParticle.Factory::new);
        particleManager.register(Heartstone.HEARTSTONE_PARTICLE_EMITTER.get(), (a, b, c, d, e, f, g, h) -> new Emitter(b, c, d, e, f, g, h));
    }

    public static void spawnParticle(NetworkHandler.ClientBoundSpawnHeartstoneParticlePacket message) {
        Minecraft.getInstance().level.addParticle(Heartstone.HEARTSTONE_PARTICLE_EMITTER.get(), message.pos.x, message.pos.y, message.pos.z,
                message.dist.x, message.dist.y, message.dist.z);
    }

    private HeartstoneParticle(ClientLevel world, double x, double y, double z, double motionX, double motionY, double motionZ, SpriteSet sprites) {
        super(world, x, y, z);
        this.pickSprite(sprites);
        this.xd = motionX;
        this.yd = motionY;
        this.zd = motionZ;
        this.setSize(0.01F, 0.01F);
        this.quadSize = 0.5f;
        this.gravity = 0;
        this.hasPhysics = false;
        this.lifetime = 20;
        this.setColor(1, 1, 1);
    }

    @Override
    public void move(double pX, double pY, double pZ) {
        this.setBoundingBox(this.getBoundingBox().move(pX, pY, pZ));
        this.setLocationFromBoundingbox();
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        float x = this.age / (float) this.lifetime;

        this.alpha = 1 - (x * x);
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.move(this.xd, this.yd, this.zd);
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Emitter extends NoRenderParticle {
        private final int type;

        private Emitter(ClientLevel world, double x, double y, double z, double dx, double dy, double dz) {
            super(world, x, y, z, 0.0D, 0.0D, 0.0D);
            Vec3 v = new Vec3(dx, dy, dz);
            double dist = v.lengthSqr();
            if (dist >= (10000 * 10000)) type = 2;
            else if (dist >= 1000 * 1000) type = 1;
            else type = 0;
            v = v.normalize();
            this.xd = v.x;
            this.yd = v.y;
            this.zd = v.z;
            world.playSound(Minecraft.getInstance().player, Minecraft.getInstance().player,
                    Heartstone.HEARTSTONE_SOUND.get(),
                    Minecraft.getInstance().player.getSoundSource(), 1.0F, 1.25F-type*0.25f);
        }

        @Override
        public void tick() {
            ++this.age;
            this.level.addParticle(Heartstone.HEARTSTONE_PARTICLE.get(),
                    x + age * 1.2 * xd,
                    y + age * 1.2 * yd,
                    z + age * 1.2 * zd,
                    type, 0.0D, 0.0D);


            if (this.age >= 15) {
                this.remove();
            }
        }
    }


    public static class Factory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public Factory(SpriteSet spriteSet) {
            this.sprite = spriteSet;
        }

        @Override
        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double index, double a, double b) {
            var particle = new HeartstoneParticle(worldIn, x, y, z, 0, 0, 0, sprite);
            int i = (int) Mth.clamp(index, 0, 2);
            particle.setSprite(sprite.get(i, 2));
            return particle;
        }
    }

}