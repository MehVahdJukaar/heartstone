package net.mehvahdjukaar.owls.entities;

import net.mehvahdjukaar.heartstone.Heartstone;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

import java.util.Random;

public enum OwlType {
    HORNED("horned"),
    SNOW("snow"),
    BARN("barn"),
    LITTLE("little"),
    BARRED("barred"),
    FISHER("fisher"),
    EAGLE("eagle"),
    MOON("moon"),
    DUO("duo");

    public final String name;
    public final ResourceLocation texture;
    public final ResourceLocation sleepingTexture;
    public final ResourceLocation eyesTexture;

    OwlType(String name) {
        this.name = name;
        this.texture = res("textures/entity/owl/" + name + ".png");
        this.sleepingTexture = res("textures/entity/owl/" + name + "_sleep.png");
        this.eyesTexture = res("textures/entity/owl/" + name + "_e.png");

    }
    public static ResourceLocation res(String name) {
        return new ResourceLocation("owlmod", name);
    }


    public static OwlType fromNBT(byte b) {
        return OwlType.values()[b % OwlType.values().length];
    }

    public static OwlType getOwlType(Biome biome, Random rand) {
        return OwlType.values()[rand.nextInt(OwlType.values().length)];
    }

}
