package net.mehvahdjukaar.heartstone.forge;

import net.mehvahdjukaar.heartstone.Heartstone;
import net.mehvahdjukaar.heartstone.HeartstoneClient;
import net.mehvahdjukaar.moonlight.api.platform.ClientHelper;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.minecraft.world.item.FireworkRocketItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.FireworkStarRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.client.FireworkShapeFactoryRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.CompoundIngredient;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.List;

/**
 * Author: MehVahdJukaar
 */
@Mod(Heartstone.MOD_ID)
public class HeartstoneForge {

    public HeartstoneForge() {
        Heartstone.commonInit();
        PlatHelper.addCommonSetup(HeartstoneForge::setup);
        MinecraftForge.EVENT_BUS.register(HeartstoneForge.class);
        if (PlatHelper.getPhysicalSide().isClient()) {
            ClientHelper.addClientSetup(HeartstoneForge::clientSetup);
        }
    }

    private static final FireworkRocketItem.Shape FIREWORK_SHAPE =
            FireworkRocketItem.Shape.create("heartstone",
                    FireworkRocketItem.Shape.values().length, "heartstone");

    public static void setup(){
        FireworkStarRecipe.SHAPE_BY_ITEM = new HashMap<>(FireworkStarRecipe.SHAPE_BY_ITEM );
        FireworkStarRecipe.SHAPE_BY_ITEM.put(Items.AMETHYST_SHARD, FIREWORK_SHAPE);
        FireworkStarRecipe.SHAPE_INGREDIENT = CompoundIngredient.of(
                FireworkStarRecipe.SHAPE_INGREDIENT,
                Ingredient.of(Items.AMETHYST_SHARD));
    }

    public static void clientSetup(){
        FireworkShapeFactoryRegistry.register(FIREWORK_SHAPE, HeartstoneFirework::createParticles);
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START && event.type == TickEvent.Type.CLIENT) {
            HeartstoneClient.onClientTick();
        }
    }

}

