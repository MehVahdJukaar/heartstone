package net.mehvahdjukaar.heartstone;

import com.mojang.serialization.Codec;
import net.mehvahdjukaar.moonlight.api.events.IDropItemOnDeathEvent;
import net.mehvahdjukaar.moonlight.api.events.MoonlightEventsHelper;
import net.mehvahdjukaar.moonlight.api.map.MapDataRegistry;
import net.mehvahdjukaar.moonlight.api.map.decoration.MLMapDecorationType;
import net.mehvahdjukaar.moonlight.api.map.decoration.MLMapMarker;
import net.mehvahdjukaar.moonlight.api.map.decoration.SimpleMapMarker;
import net.mehvahdjukaar.moonlight.api.misc.EventCalled;
import net.mehvahdjukaar.moonlight.api.misc.HolderRef;
import net.mehvahdjukaar.moonlight.api.misc.WorldSavedDataType;
import net.mehvahdjukaar.moonlight.api.platform.ClientHelper;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigBuilder;
import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigType;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.function.Supplier;

/**
 * Author: MehVahdJukaar
 */
public class Heartstone {

    public static final String MOD_ID = "heartstone";
    public static final Logger LOGGER = LogManager.getLogger();

    public static final boolean CURIO = PlatHelper.isModLoaded("curios");
    public static final boolean TRINKETS = PlatHelper.isModLoaded("trinkets");

    public static ResourceLocation res(String name) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
    }

    public static final Supplier<SoundEvent> HEARTSTONE_SOUND = RegHelper.registerSound(res("item.heartstone"));

    public static final Supplier<SimpleParticleType> HEARTSTONE_PARTICLE = RegHelper.registerParticle(res("heartstone_trail"));

    public static final Supplier<SimpleParticleType> HEARTSTONE_PARTICLE_EMITTER = RegHelper.registerParticle(res("heartstone_emitter"));

    public static final Supplier<DataComponentType<Long>> HEARTSTONE_ID = RegHelper.registerDataComponent(res("heartstone_id"),
            () -> DataComponentType.<Long>builder()
                    .persistent(Codec.LONG).networkSynchronized(ByteBufCodecs.VAR_LONG)
                    .build());


    public static final Supplier<DataComponentType<Unit>> CRACKED = RegHelper.registerDataComponent(res("cracked"),
            () -> DataComponentType.<Unit>builder()
                    .persistent(Unit.CODEC).networkSynchronized(StreamCodec.unit(Unit.INSTANCE))
                    .build());

    public static final Supplier<Item> HEARTSTONE_ITEM = RegHelper.registerItem(res("heartstone"), HeartstoneItem::new);

    public static final WorldSavedDataType<HeartstoneData> HEARTSTONE_DATA = RegHelper.registerWorldSavedData(
            res("ids"), HeartstoneData::create, () -> HeartstoneData.CODEC, null);

    public static final TagKey<BannerPattern> HEART_TAG = TagKey.create(
            Registries.BANNER_PATTERN, res("pattern_item/heart")
    );
    public static final Supplier<Item> HEART_PATTERN_ITEM = RegHelper.registerItem(res("heart_banner_pattern"),
            () -> new BannerPatternItem(HEART_TAG, new Item.Properties()
                    .rarity(Rarity.RARE)));


    public static final Supplier<Integer> HIGHLIGHT_COLOR;
    public static final Supplier<Boolean> HIGHLIGHT;
    public static final Supplier<Integer> HIGHLIGHT_DISTANCE;
    public static final Supplier<Integer> HIGHLIGHT_DURATION;

    static {
        ConfigBuilder config = ConfigBuilder.create(res("client"), ConfigType.CLIENT);
        config.push("highlight");
        // ARGB. The alpha byte is not optional here: leaving it out makes the config screen show a
        // fully transparent swatch
        HIGHLIGHT_COLOR = config.comment("Highlight color")
                .defineColor("color", 0xFFFF68CF);
        HIGHLIGHT = config.comment("Outline the bound player when you use a heartstone")
                .define("enabled", true);
        HIGHLIGHT_DISTANCE = config.comment("Distance at which the player highlight will take effect")
                .define("distance", 20, 0, 10000);
        HIGHLIGHT_DURATION = config.comment("How long the highlight lasts, in ticks")
                .define("duration", 5 * 20, 0, 10000);
        config.pop();

        config.build();
    }

    public static void commonInit() {

        NetworkHandler.init();

        if (PlatHelper.getPhysicalSide().isClient()) {
            HeartstoneClient.init();
            ClientHelper.registerOptionalTexturePack(res("heart_particles"), false);
        }

        RegHelper.addItemsToTabsRegistration(Heartstone::addItemsToTabs);

        MapDataRegistry.addDynamicServerMarkersEvent(Heartstone::getDynamicDecorations);

        MoonlightEventsHelper.addListener(Heartstone::onPlayerDeath, IDropItemOnDeathEvent.class);
    }

    public static final HolderRef<MLMapDecorationType<?, ?>> HEARTSTONE_MARKER =
            HolderRef.of(res("heartstone"), MapDataRegistry.MAP_DECORATION_REGISTRY_KEY);


    private static Set<MLMapMarker<?>> getDynamicDecorations(
            Player player, MapId mapId, MapItemSavedData data) {

        Set<MLMapMarker<?>> markers = new HashSet<>();
        Set<Player> alreadyMarked = new HashSet<>();

        // a heartstone is meant to be shared between 2 people, so each one contributes at most one marker
        for (ItemStack stack : HeartstoneItem.getAllHeartstones(player)) {
            Player boundPlayer = HeartstoneItem.getBoundPlayer(player, stack, true);
            if (boundPlayer != null && alreadyMarked.add(boundPlayer)) {
                markers.add(new SimpleMapMarker(HEARTSTONE_MARKER.getHolder(player.level()),
                        boundPlayer.getOnPos(), boundPlayer.getYRot(),
                        Optional.ofNullable(boundPlayer.getDisplayName())));
            }
        }
        return markers;
    }


    private static void addItemsToTabs(RegHelper.ItemToTabEvent event) {
        event.addAfter(CreativeModeTabs.INGREDIENTS, i -> i.is(Items.GUSTER_BANNER_PATTERN), HEART_PATTERN_ITEM.get());
        event.addBefore(CreativeModeTabs.TOOLS_AND_UTILITIES, i -> i.is(Items.COMPASS), HEARTSTONE_ITEM.get());
    }


    @EventCalled
    public static void onPlayerDeath(IDropItemOnDeathEvent event) {
        var p = event.getPlayer();
        if (event.isBeforeDrop()) {
            var list = HeartstoneItem.getAllHeartstones(p);
            for (var h : list) {
                Player target = HeartstoneItem.getBoundPlayer(p, h, false);
                if (target != null) {
                    Long id = HeartstoneItem.getHeartstoneId(h);
                    var targetList = HeartstoneItem.getAllHeartstones(target);
                    for (var th : targetList) {
                        if (HeartstoneItem.hasMatchingId(id, th)) {
                            HeartstoneItem.crack(th, target);
                        }
                    }
                }
            }
        }
    }
}
