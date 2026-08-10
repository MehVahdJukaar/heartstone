package net.mehvahdjukaar.heartstone.compat;

import net.mehvahdjukaar.candlelight.api.PlatformImpl;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

// Trinkets is fabric only and ships intermediary mapped, so the actual lookup can't live in common
public class TrinketsCompat {

    @PlatformImpl
    public static List<ItemStack> getHeartstones(Player player) {
        throw new AssertionError();
    }
}
