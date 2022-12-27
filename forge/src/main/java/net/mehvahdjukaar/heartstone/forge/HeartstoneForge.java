package net.mehvahdjukaar.heartstone.forge;

import net.mehvahdjukaar.moonlight.api.platform.PlatformHelper;
import net.mehvahdjukaar.heartstone.Heartstone;
import net.mehvahdjukaar.heartstone.HeartstoneClient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

/**
 * Author: MehVahdJukaar
 */
@Mod(Heartstone.MOD_ID)
public class HeartstoneForge {

    public HeartstoneForge() {
        Heartstone.commonInit();

        if (PlatformHelper.getEnv().isClient()) {
            HeartstoneClient.init();
        }
        MinecraftForge.EVENT_BUS.register(this);
    }
    
}

