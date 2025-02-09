package net.mehvahdjukaar.heartstone.neoforge;

import net.mehvahdjukaar.heartstone.Heartstone;
import net.mehvahdjukaar.heartstone.HeartstoneClient;
import net.mehvahdjukaar.moonlight.api.platform.ClientHelper;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.FireworkExplosion;
import net.minecraft.world.item.crafting.FireworkStarRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.FireworkShapeFactoryRegistry;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.crafting.CompoundIngredient;

import java.util.HashMap;

/**
 * Author: MehVahdJukaar
 */
@Mod(Heartstone.MOD_ID)
public class HeartstoneForge {

    public HeartstoneForge() {
        Heartstone.commonInit();
        PlatHelper.addCommonSetup(HeartstoneForge::setup);
        NeoForge.EVENT_BUS.register(HeartstoneForge.class);
        if (PlatHelper.getPhysicalSide().isClient()) {
            ClientHelper.addClientSetup(HeartstoneForge::clientSetup);
        }
    }

    public static final FireworkExplosion.Shape FIREWORK_SHAPE = FireworkExplosion.Shape.valueOf("HEARTSTONE_HEART");


    public static void setup() {
        FireworkStarRecipe.SHAPE_BY_ITEM = new HashMap<>(FireworkStarRecipe.SHAPE_BY_ITEM);
        FireworkStarRecipe.SHAPE_BY_ITEM.put(Items.AMETHYST_SHARD, FIREWORK_SHAPE);
        FireworkStarRecipe.SHAPE_INGREDIENT = CompoundIngredient.of(
                FireworkStarRecipe.SHAPE_INGREDIENT,
                Ingredient.of(Items.AMETHYST_SHARD));
    }

    public static void clientSetup() {
        FireworkShapeFactoryRegistry.register(FIREWORK_SHAPE, HeartstoneFirework::createParticles);
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Pre event) {
        HeartstoneClient.onClientTick();
    }

}

