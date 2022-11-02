package net.mehvahdjukaar.owls.entities.goals;

import net.mehvahdjukaar.owls.entities.OwlEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

import java.util.EnumSet;

public class OwlLookAtPreyGoal extends Goal {
    protected final OwlEntity owl;
    protected Entity lookAt;
    protected final float lookDistance;
    private final int minLookTime;
    private int lookTime;
    protected final float probability;
    protected final TargetingConditions lookAtContext;

    public OwlLookAtPreyGoal(OwlEntity user, float distance, int lookTime, float probability) {
        this.owl = user;
        this.minLookTime = lookTime;
        this.lookDistance = distance;
        this.probability = probability;
        this.setFlags(EnumSet.of(Flag.LOOK));
        this.lookAtContext = TargetingConditions.forCombat().range(distance).selector(owl.getPreys());

    }

    public boolean canUse() {
        if (this.owl.getRandom().nextFloat() >= this.probability) {
            return false;
        } else {
            if (this.owl.getTarget() != null) {
                this.lookAt = this.owl.getTarget();
            }

            this.lookAt = this.owl.level.getNearestEntity(PathfinderMob.class, this.lookAtContext, this.owl, this.owl.getX(), this.owl.getEyeY(), this.owl.getZ(), this.owl.getBoundingBox().inflate(this.lookDistance, 3.0D, this.lookDistance));

            return this.lookAt != null;
        }
    }

    public boolean canContinueToUse() {
        if (!this.lookAt.isAlive()) {
            return false;
        } else if (this.owl.distanceToSqr(this.lookAt) > (double) (this.lookDistance * this.lookDistance)) {
            return false;
        } else {
            return this.lookTime > 0;
        }
    }

    public void start() {
        this.lookTime = this.minLookTime + this.owl.getRandom().nextInt(10);
    }

    public void stop() {
        this.lookAt = null;
    }

    public void tick() {
        this.owl.getLookControl().setLookAt(this.lookAt.getX(), this.lookAt.getEyeY(), this.lookAt.getZ());
        --this.lookTime;
    }
}
