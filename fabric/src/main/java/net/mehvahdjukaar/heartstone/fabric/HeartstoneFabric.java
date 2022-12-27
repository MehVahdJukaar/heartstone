package net.mehvahdjukaar.heartstone.fabric;

import net.fabricmc.api.ModInitializer;
import net.mehvahdjukaar.heartstone.Heartstone;
import net.mehvahdjukaar.heartstone.HeartstoneClient;
import net.mehvahdjukaar.moonlight.api.platform.PlatformHelper;
import net.mehvahdjukaar.moonlight.fabric.FabricSetupCallbacks;

public class HeartstoneFabric implements ModInitializer {

    @Override
    public void onInitialize() {

        Heartstone.commonInit();

        if (PlatformHelper.getEnv().isClient()) {
            FabricSetupCallbacks.CLIENT_SETUP.add(HeartstoneClient::init);
        }

    }
}
