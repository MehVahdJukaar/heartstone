package net.mehvahdjukaar.owls.client.renderers.models;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.mehvahdjukaar.owls.entities.OwlEntity;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class OwlModel extends AgeableListModel<OwlEntity> {
    private final ModelPart body;
    private final ModelPart body2;
    private final ModelPart tail;
    private final ModelPart shoulderLeft;
    private final ModelPart shoulderRight;
    private final ModelPart head;
    private final ModelPart neck;
    private final ModelPart legLeft;
    private final ModelPart legRight;


    private final ModelPart wingLeft;
    private final ModelPart wingRight;


    public OwlModel(ModelPart root) {
        super(true, 14.375f, 0, 2.25F, 2.0F, 24.0F);
        body = root.getChild("body");

        body2 = body.getChild("body2");
        tail = body2.getChild("tail");
        legLeft = body2.getChild("legLeft");
        legRight = body2.getChild("legRight");
        shoulderLeft = body.getChild("shoulderLeft");
        shoulderRight = body.getChild("shoulderRight");
        wingLeft = shoulderLeft.getChild("wingLeft");
        wingRight = shoulderRight.getChild("wingRight");

        neck = root.getChild("neck");
        head = neck.getChild("head");

    }

    public static LayerDefinition createMesh() {
        MeshDefinition mesh = PlayerModel.createMesh(CubeDeformation.NONE, false);
        PartDefinition root = mesh.getRoot();

        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0, 24, 0));

        PartDefinition body2 = body.addOrReplaceChild("body2", CubeListBuilder.create()
                        .texOffs(0, 13)
                        .addBox(-4.0F, -0.5F, -3.5F, 8.0F, 8.0F, 7.0F, false),
                PartPose.offset(0, -9.5F, 0));

        PartDefinition tail = body2.addOrReplaceChild("tail", CubeListBuilder.create()
                        .texOffs(0, 28)
                        .addBox(-2.5F, -0.5F, -0.5F, 5.0F, 3.0F, 1.0F, false),
                PartPose.offsetAndRotation(0.0F, 6.55F, 3.25F, 0.4363F, 0.0F, 0.0F));


        PartDefinition legRight = body2.addOrReplaceChild("legRight", CubeListBuilder.create()
                        .texOffs(14, 28)
                        .addBox(-0.5F, 2.0F, -1.5F, 1.0F, 0.0F, 1.0F, false)
                        .texOffs(14, 29)
                        .addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, false),
                PartPose.offset(-2.0F, 7.5F, 0.0F));

        PartDefinition legLeft = body2.addOrReplaceChild("legLeft", CubeListBuilder.create()
                        .texOffs(19, 29)
                        .addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, false)
                        .texOffs(19, 28)
                        .addBox(-0.5F, 2.0F, -1.5F, 1.0F, 0.0F, 1.0F, false),
                PartPose.offset(2.0F, 7.5F, 0.0F));

        PartDefinition neck = root.addOrReplaceChild("neck", CubeListBuilder.create(),
                PartPose.offset(0.0F, 14.0F, 0.0F));

        PartDefinition head = neck.addOrReplaceChild("head", CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-0.5F, -2.0F, -4.5F, 1.0F, 2.0F, 1.0F, false)

                        .texOffs(0, 0)
                        .addBox(-4.0F, -6.0F, -3.5F, 8.0F, 6.0F, 7.0F, new CubeDeformation(0.01f))

                        .texOffs(24, 30)
                        .addBox(-5.0F, -7.0F, -3.5F, 10.0F, 2.0F, 0.0F, false),
                PartPose.ZERO);



        /*
        head = new ModelRenderer(this);
        head.setPos(0.0F, 14.0F, 0.0F);
        //body.addChild(head);
        head.texOffs(0, 0).addBox(-0.5F, -2.0F, -4.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        head.texOffs(0, 0).addBox(-4.0F, -6.0F, -3.5F, 8.0F, 6.0F, 7.0F, 0.01F, false);
        head.texOffs(24, 30).addBox(-5.0F, -7.0F, -3.5F, 10.0F, 2.0F, 0.0F, 0.0F, false);
        */

        PartDefinition shoulderLeft = body.addOrReplaceChild("shoulderLeft", CubeListBuilder.create(),
                PartPose.offsetAndRotation(4.5F, -9.5F, 0.0F, -0.2618F, 0.0F, 0.0F));

        PartDefinition wingLeft = shoulderLeft.addOrReplaceChild("wingLeft", CubeListBuilder.create()
                        .texOffs(35, 10)
                        .addBox(-0.25F, -0.93F, -3.3F, 1.0F, 8.0F, 6.0F, false),
                PartPose.offsetAndRotation(-0.25F, 0.25F, 0.5F, 0.4363F, 0.0F, 0.0F));

        PartDefinition shoulderRight = body.addOrReplaceChild("shoulderRight", CubeListBuilder.create(),
                PartPose.offsetAndRotation(-4.5F, -9.5F, 0.0F, -0.2618F, 0.0F, 0.0F));

        PartDefinition wingRight = shoulderRight.addOrReplaceChild("wingRight", CubeListBuilder.create()
                        .texOffs(50, 10)
                        .addBox(-0.75F, -0.93F, -3.3F, 1.0F, 8.0F, 6.0F, false),
                PartPose.offsetAndRotation(0.25F, 0.25F, 0.5F, 0.4363F, 0.0F, 0.0F));

        /*
        shoulderRight = new ModelRenderer(this);
        shoulderRight.setPos(-4.5F, -9.5F, 0.0F);
        body.addChild(shoulderRight);
        setRotationAngle(shoulderRight, 0.1745F, 0.0F, 0.0F);


        wingRight = new ModelRenderer(this);
        wingRight.setPos(0.5F, -0.5F, 0.0F);
        shoulderRight.addChild(wingRight);
        wingRight.texOffs(50, 10).addBox(-1.0F, 0.0F, -3.0F, 1.0F, 8.0F, 6.0F, 0.0F, false);
        */

        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    protected Iterable<ModelPart> headParts() {
        return ImmutableList.of(this.neck);
    }

    @Override
    protected Iterable<ModelPart> bodyParts() {
        return ImmutableList.of(this.body);
    }

    public void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }


    private float getHeadRollAngle(OwlEntity owl, float partialTicks) {
        return Mth.lerp(partialTicks, owl.interestedAngleO, owl.interestedAngle) * 0.15F * (float) Math.PI;
    }

    private float getWingRotation(OwlEntity entity, float partialTicks) {
        float freq = Mth.lerp(partialTicks, entity.oFlap, entity.flap);
        float amp = Mth.lerp(partialTicks, entity.oFlapSpeed, entity.flapSpeed);
        float flapSpeed = 0.4f;
        return (Mth.sin(freq * flapSpeed) + 1.0F) * amp;
    }

    public void prepareMobModel(OwlEntity p_212843_1_, float p_212843_2_, float p_212843_3_, float p_212843_4_) {
        //this.prepare(getState(p_212843_1_));
    }

    @Override
    public void setupAnim(OwlEntity owl, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

        float partialTicks = ageInTicks - ((int) ageInTicks);

        State state = getState(owl);
        float wingFlap = getWingRotation(owl, partialTicks);
        float headRoll = getHeadRollAngle(owl, partialTicks);

        this.head.xRot = headPitch * ((float) Math.PI / 180F);
        this.neck.yRot = netHeadYaw * ((float) Math.PI / 180F);
        this.neck.zRot = headRoll * 0.65f;
        //this.head.x = 0.0F;
        //this.body.x = 0.0F;
        //this.tail.x = 0.0F;
        //this.shoulderRight.x = -1.5F;
        //this.shoulderLeft.x = 1.5F;

        this.body.xRot = 0;
        this.body.y = 24;
        this.body.z = 0;

        this.neck.y = 14;
        this.neck.z = 0;

        this.shoulderRight.xRot = -0.2618F;
        this.shoulderLeft.xRot = -0.2618F;

        if (state == State.SITTING) {
            this.legLeft.y = 6.5F;
            this.legRight.y = 6.5F;
            this.body.y = 25.0F;
            this.neck.y = 15.0F;
            this.legLeft.yRot = -0.25f;
            this.legRight.yRot = 0.25f;
        } else {
            this.legLeft.y = 7.5F;
            this.legRight.y = 7.5F;
            this.legLeft.yRot = 0f;
            this.legRight.yRot = 0f;
        }

        switch (state) {
            case BABY_SLEEP:
                this.neck.xRot = 0;

                this.body2.xRot = 0;
                this.body.xRot = (float) (Math.PI / 2f);
                this.body.y = 20.5f;
                this.body.z = 10;
                this.neck.y = 21.75f;
                this.neck.z = -3;

                this.shoulderRight.xRot = -0.35F;
                this.shoulderLeft.xRot = -0.35F;

                this.legLeft.xRot = -0.8f;
                this.legRight.xRot = -0.8f;

                this.tail.xRot = 0.1463F;

                this.wingLeft.zRot = 0;

                this.wingRight.zRot = 0;
                break;
            case SITTING:
                this.neck.xRot = 0;

                this.body2.xRot = 0;

                this.legLeft.xRot = 0;
                this.legRight.xRot = 0;

                this.tail.xRot = 0.4363F + Mth.cos(limbSwing * 0.8F) * 0.4F * limbSwingAmount;

                this.wingLeft.zRot = -wingFlap;

                this.wingRight.zRot = wingFlap;

                break;
            default:
            case STANDING:
                this.neck.xRot = 0;

                this.body2.xRot = 0;
                this.legLeft.xRot = Mth.cos(limbSwing * 1.6662F) * 1.4F * limbSwingAmount;
                this.legRight.xRot = Mth.cos(limbSwing * 1.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;

                this.tail.xRot = 0.4363F + Mth.cos(limbSwing * 0.8F) * 0.4F * limbSwingAmount;

                this.wingLeft.zRot = -wingFlap;

                this.wingRight.zRot = wingFlap;
                break;
            case DIVING:
            case FLYING:
                this.neck.xRot = 0.35f;

                this.legLeft.xRot = -0.6f;
                this.legRight.xRot = -0.6f;

                this.body2.xRot = 0.8f + Mth.cos(ageInTicks * 0.09F) * 0.03F;

                this.tail.xRot = 0.4363F + Mth.cos(limbSwing * 0.8F) * 0.4F * limbSwingAmount;

                this.wingLeft.zRot = -wingFlap;

                this.wingRight.zRot = wingFlap;

                break;
        }

    }


    private static State getState(OwlEntity entity) {
        if (entity.isBaby() && entity.isSleeping()) {
            return State.BABY_SLEEP;
        } else if (entity.isInSittingPose() || entity.isSleeping()) {
            return State.SITTING;
        } else if (entity.isDiving()) {
            return State.DIVING;
        }
        return entity.isFlying() ? State.FLYING : State.STANDING;
    }


    public void translateToFeet(PoseStack matrixStack) {
        this.body.translateAndRotate(matrixStack);
        this.body2.translateAndRotate(matrixStack);
        this.legRight.translateAndRotate(matrixStack);

        matrixStack.translate(0.125, 0.125, 0);
        //matrixStack.translate(0,0.75,0);

        matrixStack.mulPose(Vector3f.XP.rotation(-body2.xRot - legRight.xRot));

        matrixStack.translate(0, 0.3126, 0);
    }

    @OnlyIn(Dist.CLIENT)
    public static enum State {
        FLYING,
        STANDING,
        SITTING,
        BABY_SLEEP,
        DIVING;
    }
}