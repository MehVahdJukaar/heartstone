package net.mehvahdjukaar.heartstone;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public class NetworkHandler {
    public static SimpleChannel INSTANCE;
    private static final String PROTOCOL_VERSION = "1";

    public static void registerMessages(FMLCommonSetupEvent event) {
        INSTANCE = NetworkRegistry.newSimpleChannel(HeartstoneOld.res("network"), () -> PROTOCOL_VERSION,
                PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

        INSTANCE.registerMessage(0, ClientBoundSpawnHeartstoneParticlePacket.class, ClientBoundSpawnHeartstoneParticlePacket::buffer,
                ClientBoundSpawnHeartstoneParticlePacket::new, ClientBoundSpawnHeartstoneParticlePacket::handler);
    }

    public static void sendHeartstoneParticles(Player player, Player other) {
        Vec3 pos = player.getEyePosition();
        NetworkHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(()->player),
                new ClientBoundSpawnHeartstoneParticlePacket(pos, other.getEyePosition().subtract(pos)));
    }

    public interface Message {
    }

    public static class ClientBoundSpawnHeartstoneParticlePacket implements Message {
        public final Vec3 pos;
        public final Vec3 dist;

        public ClientBoundSpawnHeartstoneParticlePacket(FriendlyByteBuf buf) {
            this.pos = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
            this.dist = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
        }

        public ClientBoundSpawnHeartstoneParticlePacket(Vec3 pos, Vec3 dist) {
            this.pos = pos;
            this.dist = dist;
        }

        public static void buffer(ClientBoundSpawnHeartstoneParticlePacket message, FriendlyByteBuf buf) {
            buf.writeDouble(message.pos.x);
            buf.writeDouble(message.pos.y);
            buf.writeDouble(message.pos.z);
            buf.writeDouble(message.dist.x);
            buf.writeDouble(message.dist.y);
            buf.writeDouble(message.dist.z);
        }

        public static void handler(ClientBoundSpawnHeartstoneParticlePacket message, Supplier<NetworkEvent.Context> ctx) {
            // client world
            NetworkEvent.Context context = ctx.get();
            context.enqueueWork(() -> {
                if (context.getDirection().getReceptionSide().isClient()) {
                    HeartstoneParticle.spawnParticle(message);
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }
}