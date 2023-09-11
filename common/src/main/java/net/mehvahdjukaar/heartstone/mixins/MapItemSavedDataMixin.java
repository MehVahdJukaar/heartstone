package net.mehvahdjukaar.heartstone.mixins;

import net.mehvahdjukaar.heartstone.HeartstoneItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MapItemSavedData.class)
public class MapItemSavedDataMixin {

    @Inject(method = "tickCarriedBy", at = @At(value = "HEAD"))
    public void updateHeartstoneMarker(Player player, ItemStack mapStack, CallbackInfo ci){
        HeartstoneItem.getAllHeartstones(player);
    }
}
