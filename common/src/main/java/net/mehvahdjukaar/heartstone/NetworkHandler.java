package net.mehvahdjukaar.heartstone;

import net.mehvahdjukaar.moonlight.api.platform.network.Message;
import net.mehvahdjukaar.moonlight.api.platform.network.NetworkHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public class NetworkHandler {

    public static void init() {
        NetworkHelper.addNetworkRegistration(NetworkHandler::registerMessages, 1);
    }

    private static void registerMessages(NetworkHelper.RegisterMessagesEvent event) {
        event.registerClientBound(ClientBoundSpawnHeartstoneParticlePacket.CODEC);
    }

    public static void sendHeartstoneParticles(Player player, Player other) {
        Vec3 pos = player.getEyePosition();

        NetworkHelper.sendToAllClientPlayersTrackingEntityAndSelf(player,
                new ClientBoundSpawnHeartstoneParticlePacket(pos, other.getEyePosition().subtract(pos),
                        player.getUUID(), other.getUUID()));
    }

    public record ClientBoundSpawnHeartstoneParticlePacket(
            Vec3 pos, Vec3 dist, UUID from, UUID target) implements Message {

        private static final TypeAndCodec<RegistryFriendlyByteBuf, ClientBoundSpawnHeartstoneParticlePacket> CODEC = Message.makeType(
                Heartstone.res("spawn_heartstone_particle"), ClientBoundSpawnHeartstoneParticlePacket::new);

        public ClientBoundSpawnHeartstoneParticlePacket(FriendlyByteBuf buf) {
            this(new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble()),
                    new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble()),
                    buf.readUUID(), buf.readUUID());
        }

        @Override
        public void write(RegistryFriendlyByteBuf buf) {
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
        public void handle(Context context) {
            HeartstoneClient.spawnParticle(this);
        }

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return CODEC.type();
        }
    }
}