package net.mehvahdjukaar.heartstone;

import net.mehvahdjukaar.moonlight.api.platform.network.ChannelHandler;
import net.mehvahdjukaar.moonlight.api.platform.network.Message;
import net.mehvahdjukaar.moonlight.api.platform.network.NetworkDir;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public class NetworkHandler {

    public static final ChannelHandler CHANNEL = ChannelHandler.createChannel(Heartstone.res("network"));

    public static void registerMessages() {

        CHANNEL.register(NetworkDir.PLAY_TO_CLIENT,
                ClientBoundSpawnHeartstoneParticlePacket.class, ClientBoundSpawnHeartstoneParticlePacket::new);

    }

    public static void sendHeartstoneParticles(Player player, Player other) {
        Vec3 pos = player.getEyePosition();

        CHANNEL.sentToAllClientPlayersTrackingEntityAndSelf(player,
                new ClientBoundSpawnHeartstoneParticlePacket(pos, other.getEyePosition().subtract(pos),
                        player.getUUID(),  other.getUUID()));
    }

    public static class ClientBoundSpawnHeartstoneParticlePacket implements Message {
        public final Vec3 pos;
        public final Vec3 dist;
        public final UUID target;
        public final UUID from;

        public ClientBoundSpawnHeartstoneParticlePacket(FriendlyByteBuf buf) {
            this.pos = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
            this.dist = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
            this.from = buf.readUUID();
            this.target = buf.readUUID();
        }

        public ClientBoundSpawnHeartstoneParticlePacket(Vec3 pos, Vec3 dist, UUID from, UUID target) {
            this.pos = pos;
            this.dist = dist;
            this.from = from;
            this.target = target;
        }

        @Override
        public void writeToBuffer(FriendlyByteBuf buf) {
            buf.writeDouble(this.pos.x);
            buf.writeDouble(this.pos.y);
            buf.writeDouble(this.pos.z);
            buf.writeDouble(this.dist.x);
            buf.writeDouble(this.dist.y);
            buf.writeDouble(this.dist.z);
            buf.writeUUID(from);
            buf.writeUUID(target);
        }

        @Override
        public void handle(ChannelHandler.Context context) {
            HeartstoneClient.spawnParticle(this);
        }
    }
}