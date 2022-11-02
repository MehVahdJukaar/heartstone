package net.mehvahdjukaar.owls.entities.goals;


import net.mehvahdjukaar.owls.entities.OwlEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class DiveAtTargetGoal extends Goal {
    private final OwlEntity mob;
    private LivingEntity target;
    private final float yd;

    public DiveAtTargetGoal(OwlEntity mob, float p_i1630_2_) {
        this.mob = mob;
        this.yd = p_i1630_2_;
        this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
    }

    public boolean canUse() {
        if (!this.mob.isVehicle()) {
            this.target = this.mob.getTarget();
            if (this.target != null) {
                double d0 = this.mob.distanceToSqr(this.target);
                if (!(d0 < 4.0D) && !(d0 > 16.0D)) {
                    if (!this.mob.isOnGround()) {
                        return this.mob.getRandom().nextInt(5) == 0;
                    }
                }
            }
        }
        return false;
    }

    public boolean canContinueToUse() {
        return this.mob.isOnGround();
    }

    public void start() {
        this.mob.setDiving(true);
        Vec3 vector3d = this.mob.getDeltaMovement();
        Vec3 vector3d1 = new Vec3(this.target.getX() - this.mob.getX(), this.target.getY() - this.mob.getY(), this.target.getZ() - this.mob.getZ());
        if (vector3d1.lengthSqr() > 1.0E-7D) {
            vector3d1 = vector3d1.normalize().scale(0.4D).add(vector3d.scale(0.2D));
        }

        this.mob.setDeltaMovement(vector3d1.x, vector3d1.y, vector3d1.z);
    }

    @Override
    public void stop() {
        super.stop();
        this.mob.setDiving(false);
    }
}
