package net.mehvahdjukaar.heartstone;

import net.mehvahdjukaar.moonlight.api.platform.ClientHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.lang.ref.WeakReference;

public class HeartstoneClient {


    private static WeakReference<Player> glowPlayer;
    private static int cooldown;

    public static void init() {
        ClientHelper.addParticleRegistration(HeartstoneClient::registerParticles);
    }

    private static void registerParticles(ClientHelper.ParticleEvent event) {
        event.register(Heartstone.HEARTSTONE_PARTICLE.get(), HeartstoneParticle.Factory::new);
        event.register(Heartstone.HEARTSTONE_PARTICLE_EMITTER.get(), HeartstoneParticle.EmitterFactory::new);
    }


    public static void spawnParticle(NetworkHandler.ClientBoundSpawnHeartstoneParticlePacket message) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        Level level = player.level();
        int d = Heartstone.HIGHLIGHT_DISTANCE.get();
        if (Heartstone.HIGHLIGHT.get() && message.from.equals(player.getUUID()) && message.dist.lengthSqr() < d * d) {
            highlightPlayer(level.getPlayerByUUID(message.target));
        }
        level.addParticle(Heartstone.HEARTSTONE_PARTICLE_EMITTER.get(), message.pos.x, message.pos.y, message.pos.z,
                message.dist.x, message.dist.y, message.dist.z);
    }

    public static void onClientTick() {
        if (cooldown != 0) {
            cooldown--;
            if (cooldown == 0) glowPlayer = null;
        }
    }

    public static void highlightPlayer(Player player) {
        glowPlayer = new WeakReference<>(player);
        cooldown = Heartstone.HIGHLIGHT_DURATION.get();
    }

    public static boolean isPlayerHighlighted(Entity player) {
        if (glowPlayer != null) return glowPlayer.get() == player;
        return false;
    }
}
