package net.mehvahdjukaar.owls.client.renderers.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.mehvahdjukaar.owls.entities.OwlEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;


public class OwlEyesLayer extends EyesLayer<OwlEntity, OwlModel> {


    public OwlEyesLayer(RenderLayerParent<OwlEntity, OwlModel> renderer) {
        super(renderer);

    }

    @Override
    public void render(PoseStack matrixStack, MultiBufferSource buffer, int p_225628_3_, OwlEntity owl, float p_225628_5_, float p_225628_6_, float r, float g, float b, float a) {
        if (owl.isSleeping()) return;
        VertexConsumer ivertexbuilder = buffer.getBuffer(RenderType.eyes(this.getTextureLocation(owl)));
        this.getParentModel().renderToBuffer(matrixStack, ivertexbuilder, 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }


    public RenderType renderType() {
        return null;
    }

    @Override
    public ResourceLocation getTextureLocation(OwlEntity entity) {
        return entity.getOwlType().eyesTexture;
    }
}