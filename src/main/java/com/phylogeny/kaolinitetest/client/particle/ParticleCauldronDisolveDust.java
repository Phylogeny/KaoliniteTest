package com.phylogeny.kaolinitetest.client.particle;

import com.phylogeny.kaolinitetest.block.BlockCauldron;
import com.phylogeny.kaolinitetest.init.ModelRegistration;
import com.phylogeny.kaolinitetest.reference.Reference;
import com.phylogeny.kaolinitetest.tileentity.TileEntityCauldron;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticleCauldronDisolveDust extends Particle {
    private static final ResourceLocation PARTICLE_TEXTURES = new ResourceLocation("textures/particle/particles.png");
    
    protected ParticleCauldronDisolveDust(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        motionX = xSpeedIn;
        motionY = ySpeedIn;
        motionZ = zSpeedIn;
        particleTexture = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(Reference.GROUP_ID + ":blocks/disolve_dust");
        width = height = 1;
        particleMaxAge = 41;
        particleScale *= rand.nextFloat() * 0.6F + 0.2F;
        particleRed = particleGreen = particleBlue = 1.0F;
    }

    @Override
    public void onUpdate() {
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        moveEntity(motionX, motionY, motionZ);

        BlockPos pos = new BlockPos(posX, posY, posZ);
        IBlockState state = worldObj.getBlockState(pos);
        if (state.getBlock() instanceof BlockCauldron) {
            AxisAlignedBB waterBox = TileEntityCauldron.AABB_WATER.offset(pos).contract(0.06);
            if (posY <= waterBox.minY) {
                posY = waterBox.minY;
                motionY = 0;
            }
            if (posX >= waterBox.maxX || posX <= waterBox.minX) {
                posX = posX >= waterBox.maxX ? waterBox.maxX : waterBox.minX;
                motionX = 0;
            }
            if (posZ >= waterBox.maxZ || posZ <= waterBox.minZ) {
                posZ = posZ >= waterBox.maxZ ? waterBox.maxZ : waterBox.minZ;
                motionZ = 0;
            }
        } else {
            setExpired();
        }

        if (particleAge++ >= particleMaxAge)
            setExpired();
    }

    @Override
    public void renderParticle(VertexBuffer vertexbuffer, Entity entityIn, float partialTicks,
            float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        if (particleAge < 1)
            return;
        particleAlpha = 1 - (particleAge / (float) particleMaxAge);
        vertexbuffer.finishDrawing();
        vertexbuffer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
        Minecraft.getMinecraft().getTextureManager().bindTexture(ModelRegistration.CAULDRON_DISOLVE_DUST);
        float f4 = 0.1F * this.particleScale;
        float f5 = (float)(this.prevPosX + (this.posX - this.prevPosX) * partialTicks - interpPosX);
        float f6 = (float)(this.prevPosY + (this.posY - this.prevPosY) * partialTicks - interpPosY);
        float f7 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * partialTicks - interpPosZ);
        int i = this.getBrightnessForRender(partialTicks);
        int j = i >> 16 & 65535;
        int k = i & 65535;
        vertexbuffer.pos(f5 - rotationX * f4 - rotationXY * f4, f6 - rotationZ * f4, f7 - rotationYZ * f4 - rotationXZ * f4).tex(1, 1).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
        vertexbuffer.pos(f5 - rotationX * f4 + rotationXY * f4, f6 + rotationZ * f4, f7 - rotationYZ * f4 + rotationXZ * f4).tex(1, 0).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
        vertexbuffer.pos(f5 + rotationX * f4 + rotationXY * f4, f6 + rotationZ * f4, f7 + rotationYZ * f4 + rotationXZ * f4).tex(0, 0).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
        vertexbuffer.pos(f5 + rotationX * f4 - rotationXY * f4, f6 - rotationZ * f4, f7 + rotationYZ * f4 - rotationXZ * f4).tex(0, 1).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
        Tessellator.getInstance().draw();
        Minecraft.getMinecraft().getTextureManager().bindTexture(PARTICLE_TEXTURES);
        vertexbuffer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
    }

    @SideOnly(Side.CLIENT)
    public static class Factory implements IParticleFactory {
        @Override
        public Particle getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_) {
            return new ParticleCauldronDisolveDust(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        }
    }

}