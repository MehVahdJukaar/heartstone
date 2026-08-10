package net.mehvahdjukaar.heartstone.compat;

import net.mehvahdjukaar.heartstone.HeartstoneItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import java.util.List;

public class CurioCompat {

    public static List<ItemStack> getHeartstones(Player player) {
        return CuriosApi.getCuriosHelper()
                .findCurios(player, i -> i.getItem() instanceof HeartstoneItem)
                .stream().map(SlotResult::stack).toList();
    }
}
