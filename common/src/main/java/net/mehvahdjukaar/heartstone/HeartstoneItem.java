package net.mehvahdjukaar.heartstone;

import net.mehvahdjukaar.heartstone.compat.CurioCompat;
import net.mehvahdjukaar.heartstone.compat.TrinketsCompat;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.players.PlayerList;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.util.Unit;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
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
        return stack.getOrDefault(Heartstone.HEARTSTONE_ID.get(), null);
    }

    public static boolean isCracked(ItemStack stack) {
        return stack.has(Heartstone.CRACKED.get());
    }

    public static void crack(ItemStack stack, Entity entity) {
        stack.set(Heartstone.CRACKED.get(), Unit.INSTANCE);
        if (entity instanceof Player p) {
            if (p.getMainHandItem() == stack) {
                p.onEquippedItemBroken(stack.getItem(), EquipmentSlot.MAINHAND);
            }
            if (p.getOffhandItem() == stack) {
                p.onEquippedItemBroken(stack.getItem(), EquipmentSlot.OFFHAND);
            }
        }
        entity.playSound(SoundEvents.AMETHYST_CLUSTER_BREAK, 0.6f, 1.3f);
        entity.playSound(SoundEvents.VEX_HURT, 1.3f, 0.7f);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        if (isCracked(stack) && (level.getGameTime() + 1) % 40 == 0 && entity instanceof Player p) {
            if (getBoundPlayer(p, stack, false) != null) {
                stack.remove(Heartstone.CRACKED.get());
                entity.playSound(SoundEvents.AMETHYST_BLOCK_RESONATE, 0.8f, 0.7f);
                entity.playSound(SoundEvents.ALLAY_ITEM_TAKEN, 1.1f, 1.2f);
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player player, InteractionHand pHand) {
        ItemStack itemstack = player.getItemInHand(pHand);

        if (pLevel.isClientSide) {
            return InteractionResultHolder.success(itemstack);
        } else {
            player.awardStat(Stats.ITEM_USED.get(this));

            Player otherPlayer = getBoundPlayer(player, itemstack, true);

            if (otherPlayer != null) {
                NetworkHandler.sendHeartstoneParticles(player, otherPlayer);
            } else {
                pLevel.playSound(null, player,
                        SoundEvents.AMETHYST_BLOCK_FALL,
                        player.getSoundSource(), 0.6f, 0.7f);
            }

            player.getCooldowns().addCooldown(this, 60);
            return InteractionResultHolder.consume(itemstack);
        }
    }

    @Nullable
    public static Player getBoundPlayer(Player player, ItemStack itemstack, boolean sameDim) {
        Long id = getHeartstoneId(itemstack);
        if (id != null && player.level() instanceof ServerLevel serverLevel) {
            PlayerList players = serverLevel.getServer().getPlayerList();
            for (Player targetPlayer : players.getPlayers()) {
                if (arePlayersBound(player, itemstack, targetPlayer, sameDim)) {
                    return targetPlayer;
                }
            }
        }
        return null;
    }

    public static List<ItemStack> getAllHeartstones(Player player) {
        List<ItemStack> found = new ArrayList<>();
        player.getInventory().items.stream().filter(i -> i.getItem() instanceof HeartstoneItem).forEach(found::add);
        if (Heartstone.TRINKETS) {
            ItemStack s = TrinketsCompat.getHeartstone(player);
            if (!s.isEmpty()) found.add(s);
        }
        if (Heartstone.CURIO) {
            ItemStack s = CurioCompat.getHeartstone(player);
            if (!s.isEmpty()) found.add(s);
        }
        return found;
    }


    public static boolean arePlayersBound(Player pPlayer, ItemStack original, Player target, boolean sameDimension) {
        if (sameDimension && target.level().dimension() != pPlayer.level().dimension()) return false;
        if (target != pPlayer) {
            Long id = getHeartstoneId(original);
            if (id == null) return false;
            var inv = target.getInventory();
            for (int i = 0; i < inv.getContainerSize(); ++i) {
                ItemStack s = inv.getItem(i);
                if (hasMatchingId(id, s)) return true;
            }
            if (Heartstone.TRINKETS) {
                ItemStack s = TrinketsCompat.getHeartstone(target);
                if (!s.isEmpty() && hasMatchingId(id, s)) return true;
            }
            if (Heartstone.CURIO) {
                ItemStack s = CurioCompat.getHeartstone(target);
                return !s.isEmpty() && hasMatchingId(id, s);
            }
        }
        return false;
    }

    public static boolean hasMatchingId(Long id, ItemStack s) {
        if (s.getItem() instanceof HeartstoneItem) {
            Long other = getHeartstoneId(s);
            return other != null && other.equals(id);
        }
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        Long id = getHeartstoneId(stack);
        if (id != null) {
            tooltipComponents.add(Component.translatable("message.heartstone.id", id));
        }
        if (isCracked(stack)) {
            tooltipComponents.add(Component.translatable("message.heartstone.cracked"));
        }
    }

    @Override
    public void onCraftedBy(ItemStack pStack, Level pLevel, Player pPlayer) {
        super.onCraftedBy(pStack, pLevel, pPlayer);
        if (getHeartstoneId(pStack) == null && pLevel instanceof ServerLevel serverLevel) {
            pStack.set(Heartstone.HEARTSTONE_ID.get(), HeartstoneData.getNewId(serverLevel));
        }
    }
}