package net.mehvahdjukaar.heartstone.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.debug.BrainDebugRenderer;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.client.renderer.debug.GoalSelectorDebugRenderer;
import net.minecraft.client.renderer.debug.PathfindingRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DebugRenderer.class)
public abstract class MixinDebugRenderer {


    @Shadow @Final public PathfindingRenderer pathfindingRenderer;

    @Shadow @Final public GoalSelectorDebugRenderer goalSelectorRenderer;

    @Shadow @Final public BrainDebugRenderer brainDebugRenderer;

    @Inject(method = "render",
            at = @At("HEAD"))
    public void onRender(PoseStack pPoseStack, MultiBufferSource.BufferSource pBufferSource, double pCamX, double pCamY, double pCamZ,CallbackInfo ci) {
        pathfindingRenderer.render(pPoseStack,pBufferSource,pCamX,pCamY,pCamZ);
        goalSelectorRenderer.render(pPoseStack,pBufferSource,pCamX,pCamY,pCamZ);
        brainDebugRenderer.render(pPoseStack,pBufferSource,pCamX,pCamY,pCamZ);
        /*
        pathfinding.render(stack, buf, camX, camY, camZ); // Pathfinding info
        field_217742_n.render(stack, buf, camX, camY, camZ); // Entity AI
        field_217741_m.render(stack, buf, camX, camY, camZ); // POI Info
        field_229017_n_.render(stack, buf, camX, camY, camZ); // Bee info
        RenderHelper.renderNoCamOffset(structure::render, stack, buf, camX, camY, camZ);
        //field_222927_n.render(stack, buf, camX, camY, camZ); // Raid info
        */

    }
}
