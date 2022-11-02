package net.mehvahdjukaar.owls.entities.goals;


import net.mehvahdjukaar.owls.entities.OwlEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.EnumSet;

public class OwlInterestedGoal extends Goal {
    private final OwlEntity owl;
    private Player player;
    private final float lookDistance;
    private int lookTime;
    private final TargetingConditions begTargeting;

    public OwlInterestedGoal(OwlEntity owlEntity, float lookDistance) {
        this.owl = owlEntity;
        this.lookDistance = lookDistance;
        this.begTargeting = TargetingConditions.forNonCombat().range(lookDistance);
        //  this.begTargeting = (new TargetingConditions()).range(lookDistance).allowInvulnerable().allowSameTeam().allowNonAttackable();
        this.setFlags(EnumSet.of(Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (!this.owl.isOnGround()) return false;
        this.player = this.owl.level.getNearestPlayer(this.begTargeting, this.owl);
        return this.player != null && this.playerHoldingInteresting(this.player);
    }

    @Override
    public boolean canContinueToUse() {
        if (!this.owl.isOnGround()) {
            return false;
        } else if (!this.player.isAlive()) {
            return false;
        } else if (this.owl.distanceToSqr(this.player) > (double) (this.lookDistance * this.lookDistance)) {
            return false;
        } else {
            return this.lookTime > 0 && this.playerHoldingInteresting(this.player);
        }
    }

    @Override
    public void start() {
        this.owl.setInterested(true);
        this.lookTime = 50 + this.owl.getRandom().nextInt(40);
    }

    @Override
    public void stop() {
        this.owl.setInterested(false);
        this.player = null;
    }

    @Override
    public void tick() {
        this.owl.getLookControl().setLookAt(this.player.getX(), this.player.getEyeY(), this.player.getZ(), this.owl.getMaxHeadYRot(), (float) this.owl.getMaxHeadXRot());
        --this.lookTime;
    }

    private boolean playerHoldingInteresting(Player player) {
        for (InteractionHand hand : InteractionHand.values()) {
            ItemStack itemstack = player.getItemInHand(hand);

            if (!this.owl.isTame()) {
                return this.owl.isTameFood(itemstack);
            }
            if (this.owl.isFood(itemstack)) {
                return true;
            }
        }

        return false;
    }
}

