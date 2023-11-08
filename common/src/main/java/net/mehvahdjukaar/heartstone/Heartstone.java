package net.mehvahdjukaar.heartstone;

import net.mehvahdjukaar.moonlight.api.events.IDropItemOnDeathEvent;
import net.mehvahdjukaar.moonlight.api.events.MoonlightEventsHelper;
import net.mehvahdjukaar.moonlight.api.map.MapDataRegistry;
import net.mehvahdjukaar.moonlight.api.map.markers.MapBlockMarker;
import net.mehvahdjukaar.moonlight.api.map.type.MapDecorationType;
import net.mehvahdjukaar.moonlight.api.misc.DataObjectReference;
import net.mehvahdjukaar.moonlight.api.misc.EventCalled;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigBuilder;
import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigType;
import net.mehvahdjukaar.moonlight.core.map.MapDataInternal;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Author: MehVahdJukaar
 */
public class Heartstone {

    public static final String MOD_ID = "heartstone";
    public static final Logger LOGGER = LogManager.getLogger();

    public static final boolean CURIO = PlatHelper.isModLoaded("curio");
    public static final boolean TRINKETS = PlatHelper.isModLoaded("trinkets");

    public static ResourceLocation res(String name) {
        return new ResourceLocation(MOD_ID, name);
    }

    public static final Supplier<Integer> HIGHLIGHT_COLOR;
    public static final Supplier<Boolean> HIGHLIGHT;
    public static final Supplier<Integer> HIGHLIGHT_DISTANCE;
    public static final Supplier<Integer> HIGHLIGHT_DURATION;

    static {
        ConfigBuilder config = ConfigBuilder.create(res("client"), ConfigType.CLIENT);
        config.push("highlight");
        HIGHLIGHT_COLOR = config.comment("Highlight color")
                .defineColor("color", 0xFF68CF);
        HIGHLIGHT = config.define("enabled", true);
        HIGHLIGHT_DISTANCE = config.comment("Distance at which the player highlight will take effect")
                .define("distance", 20, 0, 10000);
        HIGHLIGHT_DURATION = config
                .define("duration", 5 * 20, 0, 10000);
        config.pop();

        config.buildAndRegister();
    }

    public static void commonInit() {

        PlatHelper.addCommonSetup(Heartstone::commonSetup);

        if (PlatHelper.getPhysicalSide().isClient()) {
            HeartstoneClient.init();
        }

        RegHelper.addItemsToTabsRegistration(Heartstone::addItemsToTabs);

        MapDataRegistry.addDynamicServerMarkersEvent(Heartstone::getDynamicDecorations);

        MoonlightEventsHelper.addListener(Heartstone::onPlayerDeath, IDropItemOnDeathEvent.class);
    }

    public static final DataObjectReference<MapDecorationType<?, ?>> HEARTSTONE_MARKER =
            new DataObjectReference<>(res("heartstone"), MapDataInternal.KEY);

    private static Set<MapBlockMarker<?>> getDynamicDecorations(
            Player player, int mapId, MapItemSavedData data) {

        List<ItemStack> list = HeartstoneItem.getAllHeartstones(player);
        List<Player> visiblePlayers = new ArrayList<>();
        var iterator = new ArrayList<>(player.level().getServer().getPlayerList().getPlayers()).iterator();

        for (var i : list) {
            boolean found = false;
            while (iterator.hasNext() && !found) {
                Player targetPlayer = iterator.next();
                if (((HeartstoneItem) i.getItem()).arePlayersBound(player, i, targetPlayer, true)) {
                    visiblePlayers.add(targetPlayer);
                    iterator.remove(); // Remove the player from the list
                    //we break as heartstones are meant to eb used only by 2
                    found = true;
                }
            }
        }
        Set<MapBlockMarker<?>> markers = new HashSet<>();

        for (var p : visiblePlayers) {
            MapBlockMarker<?> marker = HEARTSTONE_MARKER.get().createEmptyMarker();
            marker.setPos(p.getOnPos());
            marker.setRotation((int) p.getYRot());
            marker.setName(p.getDisplayName());
            markers.add(marker);
        }
        return markers;
    }


    private static void addItemsToTabs(RegHelper.ItemToTabEvent event) {
        event.addBefore(CreativeModeTabs.TOOLS_AND_UTILITIES, i -> i.is(Items.COMPASS), HEARTSTONE_ITEM.get());
    }

    public static void commonSetup() {
        NetworkHandler.registerMessages();
    }

    public static final Supplier<SoundEvent> HEARTSTONE_SOUND = RegHelper.registerSound(res("item.heartstone"));

    public static final Supplier<SimpleParticleType> HEARTSTONE_PARTICLE = RegHelper.registerParticle(res("heartstone_trail"));

    public static final Supplier<SimpleParticleType> HEARTSTONE_PARTICLE_EMITTER = RegHelper.registerParticle(res("heartstone_emitter"));

    public static final Supplier<Item> HEARTSTONE_ITEM = RegHelper.registerItem(res("heartstone"), HeartstoneItem::new);


    @EventCalled
    public static void onPlayerDeath(IDropItemOnDeathEvent event) {
        var p = event.getPlayer();
        if(event.isBeforeDrop()) {
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
