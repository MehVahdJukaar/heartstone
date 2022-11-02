package net.mehvahdjukaar.heartstone;


import net.mehvahdjukaar.owls.OwlMod;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.players.PlayerList;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.List;


/**
 * Author: MehVahdJukaar
 */
@Mod(Heartstone.MOD_ID)
public class Heartstone {
    public static final String MOD_ID = "heartstone";

    public static ResourceLocation res(String name) {
        return new ResourceLocation(MOD_ID, name);
    }

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Heartstone.MOD_ID);
    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Heartstone.MOD_ID);
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Heartstone.MOD_ID);


    public Heartstone() {

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ITEMS.register(bus);
        PARTICLES.register(bus);
        SOUNDS.register(bus);
        bus.addListener(NetworkHandler::registerMessages);

        OwlMod.init(bus);
    }

    public static final RegistryObject<SoundEvent> HEARTSTONE_SOUND = SOUNDS.register("item.heartstone",
            () -> new SoundEvent(res("item.heartstone")));

    public static final RegistryObject<SimpleParticleType> HEARTSTONE_PARTICLE = PARTICLES.register("heartstone_trail",
            () -> new SimpleParticleType(true));

    public static final RegistryObject<SimpleParticleType> HEARTSTONE_PARTICLE_EMITTER = PARTICLES.register("heartstone_emitter",
            () -> new SimpleParticleType(true));

    public static final RegistryObject<Item> HEARTSTONE = ITEMS.register("heartstone", HeartstoneItem::new);


}
