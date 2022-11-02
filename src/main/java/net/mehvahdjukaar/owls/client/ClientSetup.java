package net.mehvahdjukaar.owls.client;

import net.mehvahdjukaar.heartstone.Heartstone;
import net.mehvahdjukaar.owls.OwlMod;
import net.mehvahdjukaar.owls.client.renderers.OwlEntityRenderer;
import net.mehvahdjukaar.owls.client.renderers.models.OwlModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Heartstone.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    private static ModelLayerLocation loc(String name) {
        return new ModelLayerLocation(Heartstone.res(name), name);
    }

    public static ModelLayerLocation OWL_MODEL = loc("bellows");

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(OWL_MODEL, OwlModel::createMesh);
    }

    @SubscribeEvent
    public static void entityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        //entities
        event.registerEntityRenderer(OwlMod.OWL.get(), OwlEntityRenderer::new);
    }

}
