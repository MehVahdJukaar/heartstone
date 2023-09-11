package net.mehvahdjukaar.heartstone.compat;

import net.mehvahdjukaar.heartstone.HeartstoneItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import java.util.List;

public class CurioCompat {

    public static ItemStack getHeartstone(Player player) {
        List<SlotResult> found = CuriosApi.getCuriosHelper().findCurios(player, i -> i.getItem() instanceof HeartstoneItem);
        if (!found.isEmpty()) return found.get(0).stack();
        return ItemStack.EMPTY;
    }
}
