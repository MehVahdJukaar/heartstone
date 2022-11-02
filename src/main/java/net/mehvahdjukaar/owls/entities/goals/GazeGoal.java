package net.mehvahdjukaar.owls.entities.goals;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;

public class GazeGoal extends Goal {
    protected final Mob mob;
    protected Entity lookAt;
    protected final float lookDistance;
    private final int minLookTime;
    private int lookTime;
    protected final float probability;
    protected final Class<? extends LivingEntity> lookAtType;
    protected final TargetingConditions lookAtContext;

    public GazeGoal(Mob user, Class<? extends LivingEntity> target, float distance, int lookTime) {
        this(user, target, distance, lookTime, 0.02F);
    }

    public GazeGoal(Mob user, Class<? extends LivingEntity> target, float distance, int lookTime, float probability) {
        this.mob = user;
        this.minLookTime = lookTime;
        this.lookAtType = target;
        this.lookDistance = distance;
        this.probability = probability;
        this.setFlags(EnumSet.of(Flag.LOOK));
        if (target == Player.class) {
            this.lookAtContext = TargetingConditions.forNonCombat().range(distance).selector((e) ->
                    EntitySelector.notRiding(user).test(e));
        } else {
            this.lookAtContext = TargetingConditions.forNonCombat().range(distance);
        }

    }

    @Override
    public boolean canUse() {
        if (this.mob.getRandom().nextFloat() >= this.probability) {
            return false;
        } else {
            if (this.mob.getTarget() != null) {
                this.lookAt = this.mob.getTarget();
            }

            if (this.lookAtType == Player.class) {
                this.lookAt = this.mob.level.getNearestPlayer(this.lookAtContext, this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
            } else {
                this.lookAt = this.mob.level.getNearestEntity(this.lookAtType, this.lookAtContext, this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ(), this.mob.getBoundingBox().inflate(this.lookDistance, 3.0D, this.lookDistance));
            }

            return this.lookAt != null;
        }
    }

    @Override
    public boolean canContinueToUse() {
        if (!this.lookAt.isAlive()) {
            return false;
        } else if (this.mob.distanceToSqr(this.lookAt) > (double)(this.lookDistance * this.lookDistance)) {
            return false;
        } else {
            return this.lookTime > 0;
        }
    }

    @Override
    public void start() {
        this.lookTime = this.minLookTime + this.mob.getRandom().nextInt(10);
    }

    @Override
    public void stop() {
        this.lookAt = null;
    }

    @Override
    public void tick() {
        this.mob.getLookControl().setLookAt(this.lookAt.getX(), this.lookAt.getEyeY(), this.lookAt.getZ());
        --this.lookTime;
    }
}
