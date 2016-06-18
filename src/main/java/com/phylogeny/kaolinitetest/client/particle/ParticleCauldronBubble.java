package com.phylogeny.kaolinitetest.client.particle;

import com.phylogeny.kaolinitetest.block.BlockCauldron;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleBubble;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ParticleCauldronBubble extends ParticleBubble {
    protected ParticleCauldronBubble(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
    }

    @Override
    public void onUpdate() {
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        motionY += 0.002D;
        moveEntity(motionX, motionY, motionZ);
        motionX *= 0.8500000238418579D;
        motionY *= 0.8500000238418579D;
        motionZ *= 0.8500000238418579D;

        BlockPos pos = new BlockPos(posX, posY, posZ);
        IBlockState state = worldObj.getBlockState(pos);
        if (state.getBlock() instanceof BlockCauldron) {
            AxisAlignedBB waterBox = BlockCauldron.AABB_WATER.offset(pos).expandXyz(-0.06);
            if (posY <= waterBox.minY) {
                posY = waterBox.minY;
                motionY = 0;
                motionX *= 0.699999988079071D;
                motionZ *= 0.699999988079071D;
            }
            if (posX >= waterBox.maxX || posX <= waterBox.minX) {
                posX = posX >= waterBox.maxX ? waterBox.maxX : waterBox.minX;
                motionX = 0;
            }
            if (posZ >= waterBox.maxZ || posZ <= waterBox.minZ) {
                posZ = posZ >= waterBox.maxZ ? waterBox.maxZ : waterBox.minZ;
                motionZ = 0;
            }
            if (particleMaxAge-- <= 0 || posY - MathHelper.floor_double(posY) >= ((BlockCauldron) state.getBlock()).getWaterCollisionBox(state).maxY - 0.0625) {
                setExpired();
            }
        } else {
            setExpired();
        }
    }

    @SideOnly(Side.CLIENT)
    public static class Factory implements IParticleFactory {
        @Override
        public Particle getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_) {
            return new ParticleCauldronBubble(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        }
    }

}