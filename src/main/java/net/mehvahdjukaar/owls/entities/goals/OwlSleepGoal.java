package net.mehvahdjukaar.owls.entities.goals;

import net.mehvahdjukaar.owls.entities.OwlEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class OwlSleepGoal extends Goal {
    private final OwlEntity owl;
    private int wakeUpCooldown;


    public OwlSleepGoal(OwlEntity owlEntity) {
        this.owl = owlEntity;
        this.wakeUpCooldown = owl.getRandom().nextInt(140);
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
    }

    public boolean canUse() {
        //xxa = xSpeed
        if (owl.xxa != 0.0F || owl.yya != 0.0F || owl.zza != 0.0F) return false;
        return owl.isSleeping() || false && (this.owl.getRandom().nextFloat() < 0.03 && this.owl.level.isDay() && this.owl.isOnTree(4));
    }

    public boolean canContinueToUse() {
        if (!owl.isOnGround()) {
            return false;
        }
        if (owl.level.isDay()) {
            return true;
        } else if (this.wakeUpCooldown > 0) {
            --this.wakeUpCooldown;
            return true;
        }
        return false;
    }


    public void stop() {
        this.wakeUpCooldown = owl.getRandom().nextInt(140);
        owl.setSleeping(false);
    }

    public void start() {
        owl.setSleeping(true);
        owl.setJumping(false);
        owl.getNavigation().stop();
        //owl.getMoveControl().setWantedPosition(owl.getX(), owl.getY(), owl.getZ(), 0.0D);
    }
}