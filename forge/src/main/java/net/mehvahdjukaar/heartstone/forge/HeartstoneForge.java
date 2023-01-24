package net.mehvahdjukaar.heartstone.forge;

import net.mehvahdjukaar.moonlight.api.platform.PlatformHelper;
import net.mehvahdjukaar.heartstone.Heartstone;
import net.mehvahdjukaar.heartstone.HeartstoneClient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * Author: MehVahdJukaar
 */
@Mod(Heartstone.MOD_ID)
public class HeartstoneForge {

    public HeartstoneForge() {
        Heartstone.commonInit();

        if (PlatformHelper.getEnv().isClient()) {
            HeartstoneClient.init();
            HeartstoneClientImpl.init();
        }
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
    }


    @SubscribeEvent
    public void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(Heartstone::commonSetup);
    }
}

