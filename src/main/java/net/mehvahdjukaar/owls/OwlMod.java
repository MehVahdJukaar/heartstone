package net.mehvahdjukaar.owls;

import net.mehvahdjukaar.heartstone.Heartstone;
import net.mehvahdjukaar.owls.entities.OwlEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class OwlMod {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, Heartstone.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Heartstone.MOD_ID);


    public static void init(IEventBus bus) {
        ENTITIES.register(bus);
        ITEMS.register(bus);
        bus.addListener(OwlMod::registerEntityAttributes);
    }


    public static void registerEntityAttributes(EntityAttributeCreationEvent event){
        event.put(OWL.get(), OwlEntity.setCustomAttributes().build());
    }

    public static final String OWL_NAME = "owl";

    public static final RegistryObject<EntityType<OwlEntity>> OWL = ENTITIES.register(OWL_NAME,()->
            EntityType.Builder.of(OwlEntity::new, MobCategory.MISC)
            .setShouldReceiveVelocityUpdates(true)
            .setTrackingRange(64)
            .setUpdateInterval(3)
            .sized(0.6f, 15/16f)
            .build(OWL_NAME));

    public static final RegistryObject<Item> OWL_SPAWN_EGG_ITEM = ITEMS.register(OWL_NAME + "_spawn_egg", () ->
            new ForgeSpawnEggItem(OWL, 0x32211a,0xa4935d,
                    new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

}
