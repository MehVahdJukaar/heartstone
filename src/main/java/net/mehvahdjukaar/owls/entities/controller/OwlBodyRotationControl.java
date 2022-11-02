package net.mehvahdjukaar.owls.entities.controller;

import net.mehvahdjukaar.owls.entities.OwlEntity;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.BodyRotationControl;

public class OwlBodyRotationControl extends BodyRotationControl {
    private final OwlEntity mob;
    private static final int DELAY_UNTIL_STARTING_TO_FACE_FORWARD = 20 * 6;
    private static final int HOW_LONG_IT_TAKES_TO_FACE_FORWARD = 20;
    private int headStableTime;
    private float lastStableYHeadRot;

    public OwlBodyRotationControl(OwlEntity pMob) {
        super(pMob);
        this.mob = pMob;
    }

    /**
     * Update the Head and Body rendenring angles
     */
    @Override
    public void clientTick() {
        if (this.isMoving()) {
            this.mob.yBodyRot = this.mob.getYRot();
            this.rotateHeadIfNecessary();
            this.lastStableYHeadRot = this.mob.yHeadRot;
            this.headStableTime = 0;
        } else {
            if (this.notCarryingMobPassengers()) {
                if (Math.abs(this.mob.yHeadRot - this.lastStableYHeadRot) > 15.0F) {
                    this.headStableTime = 0;
                    this.lastStableYHeadRot = this.mob.yHeadRot;
                    float f = Mth.degreesDifference(this.mob.yBodyRot, this.mob.yHeadRot);
                    float maxDiff = this.mob.getMaxHeadYRot();
                    //if more than 180 just completely rotates the whole body
                    if (Mth.abs(f) > maxDiff) {
                        this.headStableTime = DELAY_UNTIL_STARTING_TO_FACE_FORWARD + 1;
                    }

                } else {
                    ++this.headStableTime;
                    if (this.headStableTime > DELAY_UNTIL_STARTING_TO_FACE_FORWARD) {
                        this.rotateBodyToFaceFront();
                    }
                }
            }
        }
    }

    private void rotateBodyIfNecessary() {
        this.mob.yBodyRot = Mth.rotateIfNecessary(this.mob.yBodyRot, this.mob.yHeadRot, (float) this.mob.getMaxHeadYRot());
    }

    private void rotateHeadIfNecessary() {
        this.mob.yHeadRot = Mth.rotateIfNecessary(this.mob.yHeadRot, this.mob.yBodyRot, (float) this.mob.getMaxHeadYRot());
    }


    private void rotateBodyToFaceFront() {
        int time = this.headStableTime - DELAY_UNTIL_STARTING_TO_FACE_FORWARD;
        float f = Mth.clamp((float) time / HOW_LONG_IT_TAKES_TO_FACE_FORWARD, 0.0F, 1.0F);
        float f1 = (float) this.mob.getMaxHeadYRot() * (1.0F - f);
        this.mob.yBodyRot = Mth.rotateIfNecessary(this.mob.yBodyRot, this.mob.yHeadRot, f1);
    }

    private boolean notCarryingMobPassengers() {
        return !(this.mob.getFirstPassenger() instanceof Mob);
    }

    private boolean isMoving() {
        double d0 = this.mob.getX() - this.mob.xo;
        double d1 = this.mob.getZ() - this.mob.zo;
        return d0 * d0 + d1 * d1 > (double) 2.5000003E-7F;
    }
}