package com.phylogeny.kaolinitetest.entity;

import com.phylogeny.kaolinitetest.KaoliniteTest;
import com.phylogeny.kaolinitetest.block.BlockCauldron;
import com.phylogeny.kaolinitetest.init.ItemsKaoliniteTest;
import com.phylogeny.kaolinitetest.packet.PacketCauldronWaterEffects;
import com.phylogeny.kaolinitetest.tileentity.TileEntityCauldron;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

public class EntityItemKaolinitePrecursor extends EntityItem {
    private boolean inCauldronWater;

    public EntityItemKaolinitePrecursor(World worldIn, EntityItem entityItem) {
        super(worldIn, entityItem.posX, entityItem.posY, entityItem.posZ);
        motionX = entityItem.motionX;
        motionY = entityItem.motionY;
        motionZ = entityItem.motionZ;
        NBTTagCompound nbt = new NBTTagCompound();
        entityItem.writeEntityToNBT(nbt);
        readEntityFromNBT(nbt);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        int x = MathHelper.floor_double(posX);
        int y = MathHelper.floor_double(posY);
        int z = MathHelper.floor_double(posZ);
        BlockPos pos = new BlockPos(x, y, z);
        IBlockState state = worldObj.getBlockState(pos);
        if (state.getBlock() instanceof BlockCauldron) {
            BlockCauldron cauldron = (BlockCauldron) state.getBlock();
            if (cauldron.getWaterCollisionBox(state).offset(pos).intersectsWith(getEntityBoundingBox())) {
                boolean preventItemPickup = false;
                TileEntity tileEntity = worldObj.getTileEntity(pos);
                if (tileEntity != null && tileEntity instanceof TileEntityCauldron) {
                    TileEntityCauldron cauldronTE = (TileEntityCauldron) tileEntity;
                    boolean isAluminum = getEntityItem().getItem() == ItemsKaoliniteTest.aluminumDust;
                    preventItemPickup = ((isAluminum && cauldronTE.getCountAluminum() != 7) || (!isAluminum && cauldronTE.getCountSilica() != 7))
                            && cauldron.getWaterLevel(state) == 3 && !cauldronTE.isPrecursor();
                }
                handleCauldronWaterMovement(preventItemPickup);
                return;
            }
        }
        if (inCauldronWater)
            setPickupDelay(5);
        inCauldronWater = false;
    }

    private void handleCauldronWaterMovement(boolean preventItemPickup) {
        if (!inCauldronWater) {
            float f = MathHelper.sqrt_double(motionX * motionX * 0.20000000298023224D + motionY * motionY + motionZ * motionZ * 0.20000000298023224D) * 0.2F;
            if (f > 1.0F) {
                f = 1.0F;
            }
            playSound(getSplashSound(), f, 1.0F + (rand.nextFloat() - rand.nextFloat()) * 0.4F);
            KaoliniteTest.packetNetwork.sendToAllAround(new PacketCauldronWaterEffects(posX, posY, posZ, motionX, motionY, motionZ,
                    getEntityBoundingBox().minY), new TargetPoint(dimension, posX, posY, posZ, 50));
            if (preventItemPickup)
                splitStack();
        }
        inCauldronWater = true;
        if (preventItemPickup) {
            setInfinitePickupDelay();
            setNoDespawn();
        }
    }

    private void splitStack() {
        if (!worldObj.isRemote && getEntityItem().stackSize > 1) {
            int n = getEntityItem().stackSize - 1;
            getEntityItem().stackSize = 1;
            EntityItem entityItem;
            for (int i = 0; i < n; i++) {
                entityItem = new EntityItem(worldObj, posX, posY, posZ, getEntityItem().copy());
                entityItem.setInfinitePickupDelay();
                entityItem.motionY = motionY;
                worldObj.spawnEntityInWorld(entityItem);
            }
        }
    }

}