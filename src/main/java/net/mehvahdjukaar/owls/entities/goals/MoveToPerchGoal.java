package net.mehvahdjukaar.owls.entities.goals;

import net.mehvahdjukaar.owls.entities.OwlEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;

public class MoveToPerchGoal extends MoveToBlockGoal {
    private final OwlEntity owl;
    private final int minPerchHeight;
    private final int maxPerchHeight;
    private final int searchRange = 8;
    private final int searchAttempts = 30;
    private boolean reachedTarget2 = false;

    public MoveToPerchGoal(OwlEntity owl, int minPerchHeight, int maxPerchHeight) {
        super(owl, 1, 8);
        this.owl = owl;
        this.minPerchHeight = minPerchHeight;
        this.maxPerchHeight = maxPerchHeight;
    }

    @Override
    public boolean canUse() {
        if (this.owl.isTame() && this.owl.isOrderedToSit()) return false;
        if (owl.isSleeping()) return false;
        if (!owl.isOnGround() && owl.isBaby()) return false;
        return super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        if (this.reachedTarget2 && owl.isOnGround() && this.owl.getDeltaMovement().length() < 0.1 && owl.getRandom().nextFloat() < 0.07)
            return false;
        return this.tryTicks <= 300 && this.isValidTarget(this.mob.level, this.blockPos);
    }

    //path finder considers reached when its less than 1 block so it cant be too precise here
    @Override
    public double acceptedDistance() {
        return 0.3D;
    }

    @Override
    public void start() {
        super.start();
        this.owl.setInSittingPose(false);
    }

    @Override
    public void stop() {
        super.stop();
        if (owl.level.isDay())
            owl.setSleeping(this.reachedTarget2);
        //this.owl.setInSittingPose(this.isReachedTarget());
    }

    @Override
    public boolean shouldRecalculatePath() {
        return this.tryTicks % 20 == 0;
    }

    @Override
    public void tick() {
        BlockPos blockpos = this.getMoveToTarget();
        Vec3 v = Vec3.atCenterOf(blockpos);
        Vec3 diff = v.subtract(this.mob.position().add(0, 0.5, 0));
        double dist = diff.lengthSqr();
        double maxDist = this.acceptedDistance();
        if (!(dist < (maxDist * maxDist))) {
            this.reachedTarget2 = false;
            ++this.tryTicks;
            if (this.shouldRecalculatePath()) {
                PathNavigation navigator = this.mob.getNavigation();
                boolean baby = owl.isBaby();
                //NAVIGATOR WONT MOVE IT WITH SHORT DISTANCES
                navigator.moveTo(navigator.createPath((double) ((float) blockpos.getX()) + 0.5, blockpos.getY() + 0.5f, (double) ((float) blockpos.getZ()) + 0.5f, 0),
                        this.speedModifier * (baby ? 0.45f : 1));
            }
            //this is horrible lol. pushes entities that didnt land on the center of the block to it
            if (dist < 1.3 && this.mob.isOnGround() && this.mob.getNavigation().isDone()) {
                this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(diff.normalize().scale(0.01)));
                this.owl.getLookControl().setLookAt(v.x, v.y, v.z);
            }
        } else {
            this.reachedTarget2 = true;
            --this.tryTicks;
        }
    }

    //idk why I need this damn
    @Override
    protected BlockPos getMoveToTarget() {
        return this.blockPos.above(1);
    }

    @Override
    protected boolean findNearestBlock() {
        BlockPos targetPos = findRandomPerchRecursive();
        if (targetPos != null) {
            this.owl.level.setBlock(targetPos, Blocks.DIAMOND_BLOCK.defaultBlockState(), 3);
            //List<CreatureEntity> others = owl.level.getLoadedEntitiesOfClass(CreatureEntity.class, new AxisAlignedBB(targetPos).inflate(2));
            //if (others.size() != 0) eturn false;

            this.blockPos = targetPos;
            return true;
        }
        return false;
    }


    @Override
    protected boolean isValidTarget(LevelReader world, BlockPos pos) {
        BlockState feetState = world.getBlockState(pos);
        if (!feetState.getMaterial().isSolid() || feetState.getCollisionShape(world, pos).isEmpty()) {
            return false;
        }
        BlockPos above = pos.above();
        return world.getBlockState(above).getCollisionShape(world, above).isEmpty();
    }


    private BlockPos findRandomPerchRecursive() {
        for (int i = 0; i < searchAttempts; i++) {
            BlockPos res = findRandomPerch();
            if (res != null) {
                return res;
            }
        }
        return null;
    }


    private BlockPos findRandomPerch() {

        BlockPos entityPos = mob.blockPosition();
        Level world = mob.level;

        int range = searchRange - (this.owl.isBaby() ? 4 : 0);

        int x = entityPos.getX() - range + (world.random.nextInt(range + 1) * 2);
        int z = entityPos.getZ() - range + (world.random.nextInt(range + 1) * 2);

        int entityY = entityPos.getY() - 1;
        int y = Math.min(entityY + maxPerchHeight + 1, world.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z));

        int minY = entityY + minPerchHeight;

        boolean canLand = false;
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(x, y, z);
        while (!canLand) {
            --y;
            if (y < minY) {
                return null;
            }
            pos.set(x, y, z);
            canLand = this.isValidTarget(world, pos) && this.owl.isWithinRestriction(pos);
        }
        world.setBlockAndUpdate(pos, Blocks.EMERALD_BLOCK.defaultBlockState());
        return pos;
    }

}
