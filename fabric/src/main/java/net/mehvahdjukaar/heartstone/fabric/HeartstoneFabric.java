package net.mehvahdjukaar.heartstone.fabric;

import net.fabricmc.api.ModInitializer;
import net.mehvahdjukaar.heartstone.Heartstone;
import net.mehvahdjukaar.heartstone.HeartstoneClient;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.fabric.MLFabricSetupCallbacks;

public class HeartstoneFabric implements ModInitializer {

    @Override
    public void onInitialize() {

        Heartstone.commonInit();
        MLFabricSetupCallbacks.COMMON_SETUP.add(Heartstone::commonSetup);

        if (PlatHelper.getPhysicalSide().isClient()) {
            MLFabricSetupCallbacks.CLIENT_SETUP.add(HeartstoneClient::init);
        }
    }
}
