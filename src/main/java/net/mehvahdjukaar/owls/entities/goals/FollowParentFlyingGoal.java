package net.mehvahdjukaar.owls.entities.goals;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.pathfinder.Path;

import java.util.EnumSet;
import java.util.List;

public class FollowParentFlyingGoal  extends Goal {
    private final Animal childAnimal;
    private Animal parentAnimal;
    private final double speedModifier;
    private int timeToRecalcPath;
    private float range = 8F;
    private float minDist = 3F;
    public FollowParentFlyingGoal(Animal child, double speed, float range, float minDist) {
        this.childAnimal = child;
        this.speedModifier = speed;
        this.range = range;
        this.minDist = minDist;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        if (this.childAnimal.getAge() >= 0) {
            return false;
        } else {
            var entityList = this.childAnimal.level.getEntitiesOfClass(this.childAnimal.getClass(), this.childAnimal.getBoundingBox().inflate(range, range * 0.5D, range));
            Animal parent = null;
            double d0 = Double.MAX_VALUE;

            for (Animal a : entityList) {
                if (a.getAge() >= 0) {
                    double distance = this.childAnimal.distanceToSqr(a);
                    if (distance <= d0) {
                        d0 = distance;
                        parent = a;
                    }
                }
            }

            if (parent == null) {
                return false;
            } else if (d0 < minDist * minDist) {
                return false;
            } else {
                this.parentAnimal = parent;
                return true;
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        if (this.childAnimal.getAge() >= 0) {
            return false;
        } else if (!this.parentAnimal.isAlive()) {
            return false;
        } else {
            double dist = this.childAnimal.distanceToSqr(this.parentAnimal);
            return dist >= minDist * minDist && dist <= range * range;
        }
    }

    @Override
    public void start() {
        this.timeToRecalcPath = 0;
    }

    @Override
    public void stop() {
        this.parentAnimal = null;
    }

    @Override
    public void tick() {
        if (--this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = 10;
            PathNavigation nav = this.childAnimal.getNavigation();
            Path path = nav.createPath(this.parentAnimal, 2);
            if(path != null) nav.moveTo(path, this.speedModifier);
        }
    }
}