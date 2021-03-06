package com.phylogeny.kaolinitetest.client.particle;

import com.phylogeny.kaolinitetest.block.BlockCauldron;

import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleSplash;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticleCauldronSplash extends ParticleSplash {
    private static final double MAX_HEIGHT = 0.15;

    protected ParticleCauldronSplash(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
    }

    @Override
    public void onUpdate() {
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        motionY -= particleGravity;
        moveEntity(motionX, motionY, motionZ);
        motionX *= 0.9800000190734863D;
        motionY *= 0.9800000190734863D;
        motionZ *= 0.9800000190734863D;

        double x = MathHelper.floor_double(posX);
        double y = MathHelper.floor_double(posY);
        double z = MathHelper.floor_double(posZ);
        boolean isCauldron = worldObj.getBlockState(new BlockPos(x, y, z)).getBlock() instanceof BlockCauldron;
        y = posY - y;
        if (isCauldron) {
            x = posX - x;
            z = posZ - z;
            if (y >= MAX_HEIGHT && x <= 0.9375 && x >= 0.0625 && z <= 0.9375 && z >= 0.0625) {
                isCollided = true;
                motionY = 0;
                motionX *= 1.1;
                motionZ *= 1.1;
            }
        }

        if (particleMaxAge-- <= 0)
            setExpired();

        if (isCollided) {
            motionX *= 0.699999988079071D;
            motionZ *= 0.699999988079071D;
            if (isCauldron && y > MAX_HEIGHT)
                moveEntity(0, MAX_HEIGHT - y, 0);
        }
    }

    @SideOnly(Side.CLIENT)
    public static class Factory implements IParticleFactory {
        @Override
        public Particle getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_) {
            return new ParticleCauldronSplash(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        }
    }

}