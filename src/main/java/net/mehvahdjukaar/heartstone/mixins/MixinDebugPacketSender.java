package net.mehvahdjukaar.heartstone.mixins;

import io.netty.buffer.Unpooled;
import net.mehvahdjukaar.heartstone.utils.PacketUtils;
import net.mehvahdjukaar.heartstone.utils.RenderersState;
import net.mehvahdjukaar.heartstone.utils.RenderersState.RendererType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.stream.Collectors;


@Mixin(DebugPackets.class)
public class MixinDebugPacketSender {

    /**
     * Sends the specified data to all players in the given world.
     *
     * @param world   The server world with the players
     * @param buf     The data to send to all players
     * @param channel The plugin messaging channel to send the data on
     */
    @Shadow
    private static void sendPacketToAllPlayers(ServerLevel world, FriendlyByteBuf buf, ResourceLocation channel) {
    }

    /**
     * Path debug packet format<br>
     * |- The entity ID (int)<br>
     * |- The distance of the path (float)<br>
     * |- The path object (see {@link PacketUtils#writePathToBuffer(FriendlyByteBuf, Path)})
     *
     * @param world    The world of the entity
     * @param entity   The entity
     * @param path     The path
     * @param distance How long the path is
     * @param ci       CallBackInfo from the mixin injection
     */
    @Inject(at = @At("HEAD"),
            method = "sendPathFindingPacket")
    private static void onSendPath(Level world, Mob entity, Path path, float distance, CallbackInfo ci) {
        if (!(world instanceof ServerLevel serverLevel) || path == null || !RenderersState.INSTANCE.get(RendererType.PATHFINDING))
            return;

        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeInt(entity.getId()).writeFloat(distance);
        PacketUtils.writePathToBuffer(buf, path);
        sendPacketToAllPlayers(serverLevel, buf, ClientboundCustomPayloadPacket.DEBUG_PATHFINDING_PACKET);
    }

    /**
     * Goal debug packet format<br>
     * |- The block pos of the entity (int)<br>
     * |- Length of the following list of goals<br>
     * |- A list of goals (see {@link PacketUtils#writeGoalToBuf(FriendlyByteBuf, int, WrappedGoal)} )})
     *
     * @param world    The world of the entity
     * @param entity   The entity
     * @param selector The goal selector to send information for
     * @param ci       CallbackInfo from the mixin injection
     */
    @Inject(at = @At("HEAD"),
            method = "sendGoalSelector")
    private static void onSendGoal(Level world, Mob entity, GoalSelector selector, CallbackInfo ci) {
        if (!(world instanceof ServerLevel) || !RenderersState.INSTANCE.get(RendererType.ENTITY_AI)) return;

        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeBlockPos(entity.blockPosition());
        List<WrappedGoal> goals = selector.getRunningGoals().collect(Collectors.toList());
        buf.writeInt(entity.getId()).writeInt(goals.size());
        for (int i = 0; i < goals.size(); i++) {
            PacketUtils.writeGoalToBuf(buf, i, goals.get(i));
        }
        sendPacketToAllPlayers((ServerLevel) world, buf, ClientboundCustomPayloadPacket.DEBUG_GOAL_SELECTOR);
    }

    /**
     * POI Removed debug packet format<br>
     * |- The position of the removed POI (BlockPos)
     *
     * @param world The world of the event
     * @param pos   The position of the POI
     * @param ci    The CallbackInfo from the mixin injection
     */
    @Inject(method = "sendPoiRemovedPacket", // POI Removed
            at = @At("HEAD"))
    private static void onPoiRemoved(ServerLevel world, BlockPos pos, CallbackInfo ci) {
        if (!RenderersState.INSTANCE.get(RendererType.POI)) return;
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeBlockPos(pos);
        sendPacketToAllPlayers(world, buf, ClientboundCustomPayloadPacket.DEBUG_POI_REMOVED_PACKET);
    }

    /**
     * POI Added debug packet format<br>
     * |- Position (BlockPos)<br>
     * |- POI Type (String)<br>
     * |- Ammount of tickets (int)<br>
     *
     * @param world The world of the event
     * @param pos   The position of the POI
     * @param ci    The CallbackInfo from the mixin injection
     */
    @Inject(method = "sendPoiAddedPacket",
            at = @At("HEAD"))
    private static void onPoiAdded(ServerLevel world, BlockPos pos, CallbackInfo ci) {
        if (!RenderersState.INSTANCE.get(RendererType.POI)) return;
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());

        buf.writeBlockPos(pos);
        buf.writeUtf(world.getBlockState(pos).getBlock().getClass().getSimpleName());
        buf.writeInt(0);

        sendPacketToAllPlayers(world, buf, ClientboundCustomPayloadPacket.DEBUG_POI_ADDED_PACKET);
    }

    /**
     * Hive debug info packet format<br>
     * |- Position of the hive (BlockPos)<br>
     * |- Hive type (String)<br>
     * |- Bee count (int)<br>
     * |- Honey level (int)<br>
     * |- Is sedated (bool)
     *
     * @param te The BeehiveTileEntity
     * @param ci The CallbackInfo from the mixin injection
     */
    /*
    @Inject(method = "sendBeehiveDebugData",
            at = @At("HEAD"))
    private static void onSendBeehiveDebugData(BeehiveTileEntity te, CallbackInfo ci) {
        if (!RenderersState.INSTANCE.get(BEEHIVE)) return;
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());

        buf.writeBlockPos(te.getPos());
        buf.writeString(te.getClass().getSimpleName());
        buf.writeInt(te.getBeeCount());
        buf.writeInt(te.getBlockState().get(BlockStateProperties.HONEY_LEVEL));
        buf.writeBoolean(te.isSmoked());

        func_229753_a_((ServerLevel) te.getLevel(), buf, SCustomPayloadPlayPacket.field_229728_n_);
    }
*/
    /**
     * Bee debug packet format<br>
     * |- The bee data (see {@link PacketUtils#writeBeeToBuf(FriendlyByteBuf, BeeEntity)})
     *
     * @param bee The bee to send
     * @param ci  CallbackInfo from the mixin injection
     */
    /*
    @Inject(method = "func_229749_a_",
            at = @At("HEAD"))
    private static void onSendBeeDebugData(BeeEntity bee, CallbackInfo ci) {
        if (!RenderersState.INSTANCE.get(BEE)) return;
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());

        PacketUtils.writeBeeToBuf(buf, bee);

        func_229753_a_((ServerLevel) bee.getEntityLevel(), buf, SCustomPayloadPlayPacket.field_229727_m_);
    }*/

    /**
     * Raid debug packet format
     * |- Amount of raids (int)
     * |- A list of locations of raids (blockpos)
     *
     * @param world The world
     * @param raids THe collection of raid
     * @param ci    some info about the callback, what did you think this was lol.
     */
    /*
    @Inject(method = "sendRaids",
            at = @At("HEAD"))
    private static void onSendRaids(ServerLevel world, Collection<Raid> raids, CallbackInfo ci) {
        if (!RenderersState.INSTANCE.get(RAID)) return;
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());

        buf.writeInt(raids.size());
        for (Raid raid : raids)
            buf.writeBlockPos(raid.getCenter());

        func_229753_a_(world, buf, SCustomPayloadPlayPacket.DEBUG_RAIDS);
    }*/

    /**
     * Structure debug packet format<br>
     * |- The dimension ID (int)<br>
     * |- Bounding box of the whole structure ({@link PacketUtils#writeBBToBuf(BoundingBox, FriendlyByteBuf)})<br>
     * |- Length of the list of sub-components (int)<br>
     * |- A list of the bounding boxes of the sub-components ({@link PacketUtils#writeBBToBuf(BoundingBox, FriendlyByteBuf)})<br>
     *
     * @param world  The world (a LevelGenRegion)
     * @param struct The structure to send.
     * @param ci     CallbackInfo from the mixin injector.
     */
    /*
    @Inject(method = "sendStructurePacket",
            at = @At("HEAD"))
    private static void onSendStructureStart(WorldGenLevel world, StructureStart<?> struct, CallbackInfo ci) {
        if (!(world instanceof WorldGenRegion worldGenRegion) || !RenderersState.INSTANCE.get(RendererType.STRUCTURE)) return;
        ServerLevel sLevel = worldGenRegion.getLevel();
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());

        buf.writeInt(sLevel.getLevel().getDim.getType().getId());
        PacketUtils.writeBBToBuf(struct.getBoundingBox(), buf);
        List<StructurePiece> parts = struct.getComponents();
        buf.writeInt(parts.size());
        for (StructurePiece part : parts) {
            PacketUtils.writeBBToBuf(part.getBoundingBox(), buf);
            buf.writeBoolean(false);
        }

        func_229753_a_(sLevel, buf, SCustomPayloadPlayPacket.DEBUG_STRUCTURES);
    }*/

}
