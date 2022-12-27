package net.mehvahdjukaar.heartstone;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.players.PlayerList;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HeartstoneItem extends Item {

    public HeartstoneItem() {
        super(new Properties().tab(CreativeModeTab.TAB_MISC).rarity(Rarity.RARE).stacksTo(2));
    }

    @Override
    public boolean isFoil(ItemStack pStack) {
        return getId(pStack) != null;
    }

    @Nullable
    public Long getId(ItemStack stack) {
        var tag = stack.getTag();
        if (tag != null && tag.contains("Id")) return tag.getLong("Id");
        return null;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        if (pLevel.isClientSide) {
            return InteractionResultHolder.success(itemstack);
        } else {

            pPlayer.awardStat(Stats.ITEM_USED.get(this));

            Long id = getId(itemstack);
            boolean success = false;
            if (id != null && pLevel instanceof ServerLevel serverLevel) {
                PlayerList players = serverLevel.getServer().getPlayerList();
                for (Player p : players.getPlayers()) {
                    if (p.getLevel().dimension() == pLevel.dimension() && p != pPlayer) {
                        var inv = p.getInventory();
                        for (int i = 0; i < inv.getContainerSize(); ++i) {
                            ItemStack s = inv.getItem(i);
                            if (s.getItem() == this) {
                                var t = s.getTag();
                                if (t != null && t.getLong("Id") == id) {

                                    NetworkHandler.sendHeartstoneParticles(pPlayer, p);
                                    success = true;
                                    break;
                                }
                            }
                        }
                    }
                }
            }

            if (!success) {
                pLevel.playSound(null, pPlayer,
                        SoundEvents.AMETHYST_BLOCK_FALL,
                        pPlayer.getSoundSource(), 0.6f, 0.7f);
            }

            pPlayer.getCooldowns().addCooldown(this, 60);
            return InteractionResultHolder.consume(itemstack);
        }
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        Long id = getId(pStack);
        if (id != null) {
            pTooltipComponents.add(new TranslatableComponent("message.heartstone.id", id));
        }
    }

    @Override
    public void onCraftedBy(ItemStack pStack, Level pLevel, Player pPlayer) {
        super.onCraftedBy(pStack, pLevel, pPlayer);
        var tag = pStack.getOrCreateTag();
        if (!tag.contains("Id") && pLevel instanceof ServerLevel serverLevel) {
            tag.putLong("Id", HeartstoneData.getNewId(serverLevel));
        }
    }
}