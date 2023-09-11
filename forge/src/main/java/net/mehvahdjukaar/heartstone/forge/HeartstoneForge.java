package net.mehvahdjukaar.heartstone.forge;

import net.mehvahdjukaar.heartstone.Heartstone;
import net.mehvahdjukaar.heartstone.HeartstoneClient;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Author: MehVahdJukaar
 */
@Mod(Heartstone.MOD_ID)
public class HeartstoneForge {

    public HeartstoneForge() {
        Heartstone.commonInit();

        if (PlatHelper.getPhysicalSide().isClient()) {
            MinecraftForge.EVENT_BUS.register(HeartstoneForge.class);
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            HeartstoneClient.onClientTick();
        }
    }

}

