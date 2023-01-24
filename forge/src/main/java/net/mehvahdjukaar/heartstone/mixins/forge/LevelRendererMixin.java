package net.mehvahdjukaar.heartstone.mixins.forge;

import com.mojang.blaze3d.vertex.PoseStack;
import net.mehvahdjukaar.heartstone.Heartstone;
import net.mehvahdjukaar.heartstone.HeartstoneClient;
import net.mehvahdjukaar.heartstone.forge.HeartstoneClientImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.OutlineBufferSource;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

    @Shadow @Final public RenderBuffers renderBuffers;

    @ModifyArg(method = "renderLevel", at = @At(target = "Lnet/minecraft/client/renderer/LevelRenderer;renderEntity(Lnet/minecraft/world/entity/Entity;DDDFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;)V", value = "INVOKE"))
    public MultiBufferSource renderEntity(Entity entity, double camX, double camY, double camZ, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource){
        if(HeartstoneClientImpl.getOutlinePlayer() == entity){
            OutlineBufferSource outlinebuffersource = renderBuffers.outlineBufferSource();
            int i = Heartstone.highlightColor.get();
            int k = i >> 16 & 0xFF;
            int l = i >> 8 & 0xFF;
            int i1 = i & 0xFF;
            outlinebuffersource.setColor(k, l, i1, 255);
            return outlinebuffersource;
        }
        return bufferSource;
    }


}
