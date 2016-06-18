package com.phylogeny.kaolinitetest.init;

import com.phylogeny.kaolinitetest.reference.Reference;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class SoundsKaoliniteTest {
    public static SoundEvent CAULDRON_HANDLE;

    public static void registerSounds() {
        CAULDRON_HANDLE = registerSound(Reference.GROUP_ID + ":cauldron.handle");
    }

    private static SoundEvent registerSound(String soundName) {
        ResourceLocation soundNameResLoc = new ResourceLocation(soundName);
        if (SoundEvent.REGISTRY.containsKey(soundNameResLoc))
            return null;
        return GameRegistry.register(new SoundEvent(soundNameResLoc).setRegistryName(soundNameResLoc));
    }

}