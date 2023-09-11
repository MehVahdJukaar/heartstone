package net.mehvahdjukaar.heartstone;

import net.mehvahdjukaar.heartstone.compat.CurioCompat;
import net.mehvahdjukaar.heartstone.compat.TrinketsCompat;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.players.PlayerList;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class HeartstoneItem extends Item {

    public HeartstoneItem() {
        super(new Properties().rarity(Rarity.RARE).stacksTo(2));
    }

    @Override
    public boolean isFoil(ItemStack pStack) {
        return getHeartstoneId(pStack) != null;
    }

    @Nullable
    public static Long getHeartstoneId(ItemStack stack) {
        var tag = stack.getTag();
        if (tag != null && tag.contains("Id")) return tag.getLong("Id");
        return null;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player player, InteractionHand pHand) {
        ItemStack itemstack = player.getItemInHand(pHand);
        if (pLevel.isClientSide) {
            return InteractionResultHolder.success(itemstack);
        } else {

            player.awardStat(Stats.ITEM_USED.get(this));

            Long id = getHeartstoneId(itemstack);
            boolean success = false;
            if (id != null && pLevel instanceof ServerLevel serverLevel) {
                PlayerList players = serverLevel.getServer().getPlayerList();
                for (Player targetPlayer : players.getPlayers()) {
                    if (arePlayersBound(player, id, targetPlayer)) {
                        NetworkHandler.sendHeartstoneParticles(player, targetPlayer);
                        success = true;
                        break;
                    }
                }
            }

            if (!success) {
                pLevel.playSound(null, player,
                        SoundEvents.AMETHYST_BLOCK_FALL,
                        player.getSoundSource(), 0.6f, 0.7f);
            }

            player.getCooldowns().addCooldown(this, 60);
            return InteractionResultHolder.consume(itemstack);
        }
    }

    public static List<ItemStack> getAllHeartstones(Player player) {
        List<ItemStack> found = new ArrayList<>();
        player.getInventory().items.stream().filter(i -> i.getItem() instanceof HeartstoneItem).forEach(found::add);
        if(Heartstone.TRINKETS){
            ItemStack s = TrinketsCompat.getHeartstone(player);
            if(!s.isEmpty()) found.add(s);
        }
        if(Heartstone.CURIO){
            ItemStack s = CurioCompat.getHeartstone(player);
            if(!s.isEmpty()) found.add(s);
        }
        return found;
    }


    public static boolean arePlayersBound(Player pPlayer, ItemStack stack, Player target) {
        if (stack.getItem() instanceof HeartstoneItem hs) {
            Long id = getHeartstoneId(stack);
            if (id != null) {
                return hs.arePlayersBound(pPlayer, id, target);
            }
        }
        return false;
    }

    public boolean arePlayersBound(Player pPlayer, Long id, Player target) {
        if (target.level().dimension() == pPlayer.level().dimension() && target != pPlayer) {
            var inv = target.getInventory();
            for (int i = 0; i < inv.getContainerSize(); ++i) {
                ItemStack s = inv.getItem(i);
                if (hasMatchingId(id, s)) return true;
            }
            if(Heartstone.TRINKETS){
                ItemStack s = TrinketsCompat.getHeartstone(target);
                if(!s.isEmpty() && hasMatchingId(id, s)) return true;
            }
            if(Heartstone.CURIO){
                ItemStack s = CurioCompat.getHeartstone(target);
                return !s.isEmpty() && hasMatchingId(id, s);
            }
        }
        return false;
    }

    private boolean hasMatchingId(Long id, ItemStack s) {
        if (s.getItem() == this) {
            CompoundTag t = s.getTag();
            return t != null && t.getLong("Id") == id;
        }
        return false;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        Long id = getHeartstoneId(pStack);
        if (id != null) {
            pTooltipComponents.add(Component.translatable("message.heartstone.id", id));
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