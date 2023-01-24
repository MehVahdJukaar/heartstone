package net.mehvahdjukaar.heartstone.forge;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OutlineBufferSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;

public class HeartstoneClientImpl {

    private static WeakReference<Player> glowPlayer;
    private static int cooldown;

    public static void init() {
        MinecraftForge.EVENT_BUS.register(HeartstoneClientImpl.class);
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (cooldown != 0 && event.phase == TickEvent.Phase.START) {
            cooldown--;
            if (cooldown == 0) glowPlayer = null;
        }
    }

    public static void highlightPlayer(Player player) {
        glowPlayer = new WeakReference<>(player);
        cooldown = 5*20;
    }

    @Nullable
    public static Object getOutlinePlayer() {
        if (glowPlayer != null) return glowPlayer.get();
        return null;
    }
}
