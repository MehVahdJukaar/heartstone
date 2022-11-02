package net.mehvahdjukaar.heartstone.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;

import java.util.ArrayList;
import java.util.List;

public class PacketUtils {

    /**
     * Path info debug packet structure<br>
     * |- Reaches target flag (bool)<br>
     * |- Current path index (int)<br>
     * |- Length of the list of FlaggedNode (appears that the recieved data is not used)<br>
     * |- A list of the FlaggedNode s<br>
     * |- X,Y,Z of the target (ints)<br>
     * |- Amount of points in path (int)<br>
     * |- The list of all points in the path. (Structure for points in {@link #writePointToBuf(FriendlyByteBuf, Node)})<br>
     * |- Amount of open points in the following array (int)<br>
     * |- The array of open points (Again, see {@link #writePointToBuf(FriendlyByteBuf, Node)} for the structure of points)<br>
     * |- Amount of closed points (int)<br>
     * |- Array of closed points {you know what to do, look at @link #writePointToBuf(FriendlyByteBuf, Node)})<br>
     *
     * @param buf  Buffer to write to.
     * @param path The path to write there.
     */
    public static void writePathToBuffer(FriendlyByteBuf buf, Path path) {
        buf.writeBoolean(path.canReach());
        buf.writeInt(path.getNextNodeIndex());

        // Certain stuff has been stripped, lets hope its not neccessary
        // We just skip this step as it seems that the code that reads this bit
        // back does not do anything with the recieved data.
        buf.writeInt(0);

        BlockPos target = path.getTarget();
        buf.writeInt(target.getX()).writeInt(target.getY()).writeInt(target.getZ());
        List<Node> points = new ArrayList<>();
        int nodeCount = path.getNodeCount();
        buf.writeInt(nodeCount);
        for (int n = 0; n < nodeCount; n++) {
            writePointToBuf(buf, path.getNode(n));
        }

        Node[] openSet = path.getOpenSet();
        buf.writeInt(openSet.length);
        for (Node point : openSet)
            writePointToBuf(buf, point);
        Node[] closedSet = path.getClosedSet();
        buf.writeInt(closedSet.length);
        for (Node point : closedSet)
            writePointToBuf(buf, point);
    }

    /**
     * Structure of a point in a packet<br>
     * |- X,Y,Z of the point (int)<br>
     * |- Some value (field_222861_j) (float)<br>
     * |- costMalus (float)<br>
     * |- Whether the point has been visited (bool)<br>
     * |- The index of the PathNodeType#values() array for the type of point (int)<br>
     * |- The distance to the target from this point (float)<br>
     *
     * @param buf   Buffer to write to
     * @param point The point to write to it
     */
    private static void writePointToBuf(FriendlyByteBuf buf, Node point) {
        buf.writeInt(point.x).writeInt(point.y).writeInt(point.z);
        buf.writeFloat(point.walkedDistance);
        buf.writeFloat(point.costMalus);
        buf.writeBoolean(point.closed);
        int idx = -1;
        BlockPathTypes[] types = BlockPathTypes.values();
        for (int i = 0; i < types.length; i++) {
            if (types[i].equals(point.type)) {
                idx = i;
                break;
            }
        }
        assert idx != -1;
        buf.writeInt(idx);
        buf.writeFloat(point.f);
    }

    /**
     * Goal packet format<br>
     * |- Some int (I assume an index)<br>
     * |- A bool (is running?)<br>
     * |- String (assumed to be the name)<br>
     *
     * @param buf  Buffer to write to
     * @param idx  Index of the goal (assuming thats what could be put there)
     * @param goal The goal to write
     */
    public static void writeGoalToBuf(FriendlyByteBuf buf, int idx, WrappedGoal goal) {
        buf.writeInt(idx);
        buf.writeBoolean(goal.isRunning());
        buf.writeUtf(goal.getGoal().toString(), 255);
    }


    /**
     * Bee debug packet format<br>
     * |- Position of the bee (double,double,double)<br>
     * |- The UUID of the bee (UUID)<br>
     * |- Entity ID (int)<br>
     * |- Does a bee hive location follow (bool)<br>
     * |- If the previous was true, the position of the bee's hive (BlockPos)<br>
     * |- Does a flower location follow (bool)<br>
     * |- If the previous was true, the position of the flower (BlockPos)<br>
     * |- Travelling ticks (? couldn't find where this value is on the bee entity) (int)<br>
     * |- Does a path follow (bool)<br>
     * |- If the previous was true, the bee's current path ({@link #writePathToBuffer(FriendlyByteBuf, Path)})<br>
     * |- Number of strings in the following array (int)<br>
     * |- A list of the bee's goals? (List\<String\>)<br>
     * |- Length of the list of blacklisted Hives (int)<br>
     * |- List of the bee's blacklisted hives (BlockPos)<br>
     *
     * @param buf The buffer to write to
     * @param bee The bee entity to write
     */
        /*
    public static void writeBeeToBuf(FriendlyByteBuf buf, BeeEntity bee) {
        buf.writeDouble(bee.getPosX()).writeDouble(bee.getPosY()).writeDouble(bee.getPosZ());
        buf.writeUniqueId(bee.getUniqueID());
        buf.writeInt(bee.getEntityId());
        boolean hive = bee.hasHive();
        buf.writeBoolean(hive);
        if (hive)
            buf.writeBlockPos(bee.getHivePos());
        boolean flower = bee.hasFlower();
        buf.writeBoolean(flower);
        if (flower)
            buf.writeBlockPos(bee.getFlowerPos());
        buf.writeInt(1); // TODO: Find travel ticks?
        boolean hasPath = bee.hasPath();
        buf.writeBoolean(hasPath);
        if (hasPath)
            writePathToBuffer(buf, bee.getNavigator().getPath());
        List<String> goals = bee.goalSelector.getRunningGoals()
                .map(PrioritizedGoal::getGoal).map(Goal::toString)
                .collect(Collectors.toList());
        buf.writeInt(goals.size());
        for (String goal : goals)
            buf.writeString(goal);
        buf.writeInt(0); // TODO: Find where to get the blacklist from.
    }
*/
    /**
     * Sends a packet instructing the client to reset all debug renderers
     */
    /*
    public static void sendReset(ServerWorld world) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        ReflectUtils.DebugPacketSender_func_229753_a_.call(null, world, buf, new ResourceLocation(DebugRenderers.MODID, "clear_all"));
    }*/

    /**
     * Writes the given bounding box to the buffer.
     *
     * @param bb  The bounding box
     * @param buf The buffer to write to
     */
    public static void writeBBToBuf(BoundingBox bb, FriendlyByteBuf buf) {
        buf.writeInt(bb.minX());
        buf.writeInt(bb.minY());
        buf.writeInt(bb.minZ());
        buf.writeInt(bb.maxX());
        buf.writeInt(bb.maxY());
        buf.writeInt(bb.maxZ());
    }
}
