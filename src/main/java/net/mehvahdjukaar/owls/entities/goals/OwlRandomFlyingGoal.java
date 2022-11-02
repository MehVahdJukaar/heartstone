package net.mehvahdjukaar.owls.entities.goals;

import net.mehvahdjukaar.owls.entities.OwlEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class OwlRandomFlyingGoal extends Goal {
    private final OwlEntity parentEntity;
    private int timeSinceLastMove = 0;
    private double lastDist = 0.0D;

    public OwlRandomFlyingGoal(OwlEntity lam) {
        this.parentEntity = lam;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    public boolean canUse() {
        if (!this.parentEntity.isTame() && (this.parentEntity.getTarget() == null || !this.parentEntity.getTarget().isAlive())) {
            if (this.parentEntity.getRandom().nextInt(100) != 1) {
                return false;
            } else {
                MoveControl move = this.parentEntity.getMoveControl();
                double d0 = move.getWantedX() - this.parentEntity.getX();
                double d1 = move.getWantedY() - this.parentEntity.getY();
                double d2 = move.getWantedZ() - this.parentEntity.getZ();
                double distance = d0 * d0 + d1 * d1 + d2 * d2;
                boolean exec = distance < 1.0D || distance > 3600.0D;
                if (!exec) {
                    ++this.timeSinceLastMove;
                }

                if (this.timeSinceLastMove > 60) {
                    this.timeSinceLastMove = 0;
                    return true;
                } else {
                    return exec;
                }
            }
        } else {
            return false;
        }
    }

    public boolean canContinueToUse() {
        return !this.parentEntity.getNavigation().isDone();
    }

    public void tick() {

        boolean idle = true;
        if (this.parentEntity.getNavigation().getPath() != null && this.parentEntity.getNavigation().getPath().getNextNodeIndex() < this.parentEntity.getNavigation().getPath().getNodeCount()) {
            double dist = this.parentEntity.blockPosition().distSqr(this.parentEntity.getNavigation().getPath().getNextNodePos());
            idle = dist - this.lastDist < 0.05D;
            this.lastDist = dist;
        }

        if (idle) {
            ++this.timeSinceLastMove;
            if (this.timeSinceLastMove > 60) {
                this.parentEntity.getNavigation().stop();
                this.parentEntity.getNavigation().recomputePath();
                this.parentEntity.getMoveControl().setWantedPosition(this.parentEntity.getX() + (double) this.parentEntity.getRandom().nextInt(3) - 1.0D, this.parentEntity.getY() + (double) this.parentEntity.getRandom().nextInt(3) - 1.0D, this.parentEntity.getZ() + (double) this.parentEntity.getRandom().nextInt(3) - 1.0D, 0.5D);
            }
        } else {
            this.timeSinceLastMove = 0;
        }

    }

    @Override
    public void stop() {
        super.stop();
    }

    public void start() {

        BlockPos rPos = new BlockPos(this.parentEntity.getRandom().nextInt(50) - 25, this.parentEntity.getRandom().nextInt(40) - 20, this.parentEntity.getRandom().nextInt(50) - 25);
        BlockPos pos = this.parentEntity.blockPosition();
        rPos = rPos.offset(pos);
        this.parentEntity.getNavigation().moveTo((double) rPos.getX(), (double) rPos.getY(), (double) rPos.getZ(), 1.0D);
    }
}