package com.phylogeny.kaolinitetest.tileentity;

import javax.annotation.Nullable;

import com.phylogeny.kaolinitetest.KaoliniteTest;
import com.phylogeny.kaolinitetest.block.BlockCauldron;
import com.phylogeny.kaolinitetest.client.helper.ClientHelper;
import com.phylogeny.kaolinitetest.entity.EntityItemKaolinitePrecursor;
import com.phylogeny.kaolinitetest.init.ItemsKaoliniteTest;
import com.phylogeny.kaolinitetest.init.SoundsKaoliniteTest;
import com.phylogeny.kaolinitetest.packet.PacketCauldronConsumeItem;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TileEntityCauldron extends TileEntity implements ITickable {
    private int tickCounter, bufferCounter, buffer, countAluminum, countSilica;
    private boolean handleRebounded, handleHasEnergy;
    private static final float MAX_HANDLE_ROTATION = -0.43F;
    private float handleRotation = MAX_HANDLE_ROTATION;

    public static void register() {
        GameRegistry.registerTileEntity(TileEntityCauldron.class, "cauldron");
    }

    @Override
    public boolean shouldRenderInPass(int pass) {
        return true;
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(pos, 0, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager networkManager, SPacketUpdateTileEntity s35PacketUpdateTileEntity) {
        super.onDataPacket(networkManager, s35PacketUpdateTileEntity);
        readFromNBT(s35PacketUpdateTileEntity.getNbtCompound());
        worldObj.markBlockRangeForRenderUpdate(pos, pos);
    }

    public int getCountAluminum() {
        return countAluminum;
    }

    public void setCountAluminum(int countAluminum) {
        this.countAluminum = countAluminum;
    }

    public int getCountSilica() {
        return countSilica;
    }

    public void setCountSilica(int countSilica) {
        this.countSilica = countSilica;
    }

    public boolean isPrecursor() {
        return countAluminum == 7 && countSilica == 7;
    }

    public boolean isPureWater() {
        return countAluminum == 0 && countSilica == 0;
    }

    public void setPureWater() {
        countAluminum = countSilica = 0;
    }

    @Override
    public void update() {
        if (worldObj.isRemote && tickCounter > 1 && (handleRotation > MAX_HANDLE_ROTATION || handleHasEnergy)) {
            rotateHandle();
        }

        if (countAluminum == 7 && countSilica == 7)
            return;

        if (buffer > 0 && bufferCounter < 10)
            bufferCounter++;

        if (tickCounter++ % 10 != 0)
            return;

        buffer = bufferCounter = 0;
        if (worldObj.isRemote)
            return;

        IBlockState state = worldObj.getBlockState(getPos());
        if (!(state.getBlock() instanceof BlockCauldron))
            return;

        BlockCauldron cauldron = (BlockCauldron) state.getBlock();
        if (cauldron.getWaterLevel(state) != 3)
            return;

        AxisAlignedBB waterBox = cauldron.getWaterCollisionBox(state).offset(getPos());
        for (EntityItemKaolinitePrecursor entityItem : worldObj.getEntitiesWithinAABB(EntityItemKaolinitePrecursor.class, waterBox)) {
            if (consumeItem(entityItem))
                break;
        }
        if (countAluminum == 7 || countSilica == 7) {
            cauldron.allowItemPickup(worldObj, pos, state, countAluminum == 7, countSilica == 7);
        }
    }

    private boolean consumeItem(EntityItemKaolinitePrecursor entityItem) {
        ItemStack stack = entityItem.getEntityItem();
        if (stack != null) {
            if (stack.getItem() == ItemsKaoliniteTest.aluminumDust && countAluminum < 7) {
                entityItem.setDead();
                if (!worldObj.isRemote)
                    onItemConsumed(true);
                return true;
            }
            if (stack.getItem() == ItemsKaoliniteTest.silicaDust && countSilica < 7) {
                entityItem.setDead();
                if (!worldObj.isRemote)
                    onItemConsumed(false);
                return true;
            }
        }
        return false;
    }

    private void rotateHandle() {
        boolean wasLifted = handleRotation > MAX_HANDLE_ROTATION;
        handleRotation -= 0.03999999910593033;
        handleRotation *= 4;
        if (handleRotation <= MAX_HANDLE_ROTATION) {
            handleRotation = MAX_HANDLE_ROTATION;
            if (handleHasEnergy) {
                if (wasLifted && !handleRebounded) {
                    handleRebounded = true;
                    ClientHelper.playSound(SoundsKaoliniteTest.CAULDRON_HANDLE);
                } else {
                    handleRotation += 0.1F;
                    handleHasEnergy = false;
                }
            }
        }
    }

    public void onItemConsumed(boolean isAluminum) {
        buffer++;
        tickCounter = 1;
        if (isAluminum) {
            countAluminum++;
        } else {
            countSilica++;
        }

        if (!worldObj.isRemote) {
            KaoliniteTest.packetNetwork.sendToAllAround(new PacketCauldronConsumeItem(pos, isAluminum),
                    new TargetPoint(worldObj.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 50));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("countAluminum", countAluminum);
        nbt.setInteger("countSilica", countSilica);
        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        countAluminum = nbt.getInteger("countAluminum");
        countSilica = nbt.getInteger("countSilica");
    }

    public float getAlpha() {
        /*
         * int startColor = (40 / 255.0F);
         * int stageCount = 14;
         * float incrimentColor = ((255 - start) / stageCount) / 255.0F;
         * float interIncrimentColor = incrimentColor / 10.0F;
         * return 1 - (startColor + (countAluminum + countSilica - buffer) * incrimentColor + bufferCounter * interIncrimentColor);
         */
        return 1 - (0.15686274509803921568627450980392F + (countAluminum + countSilica - buffer) * 0.06022408963585434173669467787115F + bufferCounter * 0.00602240896358543417366946778712F);
    }

    public float getHandleRotation() {
        return handleRotation;
    }

    public void liftHandle() {
        handleRotation = 0;
        handleHasEnergy = true;
        tickCounter = 0;
    }

}