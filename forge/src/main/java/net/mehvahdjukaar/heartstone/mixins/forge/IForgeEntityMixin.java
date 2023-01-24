package net.mehvahdjukaar.heartstone.mixins.forge;

import net.mehvahdjukaar.heartstone.forge.HeartstoneClientImpl;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.extensions.IForgeEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LocalPlayer.class)
public abstract class IForgeEntityMixin implements IForgeEntity {
    //ugly
    // @Inject(method = "hasCustomOutlineRendering", at = @At("HEAD"), remap = false)
    // default void hasHeartstoneOutline(Player player, CallbackInfoReturnable<Boolean> cir) {
    //     if (HeartstoneClientImpl.getOutlinePlayer() == this) cir.setReturnValue(true);
    // }

    //this is terrible
    @Override
    public boolean hasCustomOutlineRendering(Player player) {
        if (HeartstoneClientImpl.getOutlinePlayer() == this) return true;
        return false;
    }
}
