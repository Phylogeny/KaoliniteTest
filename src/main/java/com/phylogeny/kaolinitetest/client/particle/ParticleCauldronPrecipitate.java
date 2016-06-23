package com.phylogeny.kaolinitetest.client.particle;

import java.util.Random;

import com.phylogeny.kaolinitetest.tileentity.TileEntityCauldron;

import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticleCauldronPrecipitate extends Particle {
    private final Random rand = new Random();
    private static final int[] COLOR_PROB = new int[]{1, 8, 15, 38, 111, 325, 642, 1024};
    private static final Vec3d[] COLORS = new Vec3d []{new Vec3d(0.701960784, 0.717647059, 0.764705882), new Vec3d(0.733333333, 0.749019608, 0.796078431),
        new Vec3d(0.858823529, 0.858823529, 0.858823529), new Vec3d(0.764705882, 0.780392157, 0.82745098), new Vec3d(0.752941176, 0.768627451, 0.815686275),
        new Vec3d(0.843137255, 0.858823529, 0.858823529), new Vec3d(0.807843137, 0.823529412, 0.858823529), new Vec3d(0.780392157, 0.796078431, 0.843137255)};

    protected ParticleCauldronPrecipitate(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);
        motionX = motionZ = 0;
        motionY = -0.01;

        int n = rand.nextInt(1024);
        for (int i = 0; i < COLOR_PROB.length; i++)
        {
            if (n < COLOR_PROB[i]) {
                Vec3d color = COLORS[i];
                particleRed = (float) color.xCoord;
                particleGreen = (float) color.yCoord;
                particleBlue = (float) color.zCoord;
                break;
            }
        }

        particleAlpha = 0;
        setParticleTextureIndex(0);
        particleMaxAge = Integer.MAX_VALUE;
        particleScale = rand.nextFloat() * 0.25F + 0.75F;
    }

    @Override
    public void onUpdate() {
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        motionX = motionZ = 0;
        moveEntity(0, motionY, 0);

        if (particleAlpha < 1)
            particleAlpha += 0.1;

        double x = MathHelper.floor_double(posX);
        double y = MathHelper.floor_double(posY);
        double z = MathHelper.floor_double(posZ);
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity tileEntity = worldObj.getTileEntity(pos);
        if (tileEntity == null || !(tileEntity instanceof TileEntityCauldron)) {
            setExpired();
            return;
        }

        TileEntityCauldron cauldronTE = (TileEntityCauldron) tileEntity;
        if (cauldronTE.getProgressTicks() == 0)
            setExpired();

        if (posY <= cauldronTE.getSolidPrecipitateLevel() + 0.05 + pos.getY()) {
            motionY = 0;
        }
    }

    @Override
    public void moveEntity(double x, double y, double z) {
        setEntityBoundingBox(getEntityBoundingBox().offset(0, y, 0));
        resetPositionToBB();
    }

    @SideOnly(Side.CLIENT)
    public static class Factory implements IParticleFactory {
        @Override
        public Particle getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_) {
            return new ParticleCauldronPrecipitate(worldIn, xCoordIn, yCoordIn, zCoordIn);
        }
    }

}