package net.mehvahdjukaar.heartstone.mixins.forge;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.mojang.blaze3d.vertex.PoseStack;
import net.mehvahdjukaar.heartstone.Heartstone;
import net.mehvahdjukaar.heartstone.HeartstoneClient;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.*;
import net.minecraft.world.entity.Entity;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

    @Shadow
    @Final
    private RenderBuffers renderBuffers;

    @ModifyArg(method = "renderLevel", at = @At(target = "Lnet/minecraft/client/renderer/LevelRenderer;renderEntity(Lnet/minecraft/world/entity/Entity;DDDFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;)V", value = "INVOKE"))
    public MultiBufferSource renderEntity(Entity entity, double camX, double camY, double camZ, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource) {
        if (HeartstoneClient.isPlayerHighlighted(entity)) {
            OutlineBufferSource outlinebuffersource = renderBuffers.outlineBufferSource();
            int color = Heartstone.HIGHLIGHT_COLOR.get();
            int k = color >> 16 & 0xFF;
            int l = color >> 8 & 0xFF;
            int i1 = color & 0xFF;
            outlinebuffersource.setColor(k, l, i1, 255);
            return outlinebuffersource;
        }
        return bufferSource;
    }

    @Inject(method = "renderLevel", at = @At(target = "Lnet/minecraft/client/renderer/LevelRenderer;renderEntity(Lnet/minecraft/world/entity/Entity;DDDFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;)V",
            shift = At.Shift.BEFORE,
            value = "INVOKE"))
    public void renderExtraOutline(PoseStack poseStack, float partialTick, long finishNanoTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f projectionMatrix, CallbackInfo ci,
                                   @Local Entity entity, @Local(ordinal = 3) LocalBooleanRef flag3) {
        if (!flag3.get() && HeartstoneClient.isPlayerHighlighted(entity)) {
            flag3.set(true);
        }
    }

}
