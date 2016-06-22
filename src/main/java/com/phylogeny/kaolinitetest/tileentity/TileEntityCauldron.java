package com.phylogeny.kaolinitetest.tileentity;

import javax.annotation.Nullable;

import com.phylogeny.kaolinitetest.KaoliniteTest;
import com.phylogeny.kaolinitetest.block.BlockCauldron;
import com.phylogeny.kaolinitetest.client.helper.ClientHelper;
import com.phylogeny.kaolinitetest.client.particle.ParticleCauldronPrecipitate;
import com.phylogeny.kaolinitetest.client.particle.ParticleCauldronSmokeNormal;
import com.phylogeny.kaolinitetest.entity.EntityItemKaolinitePrecursor;
import com.phylogeny.kaolinitetest.init.BlocksKaoliniteTest;
import com.phylogeny.kaolinitetest.init.ItemsKaoliniteTest;
import com.phylogeny.kaolinitetest.init.SoundsKaoliniteTest;
import com.phylogeny.kaolinitetest.packet.PacketCauldronConsumeItem;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.ParticleFlame;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TileEntityCauldron extends TileEntity implements ITickable {
    private int tickCounter, bufferCounter, buffer, countAluminum, countSilica, progressTicks;
    private boolean handleRebounded, handleHasEnergy;
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

    public double getWaterTemp() {
        return waterTemp;
    }

    public void setWaterTemp(double waterTemp) {
        this.waterTemp = waterTemp;
    }

    public boolean hasSolidPrecipitate() {
        return getSolidPrecipitateLevel() == BlockCauldron.AABB_BASE.maxY;
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
        if (worldObj.isRemote && tickCounter > 1 && (handleRotation > MAX_HANDLE_ROTATION || handleHasEnergy)) {
            rotateHandle();
        }

        IBlockState state = worldObj.getBlockState(getPos());
        if (!(state.getBlock() instanceof BlockCauldron))
            return;

        BlockCauldron cauldron = (BlockCauldron) state.getBlock();
        int waterLevel = cauldron.getWaterLevel(state);

        if (worldObj.isRemote && cauldron == BlocksKaoliniteTest.cauldron_lit && (tickCounter >= 400 || worldObj.rand.nextInt(400) < tickCounter)) {
            IParticleFactory[] particles = new IParticleFactory[4];
            particles [0] = new ParticleFlame.Factory();
            for (int i = 1; i < 4; i++) {
                particles[i] = new ParticleCauldronSmokeNormal.Factory();
            }
            cauldron.spawnParticlesForLogs(worldObj, pos, null, 5, particles);
        }

        if (waterLevel > 0) {
            if (cauldron == BlocksKaoliniteTest.cauldron_lit) {
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

        if (progressTicks == TICKS_PRECIPITATION_DELAY + TICKS_PRECIPITATION_TOTAL) {
            setPureWater();
        }

        if (isPrecursor()) {
            if (worldObj.isRemote && progressTicks > TICKS_PRECIPITATION_DELAY
                    && progressTicks < TICKS_PRECIPITATION_DELAY + TICKS_PRECIPITATION_TOTAL) {
                AxisAlignedBB box = cauldron.AABB_WATER;
                Vec3d particlePos = new Vec3d(pos.getX() + Math.random() * (box.maxX - box.minX) + box.minX, pos.getY() +
                        Math.random() * (box.maxY - box.minY) + box.minY, pos.getZ() + Math.random() * (box.maxZ - box.minZ) + box.minZ);
                ClientHelper.spawnParticle(worldObj, particlePos, new ParticleCauldronPrecipitate.Factory());
            }
            buffer = bufferCounter = 0;
            return;
        }

        if (buffer > 0 && bufferCounter < 10)
            bufferCounter++;

        if (tickCounter % 10 != 0)
            return;

        buffer = bufferCounter = 0;
        if (worldObj.isRemote)
            return;

        if (waterLevel != 3)
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
        writeCauldronToNBT(nbt);
        return nbt;
    }

    public NBTTagCompound writeCauldronToNBT(NBTTagCompound nbt) {
        nbt.setInteger("countAluminum", countAluminum);
        nbt.setInteger("countSilica", countSilica);
        nbt.setDouble("waterTemp", waterTemp);
        nbt.setInteger("ticksProgress", progressTicks);
        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        readCauldronFromNBT(nbt);
    }

    public void readCauldronFromNBT(NBTTagCompound nbt) {
        countAluminum = nbt.getInteger("countAluminum");
        countSilica = nbt.getInteger("countSilica");
        waterTemp = nbt.getDouble("waterTemp");
        progressTicks = nbt.getInteger("ticksProgress");
    }

    public float getAlpha() {
        if (isPrecursor()) {
            /*
             * int startColor = (40 / 255.0F);
             * int stageCount = TICKS_TO_COMPLETION;
             * float incrimentColor = ((255 - start) / stageCount) / 255.0F;
             * return progressTicks * incrimentColor;
             */
            return progressTicks < TICKS_PRECIPITATION_DELAY ? 0 : getPrecipitationProgressTicks() * 0.00070261437908496732026143790849673F;
        }
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