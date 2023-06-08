package net.mehvahdjukaar.heartstone;

import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigBuilder;
import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

    public static Supplier<Integer> highlightColor = () -> 1;
    public static Supplier<Boolean> highlight = () -> false;
    public static Supplier<Integer> highlightDistance = () -> 1;

    public static void commonInit() {

        if (PlatHelper.getPlatform().isForge()) {
            ConfigBuilder config = ConfigBuilder.create(res("client"), ConfigType.CLIENT);
            config.push("highlight");
            highlightColor = config.comment("Highlight color")
                    .defineColor("highlight_color", 0xffE630BF);
            highlight = config.define("enabled", true);
            highlightDistance = config.comment("Distance at which the player highlight will take effect")
                    .define("distance", 20, 0, 10000);
            config.pop();

            config.buildAndRegister();
        }

        RegHelper.addItemsToTabsRegistration(Heartstone::addItemsToTabs);
    }

    private static void addItemsToTabs(RegHelper.ItemToTabEvent event) {
        event.addBefore(CreativeModeTabs.TOOLS_AND_UTILITIES, i->i.is(Items.COMPASS), HEARTSTONE.get());
    }

    public static void commonSetup() {
        NetworkHandler.registerMessages();
    }

    public static final Supplier<SoundEvent> HEARTSTONE_SOUND = RegHelper.registerSound(res("item.heartstone"));

    public static final Supplier<SimpleParticleType> HEARTSTONE_PARTICLE = RegHelper.registerParticle(res("heartstone_trail"));

    public static final Supplier<SimpleParticleType> HEARTSTONE_PARTICLE_EMITTER = RegHelper.registerParticle(res("heartstone_emitter"));

    public static final Supplier<Item> HEARTSTONE = RegHelper.registerItem(res("heartstone"), HeartstoneItem::new);

}
