package net.mehvahdjukaar.heartstone.compat.platform;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.mehvahdjukaar.heartstone.Heartstone;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class TrinketsCompatImpl {

    public static List<ItemStack> getHeartstones(Player player) {
        TrinketComponent trinket = TrinketsApi.getTrinketComponent(player).orElse(null);
        if (trinket == null) return List.of();
        List<Tuple<SlotReference, ItemStack>> equipped = trinket.getEquipped(Heartstone.HEARTSTONE_ITEM.get());
        return equipped.stream().map(Tuple::getB).toList();
    }
}
