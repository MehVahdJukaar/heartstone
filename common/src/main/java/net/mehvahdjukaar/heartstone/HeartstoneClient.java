package net.mehvahdjukaar.heartstone;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.mehvahdjukaar.moonlight.api.platform.ClientPlatformHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

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
        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        Level level = player.level;
        int d = Heartstone.highlightDistance.get();
        if (Heartstone.highlight.get() && message.from.equals(player.getUUID()) && message.dist.lengthSqr() < d * d) {
            highlightPlayer(level.getPlayerByUUID(message.target));
        }
        level.addParticle(Heartstone.HEARTSTONE_PARTICLE_EMITTER.get(), message.pos.x, message.pos.y, message.pos.z,
                message.dist.x, message.dist.y, message.dist.z);
    }
}
