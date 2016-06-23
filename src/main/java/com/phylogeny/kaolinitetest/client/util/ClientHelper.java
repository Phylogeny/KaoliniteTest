package com.phylogeny.kaolinitetest.client.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ClientHelper {
    public static EntityPlayer getPlayer() {
        return Minecraft.getMinecraft().thePlayer;
    }

    public static void spawnParticle(World worldIn, Vec3d particlePos, IParticleFactory particleFactory) {
        Minecraft.getMinecraft().effectRenderer.addEffect(particleFactory.getEntityFX(0, worldIn, particlePos.xCoord, particlePos.yCoord, particlePos.zCoord, 0.0D, 0.0D, 0.0D, new int[0]));
    }

    public static void spawnParticle(Particle particle) {
        Minecraft.getMinecraft().effectRenderer.addEffect(particle);
    }

    public static void playSound(SoundEvent sound) {
        if (sound == null)
            return;
        Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(sound, 1.0F));
    }

}