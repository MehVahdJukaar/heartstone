package net.mehvahdjukaar.owls.client.renderers;

import net.mehvahdjukaar.owls.client.ClientSetup;
import net.mehvahdjukaar.owls.client.renderers.models.OwlEyesLayer;
import net.mehvahdjukaar.owls.client.renderers.models.OwlHeldItemLayer;
import net.mehvahdjukaar.owls.client.renderers.models.OwlModel;
import net.mehvahdjukaar.owls.entities.OwlEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class OwlEntityRenderer extends MobRenderer<OwlEntity, OwlModel> {

    public OwlEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new OwlModel(context.bakeLayer(ClientSetup.OWL_MODEL)), 0.3F);
        this.addLayer(new OwlEyesLayer(this));
        this.addLayer(new OwlHeldItemLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(OwlEntity entity) {
        return entity.isSleeping() ? entity.getOwlType().sleepingTexture : entity.getOwlType().texture;
    }


}