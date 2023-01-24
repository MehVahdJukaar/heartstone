package net.mehvahdjukaar.heartstone;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.mehvahdjukaar.moonlight.api.platform.ClientPlatformHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.player.Player;

public class HeartstoneClient {

    public static void init() {
        ClientPlatformHelper.addParticleRegistration(HeartstoneClient::registerParticles);
    }

    private static void registerParticles(ClientPlatformHelper.ParticleEvent event) {
        event.register(Heartstone.HEARTSTONE_PARTICLE.get(), HeartstoneParticle.Factory::new);
        event.register(Heartstone.HEARTSTONE_PARTICLE_EMITTER.get(), HeartstoneParticle.EmitterFactory::new);
    }

    public static void setup() {
    }

    @ExpectPlatform
    public static void highlightPlayer(Player player) {
        throw new AssertionError();
    }


    public static void spawnParticle(NetworkHandler.ClientBoundSpawnHeartstoneParticlePacket message) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) return;
        int d = Heartstone.highlightDistance.get();
        if (message.target != null && Heartstone.highlight.get() && message.dist.lengthSqr() < d*d) {
            highlightPlayer(level.getPlayerByUUID(message.target));
        }
        level.addParticle(Heartstone.HEARTSTONE_PARTICLE_EMITTER.get(), message.pos.x, message.pos.y, message.pos.z,
                message.dist.x, message.dist.y, message.dist.z);
    }
}
