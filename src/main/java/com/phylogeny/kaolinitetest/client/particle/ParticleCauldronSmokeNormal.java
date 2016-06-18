package com.phylogeny.kaolinitetest.client.particle;

import com.phylogeny.kaolinitetest.block.BlockModCauldron;

import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleSmokeNormal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ParticleCauldronSmokeNormal extends ParticleSmokeNormal {
    private static final double MAX_HEIGHT = 0.1875;

    protected ParticleCauldronSmokeNormal(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, float scale) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, scale);
    }

    @Override
    public void onUpdate() {
        double x = MathHelper.floor_double(posX);
        double y = MathHelper.floor_double(posY);
        double z = MathHelper.floor_double(posZ);
        BlockPos pos = new BlockPos(x, y, z);
        if (worldObj.getBlockState(pos).getBlock() instanceof BlockModCauldron) {
            x = posX - x;
            double y2 = posY - y;
            z = posZ - z;
            if (y2 >= MAX_HEIGHT && x <= 0.875 && x >= 0.125 && z <= 0.875 && z >= 0.125) {
                isCollided = true;
                motionY = 0;
                motionX *= 1.1;
                motionZ *= 1.1;
            }
        }
        super.onUpdate();
        if (posY - y > MAX_HEIGHT)
            posY = y + MAX_HEIGHT;
    }

    @SideOnly(Side.CLIENT)
    public static class Factory implements IParticleFactory {
        @Override
        public Particle getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_) {
            return new ParticleCauldronSmokeNormal(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, 1.0F);
        }
    }
}