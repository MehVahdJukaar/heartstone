package net.mehvahdjukaar.heartstone;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.mehvahdjukaar.heartstone.HeartstoneItem;
import net.mehvahdjukaar.moonlight.api.block.VerticalSlabBlock;
import net.mehvahdjukaar.moonlight.api.item.WoodBasedBlockItem;
import net.mehvahdjukaar.moonlight.api.misc.Registrator;
import net.mehvahdjukaar.moonlight.api.platform.PlatformHelper;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.mehvahdjukaar.moonlight.api.set.BlockSetAPI;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.mehvahdjukaar.vsc.dynamicpack.ClientDynamicResourcesHandler;
import net.mehvahdjukaar.vsc.dynamicpack.ServerDynamicResourcesHandler;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Author: MehVahdJukaar
 */
public class Heartstone {

    public static final String MOD_ID = "heartstone";
    public static final Logger LOGGER = LogManager.getLogger();

    public static ResourceLocation res(String name) {
        return new ResourceLocation(MOD_ID, name);
    }

    public static void commonInit() {

        if (PlatformHelper.getEnv().isClient()) {

        }
    }

    public static final Supplier<SoundEvent> HEARTSTONE_SOUND = RegHelper.registerSound(res("item.heartstone"),
            () -> new SoundEvent(res("item.heartstone")));

    public static final Supplier<SimpleParticleType> HEARTSTONE_PARTICLE = RegHelper.registerParticle(res("heartstone_trail"));

    public static final Supplier<SimpleParticleType> HEARTSTONE_PARTICLE_EMITTER = RegHelper.registerParticle(res(("heartstone_emitter");

    public static final Supplier<Item> HEARTSTONE = RegHelper.registerItem(res("heartstone"), HeartstoneItem::new);

}
