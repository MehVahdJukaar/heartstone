package net.mehvahdjukaar.owls.entities.controller;

import net.mehvahdjukaar.owls.entities.OwlEntity;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;


//responsible to move an entity from one pos to another
public class OwlMoveController extends MoveControl {

    private boolean shouldLookAtTarget;
    private boolean needsYSupport;

    private final int maxTurn;

    private final OwlEntity owl;
    private final boolean hoversInPlace;

    public OwlMoveController(OwlEntity owl, boolean hoversInPlace) {
        super(owl);
        this.owl = owl;
        this.maxTurn = 180;
        this.hoversInPlace = hoversInPlace;
    }

    //vanilla parrot
    @Override
    public void tick() {
        if (this.operation == Operation.MOVE_TO) {
            this.operation = Operation.WAIT;
            this.mob.setNoGravity(true);
            double diffX = this.wantedX - this.mob.getX();
            double diffY = this.wantedY - this.mob.getY();
            double diffZ = this.wantedZ - this.mob.getZ();
            Vec3 diffVec = new Vec3(diffX, diffY, diffZ);
            double dist = diffVec.lengthSqr();
            double horizontalDist = Math.sqrt(diffX * diffX + diffZ * diffZ);

            if (dist < (0.01*0.01)) {
                this.operation = Operation.WAIT;
                mob.setDeltaMovement(mob.getDeltaMovement().scale(0.5D));
                this.mob.setYya(0.0F);
                this.mob.setZza(0.0F);
                return;
            }

            float speed;
            if (this.mob.isOnGround()) {
                speed = (float) (this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED));
            } else {
                speed = (float) (this.speedModifier * this.mob.getAttributeValue(Attributes.FLYING_SPEED));
            }
            this.mob.setSpeed(speed);

            //boost
            //speed depends on how fast it's flapping
            float flapProgress = owl.flapSpeed * 0.011f;
            //Vec3 view = this.mob.getLookAngle();
            //Vec3 boostDir = view.add(diffVec.normalize()).scale(0.5);
            //this.mob.setDeltaMovement(mob.getDeltaMovement().add(boostDir.scale(this.speedModifier * flapProgress)));
            this.mob.setDeltaMovement(mob.getDeltaMovement().add(diffVec.normalize().scale(this.speedModifier * flapProgress)));

            if(false){
                double d1 = this.wantedY - owl.getY();
                owl.setDeltaMovement(owl.getDeltaMovement().add(0.0D, owl.getSpeed() * Mth.clamp(d1, -1, 1) * 0.006F, 0.0D));
            }


            //look at target
            float wantedYaw = (float) (Mth.atan2(diffZ, diffX) * (double) (180F / (float) Math.PI)) - 90.0F;

            float maxYawChange = (float) Mth.clamp(horizontalDist*horizontalDist*30,0,90);

            this.mob.setYRot(this.rotlerp(this.mob.getYRot(), wantedYaw, maxYawChange));

            //Vec3 vector3d1 = mob.getDeltaMovement();
            //mob.setYRot(-((float) Mth.atan2(vector3d1.x, vector3d1.z)) * (180F / (float) Math.PI));

            this.mob.yBodyRot =  this.mob.getYRot();






            //xrot stuff. maybe not needed

            float wantedXRot = (float) (-(Mth.atan2(diffY, horizontalDist) * (double) (180F / (float) Math.PI)));
            this.mob.setXRot(this.rotlerp(this.mob.getXRot(), wantedXRot, (float) this.maxTurn));
            this.mob.setYya(diffY > 0.0D ? speed*1 : -speed*1);



        } else {
            //keeping movement
            if (!this.hoversInPlace) {
                this.mob.setNoGravity(false);
            }
            this.mob.setYya(0.0F);
            this.mob.setZza(0.0F);
        }
    }


/*
    //bald eagle
    public void tick() {
        if (this.operation == Action.MOVE_TO) {
            this.mob.setNoGravity(true);

            double diffX = this.wantedX - this.mob.getX();
            double diffY = this.wantedY - this.mob.getY();
            double diffZ = this.wantedZ - this.mob.getZ();
            Vector3d vector3d = new Vector3d(diffX, diffY, diffZ);
            double dist = vector3d.length();

            if (dist < 0.3) {
                this.operation = Action.WAIT;
                mob.setDeltaMovement(mob.getDeltaMovement().scale(0.5D));
            } else {


                float newSpeed;
                if (this.mob.isOnGround()) {
                    newSpeed = (float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED));
                } else {
                    newSpeed = (float)(this.speedModifier * this.mob.getAttributeValue(Attributes.FLYING_SPEED));
                }
                this.mob.setSpeed(newSpeed);


                //mob.setDeltaMovement(mob.getDeltaMovement().add(vector3d.scale(this.speedGeneral * 0.05D / dist)));


                //mob.setDeltaMovement(mob.getDeltaMovement().add(0.0D, mob.getSpeed() * speedGeneral * MathHelper.clamp(diffY, -1, 1) * 0.6F, 0.0D));
                if (this.isPathClear(vector3d, MathHelper.ceil(dist))) {
                    this.mob.setDeltaMovement(mob.getDeltaMovement().add(vector3d.scale( 0.05D / dist )));

                    //this.parentEntity.setDeltaMovement(this.parentEntity.getDeltaMovement().add(vector3d.scale(0.1D)));
                } else {
                    this.operation = MovementController.Action.WAIT;
                }

                //this.mob.setDeltaMovement(mob.getDeltaMovement().add(vector3d.scale( 0.05D / dist )));

                Vector3d vector3d1 = mob.getDeltaMovement();

                float wantedYaw = (float)(MathHelper.atan2(diffZ, diffX) * (double)(180F / (float)Math.PI)) - 90.0F;
                //this.mob.yRot = this.rotlerp(this.mob.yRot, wantedYaw, 40.0F);
                mob.yRot = -((float) MathHelper.atan2(vector3d1.x, vector3d1.z)) * (180F / (float) Math.PI);
                mob.yBodyRot = mob.yRot;
                //this.mob.setYya(diffY > 0.0D ? newSpeed*3 : -newSpeed*3);

            }

        }
        else{
            this.mob.setNoGravity(false);
            this.mob.setYya(0.0F);
            this.mob.setZza(0.0F);
        }
    }

*/

    private boolean isPathClear(Vec3 movement, int distance) {
        AABB axisalignedbb = this.mob.getBoundingBox();

        for (int i = 1; i < distance; ++i) {
            axisalignedbb = axisalignedbb.move(movement);
            if (!this.mob.level.noCollision(this.mob, axisalignedbb)) {
                return false;
            }
        }

        return true;
    }
    /*


     */
    /*

    //crow
    public void tick() {
        if (this.operation == MovementController.Action.MOVE_TO) {

            double diffX = this.wantedX - this.mob.getX();
            double diffY = this.wantedY - this.mob.getY();
            double diffZ = this.wantedZ - this.mob.getZ();
            Vector3d vector3d = new Vector3d(diffX, diffY, diffZ);
            double dist = vector3d.length();

            if (dist < parentEntity.getBoundingBox().getSize()) {
                this.operation = MovementController.Action.WAIT;
                parentEntity.setDeltaMovement(parentEntity.getDeltaMovement().scale(0.5D));
            } else {


                float f1;
                if (this.mob.isOnGround()) {
                    f1 = (float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED));
                } else {
                    f1 = (float)(this.speedModifier * this.mob.getAttributeValue(Attributes.FLYING_SPEED));
                }

                this.mob.setSpeed(f1);


                parentEntity.setDeltaMovement(parentEntity.getDeltaMovement().add(vector3d.scale(this.speedModifier * speedGeneral * 0.05D / dist)));
                if(needsYSupport){
                    double d1 = this.wantedY - parentEntity.getY();
                    parentEntity.setDeltaMovement(parentEntity.getDeltaMovement().add(0.0D, parentEntity.getSpeed() * speedGeneral * MathHelper.clamp(d1, -1, 1) * 0.6F, 0.0D));
                }
                if (parentEntity.getTarget() == null || !shouldLookAtTarget) {
                    Vector3d vector3d1 = parentEntity.getDeltaMovement();
                    parentEntity.yRot = -((float) MathHelper.atan2(vector3d1.x, vector3d1.z)) * (180F / (float) Math.PI);
                } else{
                    double d2 = parentEntity.getTarget().getX() - parentEntity.getX();
                    double d1 = parentEntity.getTarget().getZ() - parentEntity.getZ();
                    parentEntity.yRot = -((float) MathHelper.atan2(d2, d1)) * (180F / (float) Math.PI);
                }
                parentEntity.yBodyRot = parentEntity.yRot;
            }

        }
    }*/


}