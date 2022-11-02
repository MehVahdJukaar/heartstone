package net.mehvahdjukaar.owls.entities.goals;

import net.mehvahdjukaar.owls.entities.OwlEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class FlyWanderGoal extends WaterAvoidingRandomStrollGoal {
    private final OwlEntity owl;

    public FlyWanderGoal(OwlEntity owlEntity, double speed, float probability) {
        super(owlEntity, speed, probability);
        setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));

        this.owl = owlEntity;
    }

    public FlyWanderGoal(OwlEntity dragon, double speed) {
        this(dragon, speed, 0.001f);
    }

    @Override
    public boolean canUse() {
        if (owl.isInSittingPose()) return false;
        if (owl.canBeControlledByRider()) return false;
        if (!owl.isFlying()) return false;
        Vec3 vec3d = getPosition();
        if (vec3d != null) {
            this.wantedX = vec3d.x;
            this.wantedY = vec3d.y;
            this.wantedZ = vec3d.z;
            this.forceTrigger = false;
            return true;
        }

        return false;
    }

    public double getAltitude() {
        BlockPos.MutableBlockPos pos = owl.blockPosition().mutable().move(0, -1, 0);
        while (pos.getY() > 0 && !owl.level.getBlockState(pos).canOcclude()) pos.setY(pos.getY() - 1);
        return owl.getY() - pos.getY();
    }

    @Override
    public Vec3 getPosition() {
        Vec3 position = null;

        if (owl.isFlying() || (!owl.isLeashed() && owl.getRandom().nextFloat() <= probability + 0.02)) {
            if ((!owl.level.isDay()) || owl.getRandom().nextFloat() <= probability)
                position = LandRandomPos.getPos(owl, 20, 25);
            else {
                Vec3 vec3d = owl.getLookAngle();
                if (!owl.isWithinRestriction())
                    vec3d = Vec3.atLowerCornerOf(owl.getRestrictCenter()).subtract(owl.position()).normalize();

                int yOffset = getAltitude() > 40 ? 10 : 0;

                //TODO: check bee code here
                position = HoverRandomPos.getPos(owl, 50, 30, vec3d.x, vec3d.z, (float) (Math.PI / 2), 10, yOffset);
            }
            if (position != null && position.y > owl.getY() + owl.getBbHeight() && !owl.isFlying()) {
            } //dragon.setFlying(true);
        }

        return position == null ? super.getPosition() : position;
    }
}
