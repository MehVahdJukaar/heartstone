package net.mehvahdjukaar.heartstone.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.mehvahdjukaar.heartstone.Heartstone;
import net.mehvahdjukaar.heartstone.HeartstoneClient;
import net.mehvahdjukaar.moonlight.api.platform.PlatformHelper;
import net.mehvahdjukaar.moonlight.fabric.FabricSetupCallbacks;

public class HeartstoneFabric implements ModInitializer {

    @Override
    public void onInitialize() {

        Heartstone.commonInit();
        FabricSetupCallbacks.COMMON_SETUP.add(Heartstone::commonSetup);

        if (PlatformHelper.getEnv().isClient()) {
            FabricSetupCallbacks.CLIENT_SETUP.add(HeartstoneClient::init);
        }
        FabricSetupCallbacks.finishModInit(Heartstone.MOD_ID);
    }
}
