package net.mehvahdjukaar.heartstone.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.mehvahdjukaar.heartstone.Heartstone;
import net.mehvahdjukaar.heartstone.HeartstoneClient;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;

public class HeartstoneFabric implements ModInitializer {

    @Override
    public void onInitialize() {

        Heartstone.commonInit();

        if (PlatHelper.getPhysicalSide().isClient()) {
            ClientTickEvents.END_CLIENT_TICK.register(a -> HeartstoneClient.onClientTick());
        }
    }
}
