package net.mehvahdjukaar.heartstone.utils;

import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber
public class RenderersState {

    public static RenderersState INSTANCE;

    private final Map<RendererType, Boolean> settings = new HashMap<>();

    public void disableAll() {
        for (RendererType type : RendererType.values()) {
            settings.put(type, true);
        }

    }

    @SubscribeEvent
    public static void onServerStart(ServerStartingEvent event) {
        INSTANCE = new RenderersState();
        //DebugRenderersCommand.register(event.getCommandDispatcher());
        INSTANCE.disableAll();
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean get(RendererType type) {
       // settings.put(RendererType.PATHFINDING,false);
        return settings.get(type);
    }

    public void toggle(RendererType type) {
        settings.put(type, !settings.get(type));
    }

    public enum RendererType {
        BEE,
        POI,
        PATHFINDING,
        BEEHIVE,
        ENTITY_AI,
        RAID,
        STRUCTURE
    }
}
