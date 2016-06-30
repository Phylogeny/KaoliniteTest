package com.phylogeny.kaolinitetest.tileentity;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.phylogeny.kaolinitetest.KaoliniteTest;
import com.phylogeny.kaolinitetest.block.BlockCauldron;
import com.phylogeny.kaolinitetest.client.particle.ParticleCauldronDisolveDust;
import com.phylogeny.kaolinitetest.client.particle.ParticleCauldronFlame;
import com.phylogeny.kaolinitetest.client.particle.ParticleCauldronPrecipitate;
import com.phylogeny.kaolinitetest.client.particle.ParticleCauldronSmokeNormal;
import com.phylogeny.kaolinitetest.client.util.ClientHelper;
import com.phylogeny.kaolinitetest.init.FluidsKaoliniteTest;
import com.phylogeny.kaolinitetest.init.ItemsKaoliniteTest;
import com.phylogeny.kaolinitetest.init.SoundsKaoliniteTest;
import com.phylogeny.kaolinitetest.packet.PacketCauldronConsumeItem;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class TileEntityCauldron extends TileEntity implements ITickable, IFluidHandler, IItemHandler {
    public static final AxisAlignedBB AABB_WATER = new AxisAlignedBB(0.1875, 0.3125, 0.1875, 0.8125, 0.8125, 0.8125);
    private FluidTank tank = new FluidTank(Fluid.BUCKET_VOLUME);
    private ItemStack kaoliniteBall = null;
    private static final String DUST_DEATH_COUNTER = "dustDeathCounter";
    private List<DustBufferElement> dustBuffer = new ArrayList<DustBufferElement>();
    private float dustBufferTotalAlpha;
    private int tickCounter, countAluminum, countSilica, progressTicks, dustBufferCount;
    private boolean handleRebounded, handleHasEnergy, isBurning;
    private static final float TICKS_PRECIPITATION_TOTAL = 1200;
    private static final float TICKS_PRECIPITATION_DELAY = 600;
    private static final double MAXIMUM_PRECIPITATE_LEVEL = 0.0625;
    private static final float MAX_HANDLE_ROTATION = -0.43F;
    private float handleRotation = MAX_HANDLE_ROTATION;
//    private static final double LITERS_PER_MILIBUCKET = 0.01;
//    private static final double SECECONDS_PER_TICK = 0.05D;
//    private static final double KILOWATTS = 100.0;
//    private static final double SPECIFIC_HEAT_WATER = 4.19;
//    private static final double THERMAL_CONDUCTIVITY_WATER = 0.056;
    private static final double TEMP_LIMIT = 100.0;
    private static final double TEMP_SURROUNDING = 22.0;
    private double waterTemp = 22.0;

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return (T) this;
        return super.getCapability(capability, facing);
    }

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
        readFromNBT(s35PacketUpdateTileEntity.getNbtCompound());
        worldObj.markBlockRangeForRenderUpdate(pos, pos);
    }

    public boolean isBurning() {
        return isBurning;
    }

    public void setBurning(boolean isBurning) {
        this.isBurning = isBurning;
        tickCounter = 0;
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
        return getFluid() != null && getFluid().getFluid() == FluidsKaoliniteTest.kaolinitePrecursor;
    }

    public boolean isPureWater() {
        return countAluminum == 0 && countSilica == 0 && (getFluid() == null || getFluid().getFluid() == FluidRegistry.WATER);
    }

    public void setPureWater() {
        countAluminum = countSilica = 0;
        worldObj.notifyNeighborsOfStateChange(pos, blockType);
    }

    public double getWaterTemp() {
        return waterTemp;
    }

    public void setWaterTemp(double waterTemp) {
        this.waterTemp = waterTemp;
    }

    public double getWaterLevel()
    {
        return 0.3125 + (getFluidAmount() / (double) getCapacity()) * 0.5;
    }

    public AxisAlignedBB getWaterCollisionBox() {
        return new AxisAlignedBB(AABB_WATER.minX, AABB_WATER.minY, AABB_WATER.minZ, AABB_WATER.maxX, getWaterLevel(), AABB_WATER.maxZ);
    }
    
    public boolean hasSolidPrecipitate() {
        return getSolidPrecipitateLevel() > BlockCauldron.AABB_BASE.maxY;
    }

    public boolean hasMaximunSolidPrecipitate() {
        return getSolidPrecipitateLevel() == BlockCauldron.AABB_BASE.maxY + 0.001 + MAXIMUM_PRECIPITATE_LEVEL;
    }

    public double getSolidPrecipitateLevel() {
        return progressTicks <= TICKS_PRECIPITATION_DELAY ? -1 : BlockCauldron.AABB_BASE.maxY + 0.001 + (getPrecipitationProgressTicks() / TICKS_PRECIPITATION_TOTAL) * MAXIMUM_PRECIPITATE_LEVEL;
    }

    public int getPrecipitationProgressTicks() {
        return (int) (progressTicks - TICKS_PRECIPITATION_DELAY);
    }

    public int getProgressTicks() {
        return progressTicks;
    }

    public void setProgressTicks(int progressTicks) {
        this.progressTicks = progressTicks;
    }

    @Override
    public void update() {
        tickCounter++;
        if (worldObj.isRemote) {
            if (tickCounter > 1 && (handleRotation > MAX_HANDLE_ROTATION || handleHasEnergy))
                rotateHandle();
        }

        for (int i = 0; i < dustBuffer.size(); i++) {
            DustBufferElement bufferElement = dustBuffer.get(i);
            bufferElement.increasePrecipitateAlpha();
            if (bufferElement.tickRemaining == 0) {
                dustBuffer.remove(i);
                dustBufferCount -= bufferElement.amount;
                i--;
            }
        }

        IBlockState state = worldObj.getBlockState(getPos());
        if (!(state.getBlock() instanceof BlockCauldron))
            return;

        BlockCauldron cauldron = (BlockCauldron) state.getBlock();

        if (worldObj.isRemote && isBurning && (tickCounter >= 400 || worldObj.rand.nextInt(400) < tickCounter)) {
            IParticleFactory[] particles = new IParticleFactory[4];
            particles [0] = new ParticleCauldronFlame.Factory();
            for (int i = 1; i < 4; i++) {
                particles[i] = new ParticleCauldronSmokeNormal.Factory();
            }
            cauldron.spawnParticlesForLogs(worldObj, pos, null, 5, particles);
        }

        if (!isEmpty()) {
            if (isBurning) {
//                int waterAmount = (int) (1000 * (3 / (double) waterLevel));
//                waterTemp += (SECECONDS_PER_TICK * KILOWATTS) / (SPECIFIC_HEAT_WATER * waterAmount * LITERS_PER_MILIBUCKET);
                waterTemp += 0.133333333;
                if (waterTemp > TEMP_LIMIT)
                    waterTemp = TEMP_LIMIT;
            } else {
//                waterTemp = TEMP_SURROUNDING + ((waterTemp - TEMP_SURROUNDING) * Math.exp(-THERMAL_CONDUCTIVITY_WATER * (SECECONDS_PER_TICK / 60.0D)));
                waterTemp -= 0.0133333333;
                if (waterTemp < TEMP_SURROUNDING)
                    waterTemp = TEMP_SURROUNDING;
            }
        } else {
            waterTemp = TEMP_SURROUNDING;
        }

        if (isPrecursor() && waterTemp >= 80 && progressTicks < TICKS_PRECIPITATION_DELAY + TICKS_PRECIPITATION_TOTAL) {
            progressTicks++;
        }

        if (progressTicks == TICKS_PRECIPITATION_DELAY + TICKS_PRECIPITATION_TOTAL && isPrecursor()) {
            tank = new FluidTank(FluidRegistry.WATER, Fluid.BUCKET_VOLUME, Fluid.BUCKET_VOLUME);
            if (!worldObj.isRemote)
                kaoliniteBall = new ItemStack(ItemsKaoliniteTest.kaoliniteBall);
            setPureWater();
        }

        if (countAluminum == 7 && countSilica == 7 && dustBufferCount == 0) {
            tank = new FluidTank(FluidsKaoliniteTest.kaolinitePrecursor, Fluid.BUCKET_VOLUME, Fluid.BUCKET_VOLUME);
            countAluminum = countSilica = 0;
        }

        if (isPrecursor()) {
            if (worldObj.isRemote && progressTicks > TICKS_PRECIPITATION_DELAY && progressTicks < TICKS_PRECIPITATION_DELAY + TICKS_PRECIPITATION_TOTAL) {
                AxisAlignedBB box = TileEntityCauldron.AABB_WATER;
                Vec3d particlePos = new Vec3d(pos.getX() + Math.random() * (box.maxX - box.minX) + box.minX, pos.getY() +
                        Math.random() * (box.maxY - box.minY) + box.minY, pos.getZ() + Math.random() * (box.maxZ - box.minZ) + box.minZ);
                ClientHelper.spawnParticle(worldObj, particlePos, new ParticleCauldronPrecipitate.Factory());
            }
            return;
        }

        if (!isFull())
            return;
        if (!worldObj.isRemote && !isPrecursor() && !hasMaximunSolidPrecipitate()) {
            AxisAlignedBB waterBox = getWaterCollisionBox().offset(getPos());
            for (EntityItem entityItem : worldObj.getEntitiesWithinAABB(EntityItem.class, waterBox)) {
                consumeDust(entityItem);
                if (countAluminum == 7 && countSilica == 7)
                    break;
            }
        }
    }

    private void consumeDust(EntityItem entityItem) {
        ItemStack stack = entityItem.getEntityItem();
        if (stack != null && stack.getItem() != null) {
            if (stack.hasTagCompound() && stack.getTagCompound().hasKey(DUST_DEATH_COUNTER)) {
                int count = stack.getTagCompound().getInteger(DUST_DEATH_COUNTER) - 1;
                if (count > 0) {
                    stack.getTagCompound().setInteger(DUST_DEATH_COUNTER, count);
                } else {
                    entityItem.setDead();
                }
            } else {
                Vec3d dustPos = new Vec3d(entityItem.posX, entityItem.posY, entityItem.posZ);
                Vec3d dustMotion = new Vec3d(entityItem.motionX, entityItem.motionY, entityItem.motionZ);
                float dustWidth = entityItem.width;
                double dustMinY = entityItem.getEntityBoundingBox().minY;
                if (stack.getItem() == ItemsKaoliniteTest.aluminumDust && countAluminum < 7) {
                    int amount = Math.min(stack.stackSize, 7 - countAluminum);
                    decrementStack(stack, amount);
                    onItemConsumed(true, amount, dustPos, dustMotion, dustWidth, dustMinY);
                } else if (stack.getItem() == ItemsKaoliniteTest.silicaDust && countSilica < 7) {
                    int amount = Math.min(stack.stackSize, 7 - countSilica);
                    decrementStack(stack, amount);
                    onItemConsumed(false, amount, dustPos, dustMotion, dustWidth, dustMinY);
                }
            }
        }
    }

    private void decrementStack(ItemStack stack, int amount) {
        if (stack.stackSize > amount) {
            stack.stackSize -= amount;
        } else {
            if (!stack.hasTagCompound())
                stack.setTagCompound(new NBTTagCompound());
            stack.getTagCompound().setInteger(DUST_DEATH_COUNTER, 1);
        }
    }

    public void onItemConsumed(boolean isAluminum, int amount, Vec3d dustPos, Vec3d dustMotion, float dustWidth, double dustMinY) {
        dustBufferCount += amount;
        dustBuffer.add(new DustBufferElement(amount));
        dustBufferTotalAlpha += 0.06022408963585434173669467787115F * amount;
        if (isAluminum) {
            countAluminum += amount;
        } else {
            countSilica += amount;
        }

        if (worldObj.isRemote) {
            float f1 = MathHelper.floor_double(dustMinY);
            for (int i = 0; i < (1.0F + dustWidth * 20.0F) * amount; ++i) {
                ClientHelper.spawnParticle((new ParticleCauldronDisolveDust.Factory()).getEntityFX(0, worldObj, dustPos.xCoord,
                        TileEntityCauldron.AABB_WATER.offset(pos).maxY - 0.125, dustPos.zCoord, worldObj.rand.nextDouble() * -0.01 + 0.005, worldObj.rand.nextDouble() * -0.002, worldObj.rand.nextDouble() * -0.01 + 0.005, new int[0]));
            }
            for (int j = 0; j < 1.0F + dustWidth * 20.0F; ++j) {
                float f4 = (worldObj.rand.nextFloat() * 2.0F - 1.0F) * dustWidth;
                float f5 = (worldObj.rand.nextFloat() * 2.0F - 1.0F) * dustWidth;
                worldObj.spawnParticle(EnumParticleTypes.WATER_SPLASH, dustPos.xCoord + f4, f1 + 1.0F, dustPos.zCoord + f5,
                        dustMotion.xCoord, dustMotion.yCoord, dustMotion.zCoord, new int[0]);
            }
        } else {
            float f = MathHelper.sqrt_double(dustMotion.xCoord * dustMotion.xCoord * 0.20000000298023224D + dustMotion.yCoord * dustMotion.yCoord + dustMotion.zCoord * dustMotion.zCoord * 0.20000000298023224D) * 0.2F;
            if (f > 1.0F)
                f = 1.0F;
            worldObj.playSound((EntityPlayer)null, dustPos.xCoord, dustPos.yCoord, dustPos.zCoord, SoundEvents.ENTITY_GENERIC_SPLASH, SoundCategory.NEUTRAL, f, 1.0F + (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.4F);
            KaoliniteTest.packetNetwork.sendToAllAround(new PacketCauldronConsumeItem(pos, isAluminum, amount, dustPos, dustMotion, dustWidth, dustMinY),
                    new TargetPoint(worldObj.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 100));
        }
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

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        tank.writeToNBT(nbt);
        nbt.setInteger("countAluminum", countAluminum);
        nbt.setInteger("countSilica", countSilica);
        nbt.setDouble("waterTemp", waterTemp);
        nbt.setInteger("ticksProgress", progressTicks);
        nbt.setBoolean("isCauldronBurning", isBurning);
        NBTTagList nbttaglist = new NBTTagList();
        if (kaoliniteBall != null) {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setByte("Slot", (byte)0);
            kaoliniteBall.writeToNBT(nbttagcompound);
            nbttaglist.appendTag(nbttagcompound);
        }
        nbt.setTag("Items", nbttaglist);
        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        tank.readFromNBT(nbt);
        countAluminum = nbt.getInteger("countAluminum");
        countSilica = nbt.getInteger("countSilica");
        waterTemp = nbt.getDouble("waterTemp");
        progressTicks = nbt.getInteger("ticksProgress");
        isBurning = nbt.getBoolean("isCauldronBurning");
        NBTTagList nbttaglist = nbt.getTagList("Items", 10);
        if (nbttaglist.tagCount() == 1) {
            NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(0);
            if ((nbttagcompound.getByte("Slot") & 255) == 0) {
                kaoliniteBall = ItemStack.loadItemStackFromNBT(nbttagcompound);
            }
        }
    }

    public float getAlpha() {
        if (isPrecursor()) {
            /*
             * int startColor = (40 / 255.0F);
             * int stageCount = TICKS_TO_COMPLETION;
             * float incrimentColor = (255 - start) / (stageCount * 255.0F);
             * return progressTicks * incrimentColor;
             */
            return progressTicks < TICKS_PRECIPITATION_DELAY ? 0 : getPrecipitationProgressTicks() * 0.00070261437908496732026143790849673F;
        }
        /*
         * int startColor = (40 / 255.0F);
         * int stageCount = 14;
         * float incrimentColor = (255 - start) / (stageCount * 255.0F);
         * return 1 - (startColor + (countAluminum + countSilica) * incrimentColor - dustBufferTotalAlpha);
         */
        return 1 - (0.15686274509803921568627450980392F + (countAluminum + countSilica) * 0.06022408963585434173669467787115F - dustBufferTotalAlpha);
    }

    public float getHandleRotation() {
        return handleRotation;
    }

    public void liftHandle() {
        handleRotation = 0;
        handleHasEnergy = true;
        handleRebounded = false;
        tickCounter = 0;
    }

    private class DustBufferElement {
        private int amount, tickRemaining;

        public DustBufferElement(int amount) {
            this.amount = amount;
            tickRemaining = 40;
        }

        private void increasePrecipitateAlpha() {
            if (tickRemaining-- > 0) {
                dustBufferTotalAlpha -= amount * (215 / (14 * 255 * 40.0));
                if (dustBufferTotalAlpha < 0)
                    dustBufferTotalAlpha = 0;
            }
        }
    }

    @Nullable
    public FluidStack getFluid() {
        return tank.getFluid();
    }

    public int getFluidAmount() {
        return tank.getFluidAmount();
    }

    public int getCapacity() {
        return Fluid.BUCKET_VOLUME;
    }

    @Override
    public IFluidTankProperties[] getTankProperties() {
        return tank.getTankProperties();
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        if (hasMaximunSolidPrecipitate() || resource == null || resource.getFluid() == null
                || !((resource.getFluid() == FluidRegistry.WATER && isPureWater()) || (resource.getFluid() == FluidsKaoliniteTest.kaolinitePrecursor && (isPrecursor() || isPureWater()))))
            return 0;

        double fluidTemp = resource.getFluid().getTemperature() - 273.15D;
        int V2 = tank.fill(resource, doFill);
        if (doFill) {
            int V1 = tank.getFluidAmount();
            waterTemp = (V1 * waterTemp + V2 * fluidTemp) / (V1 + V2);
            if (resource.getFluid() == FluidsKaoliniteTest.kaolinitePrecursor && isPureWater()) {
                setCountAluminum(7);
                setCountSilica(7);
            }
        }
        return V2;
    }

    @Override
    @Nullable
    public FluidStack drain(int maxDrain, boolean doDrain) {
        if (preventDraining())
            return null;
        FluidStack result = tank.drain(maxDrain, doDrain);
        if (isEmpty())
            setPureWater();
        return result;
    }

    @Override
    @Nullable
    public FluidStack drain(FluidStack resource, boolean doDrain) {
        if (preventDraining())
            return null;
        return tank.drain(resource, doDrain);
    }

    private boolean preventDraining() {
        return !(isPrecursor() || isPureWater()) || (hasSolidPrecipitate() && !hasMaximunSolidPrecipitate());
    }

    public boolean isFull() {
        return tank.getFluidAmount() == tank.getCapacity();
    }

    public boolean isEmpty() {
        return tank.getFluidAmount() == 0;
    }

    @Override
    @Nullable
    public ItemStack getStackInSlot(int index) {
        return index == 0 ? kaoliniteBall : null;
    }

    public void clear() {
        kaoliniteBall = null;
        progressTicks = 0;
    }

    @Override
    public int getSlots() {
        return 1;
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        return null;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (slot != 0 || getFluidAmount() != 0 || kaoliniteBall == null || amount < 1)
            return null;

        if (simulate)
            return kaoliniteBall.copy();

        ItemStack stack = kaoliniteBall;
        clear();
        markDirty();
        return stack;
    }

}