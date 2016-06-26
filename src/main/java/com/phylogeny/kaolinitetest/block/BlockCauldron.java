package com.phylogeny.kaolinitetest.block;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.phylogeny.kaolinitetest.client.particle.ParticleCauldronSmokeLarge;
import com.phylogeny.kaolinitetest.client.particle.ParticleCauldronSmokeNormal;
import com.phylogeny.kaolinitetest.client.particle.ParticleCauldronSplash;
import com.phylogeny.kaolinitetest.client.util.ClientHelper;
import com.phylogeny.kaolinitetest.init.BlocksKaoliniteTest;
import com.phylogeny.kaolinitetest.init.FluidsKaoliniteTest;
import com.phylogeny.kaolinitetest.init.ItemsKaoliniteTest;
import com.phylogeny.kaolinitetest.tileentity.TileEntityCauldron;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.ParticleFlame;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBanner;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionUtils;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BlockCauldron extends Block {
    private final boolean isBurning;
    public static final PropertyInteger LEVEL = PropertyInteger.create("level", 0, 3);
    protected static final AxisAlignedBB AABB_LEG_1_SEGMENT_1 = new AxisAlignedBB(0.125, 0.1875, 0.1875, 0.1875, 0.25, 0.3125);
    protected static final AxisAlignedBB AABB_LEG_1_SEGMENT_2 = new AxisAlignedBB(0.125, 0.1875, 0.125, 0.3125, 0.25, 0.1875);
    protected static final AxisAlignedBB AABB_LEG_2_SEGMENT_1 = new AxisAlignedBB(0.125, 0.1875, 0.6875, 0.1875, 0.25, 0.8125);
    protected static final AxisAlignedBB AABB_LEG_2_SEGMENT_2 = new AxisAlignedBB(0.125, 0.1875, 0.8125, 0.3125, 0.25, 0.875);
    protected static final AxisAlignedBB AABB_LEG_3_SEGMENT_1 = new AxisAlignedBB(0.8125, 0.1875, 0.6875, 0.875, 0.25, 0.8125);
    protected static final AxisAlignedBB AABB_LEG_3_SEGMENT_2 = new AxisAlignedBB(0.6875, 0.1875, 0.8125, 0.875, 0.25, 0.875);
    protected static final AxisAlignedBB AABB_LEG_4_SEGMENT_1 = new AxisAlignedBB(0.8125, 0.1875, 0.1875, 0.875, 0.25, 0.3125);
    protected static final AxisAlignedBB AABB_LEG_4_SEGMENT_2 = new AxisAlignedBB(0.6875, 0.1875, 0.125, 0.875, 0.25, 0.1875);
    public static final AxisAlignedBB AABB_BASE = new AxisAlignedBB(0.125, 0.25, 0.125, 0.875, 0.3125, 0.875);
    protected static final AxisAlignedBB AABB_WALL_1 = new AxisAlignedBB(0.125, 0.3125, 0.1875, 0.1875, 0.875, 0.875);
    protected static final AxisAlignedBB AABB_WALL_2 = new AxisAlignedBB(0.8125, 0.3125, 0.125, 0.875, 0.875, 0.8125);
    protected static final AxisAlignedBB AABB_WALL_3 = new AxisAlignedBB(0.125, 0.3125, 0.125, 0.8125, 0.875, 0.1875);
    protected static final AxisAlignedBB AABB_WALL_4 = new AxisAlignedBB(0.1875, 0.3125, 0.8125, 0.875, 0.875, 0.875);
    protected static final AxisAlignedBB AABB_LOG_1 = new AxisAlignedBB(0.40625, -0.000625, 0.0625, 0.59375, 0.186875, 0.9375);
    protected static final AxisAlignedBB AABB_LOG_3 = new AxisAlignedBB(0.0625, 0.0, 0.375, 0.9375, 0.1875, 0.5625);
    protected static final AxisAlignedBB AABB_WOOD = new AxisAlignedBB(0.0625, 0.0, 0.0625, 0.9375, 0.1875, 0.9375);
    protected static final AxisAlignedBB[] BOXES = new AxisAlignedBB[]{AABB_LEG_1_SEGMENT_1, AABB_LEG_1_SEGMENT_2, AABB_LEG_2_SEGMENT_1, AABB_LEG_2_SEGMENT_2, AABB_LEG_3_SEGMENT_1,
        AABB_LEG_3_SEGMENT_2, AABB_LEG_4_SEGMENT_1, AABB_LEG_4_SEGMENT_2, AABB_BASE, AABB_WALL_1, AABB_WALL_2, AABB_WALL_3, AABB_WALL_4, AABB_WOOD};
    public static final AxisAlignedBB AABB_WATER = new AxisAlignedBB(0.1875, 0.3125, 0.1875, 0.8125, 0.8125, 0.8125);
    public static final AxisAlignedBB AABB_PRECIPITATE = new AxisAlignedBB(0.1875, 0.3125, 0.1875, 0.8125, 0.375, 0.8125);
    protected static final AxisAlignedBB AABB_BOUNDING_BOX = new AxisAlignedBB(0.125, 0.1875, 0.125, 0.875, 0.875, 0.875);

    public BlockCauldron(boolean isBurning) {
        super(Material.IRON, MapColor.STONE);
        setDefaultState(blockState.getBaseState().withProperty(LEVEL, Integer.valueOf(0)));
        if (isBurning)
            setLightLevel(0.875F);
        this.isBurning = isBurning;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityCauldron();
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn) {
        for (int i = 0; i < BOXES.length; i++) {
            AxisAlignedBB box = BOXES[i];
            if (box.equals(AABB_WOOD)) {
                box = box.expand(-0.0625, 0, -0.0625);
            }
            addCollisionBoxToList(pos, entityBox, collidingBoxes, box);
        }
    }

    @Override
    @Nullable
    public RayTraceResult collisionRayTrace(IBlockState blockState, World worldIn, BlockPos pos, Vec3d start, Vec3d end) {
        RayTraceResult lookObject = getExtendedRayTraceResult(start, end, pos);
        if (lookObject != null) {
            return new RayTraceResult(lookObject.hitVec.addVector(pos.getX(), pos.getY(), pos.getZ()), lookObject.sideHit, pos);
        }
        return null;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return AABB_BOUNDING_BOX;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        TileEntityCauldron cauldronTE = getCauldronTileEntity(worldIn, pos);
        if (cauldronTE != null) {
            cauldronTE.liftHandle();
        }
    }

    private TileEntityCauldron getCauldronTileEntity(World worldIn, BlockPos pos) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity != null && tileEntity instanceof TileEntityCauldron) {
            return (TileEntityCauldron) tileEntity;
        }
        return null;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        super.breakBlock(worldIn, pos, state);
        worldIn.removeTileEntity(pos);
    }

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        if (worldIn.isRemote)
            return;

        AxisAlignedBB waterBox = getWaterCollisionBox(state);
        if (entityIn.getEntityBoundingBox().intersectsWith(waterBox.offset(pos))) {
            if (entityIn.isBurning())
                entityIn.extinguish();

            if (entityIn instanceof EntityItem) {
                EntityItem entityItem = ((EntityItem) entityIn);
                ItemStack stack = entityItem.getEntityItem();
                if (stack == null || stack.getItem() == null)
                    return;

                if (stack.getItem() == ItemsKaoliniteTest.crucibleClayDust) {
                    entityItem.setEntityItemStack(new ItemStack(ItemsKaoliniteTest.wetCrucibleClay, stack.stackSize));
                }
            }
        }
    }

    public AxisAlignedBB getWaterCollisionBox(IBlockState state) {
        int level = getWaterLevel(state);
        double waterHeight = 0.3125 + (level < 2 ? level * 0.125 : 0.125 + (level - 1) * 0.1875);
        AxisAlignedBB waterBox = new AxisAlignedBB(AABB_WATER.minX, AABB_WATER.minY, AABB_WATER.minZ, AABB_WATER.maxX, waterHeight, AABB_WATER.maxZ);
        return waterBox;
    }

    public int getWaterLevel(IBlockState state) {
        return state.getValue(LEVEL).intValue();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity == null || !(tileEntity instanceof TileEntityCauldron))
            return true;

        TileEntityCauldron cauldronTE = (TileEntityCauldron) tileEntity;

        int level = getWaterLevel(state);
        boolean fullOfSolidPrecipitate = cauldronTE.hasMaximunSolidPrecipitate();
        ExtendedRayTraceResult lookObject = getExtendedRayTraceResultFromPlayer(playerIn, pos);

        if (level == 0 && fullOfSolidPrecipitate && lookObject != null && lookObject.isLookingAtPrecipitate && (heldItem == null || (heldItem.getItem() == ItemsKaoliniteTest.kaoliniteBall && heldItem.stackSize < heldItem.getMaxStackSize()))) {
            ItemStack stack;
            if (heldItem == null) {
                stack = new ItemStack(ItemsKaoliniteTest.kaoliniteBall);
            } else {
                stack = heldItem.copy();
                stack.stackSize = Math.min(stack.stackSize + 1, stack.getMaxStackSize());
            }
            playerIn.setHeldItem(hand, stack);
            cauldronTE.setProgressTicks(0);
            return true;
        }

        if (heldItem == null)
            return true;

        Item item = heldItem.getItem();

        if (lookObject != null && lookObject.isLookingAtLogs) {
            return interactWithLogs(worldIn, pos, playerIn, hand, heldItem, item, level, lookObject);
        }

        boolean isPrecursor = cauldronTE.isPrecursor();
        boolean isPureWater = cauldronTE.isPureWater();

        if (item == Items.BUCKET) {
            if (level != 3 || !(isPrecursor || isPureWater) || (cauldronTE.hasSolidPrecipitate() && !fullOfSolidPrecipitate))
                return true;
            collectLiquid(worldIn, pos, state, playerIn, hand, heldItem, isPrecursor);
            return true;
        }

        boolean isFilledBucket = item == Items.WATER_BUCKET || ItemStack.areItemStacksEqual(heldItem, getLiquidBucket(true));
        if (isPrecursor)
            return isFilledBucket;

        if (isFilledBucket) {
            if (fullOfSolidPrecipitate)
                return true;
            if (level < 3 && !worldIn.isRemote) {
                if (!playerIn.capabilities.isCreativeMode) {
                    playerIn.setHeldItem(hand, new ItemStack(Items.BUCKET));
                }
                playerIn.addStat(StatList.CAULDRON_FILLED);
                setWaterLevel(worldIn, pos, state, 3);
            }
            if (item == Items.WATER_BUCKET) {
                cauldronTE.setPureWater();
            } else {
                TileEntity tileEntity2 = worldIn.getTileEntity(pos);
                if (tileEntity2 != null && tileEntity2 instanceof TileEntityCauldron) {
                    TileEntityCauldron cauldronTE2 = (TileEntityCauldron) tileEntity2;
                    cauldronTE2.setCountAluminum(7);
                    cauldronTE2.setCountSilica(7);
                }
            }
            playEmptyBucketSound(worldIn, pos, playerIn);
            return true;
        } else if (item == Items.GLASS_BOTTLE) {
            if (level == 0 || !isPureWater)
                return true;
            if (worldIn.isRemote) {
                worldIn.playSound(playerIn, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0F, 1.5F);
                return true;
            }
            if (!playerIn.capabilities.isCreativeMode) {
                ItemStack itemstack1 = PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.WATER);
                playerIn.addStat(StatList.CAULDRON_USED);

                if (--heldItem.stackSize == 0) {
                    playerIn.setHeldItem(hand, itemstack1);
                } else if (!playerIn.inventory.addItemStackToInventory(itemstack1)) {
                    playerIn.dropItem(itemstack1, false);
                } else if (playerIn instanceof EntityPlayerMP) {
                    ((EntityPlayerMP)playerIn).sendContainerToPlayer(playerIn.inventoryContainer);
                } 
            }
            setWaterLevel(worldIn, pos, state, level - 1);
            return true;
        } else {
            if (level > 0 && item instanceof ItemArmor) {
                ItemArmor itemarmor = (ItemArmor)item;
                if (itemarmor.getArmorMaterial() == ItemArmor.ArmorMaterial.LEATHER && itemarmor.hasColor(heldItem) && !worldIn.isRemote) {
                    itemarmor.removeColor(heldItem);
                    setWaterLevel(worldIn, pos, state, level - 1);
                    playerIn.addStat(StatList.ARMOR_CLEANED);
                    return true;
                }
            }

            if (level > 0 && item instanceof ItemBanner) {
                if (TileEntityBanner.getPatterns(heldItem) == 0 || worldIn.isRemote)
                    return true;
                ItemStack itemstack = heldItem.copy();
                itemstack.stackSize = 1;
                TileEntityBanner.removeBannerData(itemstack);
                playerIn.addStat(StatList.BANNER_CLEANED);
                if (!playerIn.capabilities.isCreativeMode) {
                    --heldItem.stackSize;
                }

                if (heldItem.stackSize == 0) {
                    playerIn.setHeldItem(hand, itemstack);
                } else if (!playerIn.inventory.addItemStackToInventory(itemstack)) {
                    playerIn.dropItem(itemstack, false);
                } else if (playerIn instanceof EntityPlayerMP) {
                    ((EntityPlayerMP)playerIn).sendContainerToPlayer(playerIn.inventoryContainer);
                }

                if (!playerIn.capabilities.isCreativeMode) {
                    setWaterLevel(worldIn, pos, state, level - 1);
                }
                return true;
            }
        }
        return false;
    }

    private void collectLiquid(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, boolean isPrecursor) {
        if (!worldIn.isRemote) {
            if (!playerIn.capabilities.isCreativeMode) {
                --heldItem.stackSize;
                if (heldItem.stackSize == 0) {
                    playerIn.setHeldItem(hand, getLiquidBucket(isPrecursor));
                } else if (!playerIn.inventory.addItemStackToInventory(getLiquidBucket(isPrecursor))) {
                    playerIn.dropItem(getLiquidBucket(isPrecursor), false);
                }
            }
            playerIn.addStat(StatList.CAULDRON_USED);
            setWaterLevel(worldIn, pos, state, 0);
        }
        worldIn.playSound(playerIn, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
    }

    private boolean interactWithLogs(World worldIn, BlockPos pos, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, Item item, int level, ExtendedRayTraceResult lookObject) {
        if (item == Items.WATER_BUCKET) {
            playEmptyBucketSound(worldIn, pos, playerIn);
            int n = isBurning ? 4 : 3;
            IParticleFactory[] particles = new IParticleFactory[n];
            for (int i = 0; i < 3; i++) {
                particles[i] = new ParticleCauldronSplash.Factory();
            }
            if (isBurning) {
                particles[3] = new ParticleCauldronSmokeLarge.Factory();
                worldIn.playSound(playerIn, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8F);
            }
            spawnParticlesForLogs(worldIn, pos, lookObject, 25, particles);
            if (!worldIn.isRemote) {
                if (!playerIn.capabilities.isCreativeMode) {
                    playerIn.setHeldItem(hand, new ItemStack(Items.BUCKET));
                }
                setCauldronBurning(worldIn, pos, level, BlocksKaoliniteTest.cauldron_unlit);
            }
        } else if (item == Items.FLINT_AND_STEEL) {
            worldIn.playSound(playerIn, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, worldIn.rand.nextFloat() * 0.4F + 0.8F);
            spawnParticlesForLogs(worldIn, pos, lookObject, 16, new ParticleFlame.Factory(), new ParticleCauldronSmokeNormal.Factory());
            heldItem.damageItem(1, playerIn);
            if (!worldIn.isRemote) {
                setCauldronBurning(worldIn, pos, level, BlocksKaoliniteTest.cauldron_lit);
            }
        }
        return true;
    }

    private void setCauldronBurning(World worldIn, BlockPos pos, int level, Block cauldronBlock) {
        NBTTagCompound cauldronNBT = new NBTTagCompound();
        TileEntityCauldron cauldronTE = getCauldronTileEntity(worldIn, pos);
        if (cauldronTE != null) {
            cauldronTE.writeCauldronToNBT(cauldronNBT);
        }
        worldIn.setBlockState(pos, cauldronBlock.getDefaultState().getActualState(worldIn, pos).withProperty(LEVEL, Integer.valueOf(MathHelper.clamp_int(level, 0, 3))), 2);
        cauldronTE = getCauldronTileEntity(worldIn, pos);
        if (cauldronTE != null) {
            cauldronTE.readCauldronFromNBT(cauldronNBT);
        }
    }

    public void spawnParticlesForLogs(World worldIn, BlockPos pos, ExtendedRayTraceResult lookObject, int particleCount, IParticleFactory... particleFactories) {
        if (!worldIn.isRemote)
            return;
        Vec3d particlePos;
        if (lookObject != null) {
            Vec3d hit = lookObject.hitVec;
            particlePos = new Vec3d(hit.xCoord + Math.random() * 0.02 - 0.01, hit.yCoord + Math.random() * 0.02 - 0.01, hit.zCoord + Math.random() * 0.01);
            for (IParticleFactory particleFactory : particleFactories) {
                ClientHelper.spawnParticle(worldIn, particlePos, particleFactory);
            }
        }
        Vec3d logXZCenter = new Vec3d(0.5, 0, 0.5);
        Vec3d particleXZPos;
        for (int k = 0; k < particleCount; ++k) {
            while (true) {
                particleXZPos = new Vec3d(Math.random(), 0, Math.random());
                if (particleXZPos.distanceTo(logXZCenter) > (AABB_WOOD.maxZ - AABB_WOOD.minZ) / 2)
                    continue;
                AxisAlignedBB boundBox = AABB_WOOD;
                particlePos = new Vec3d(pos.getX() + particleXZPos.xCoord, pos.getY() + Math.random() * (boundBox.contract(0.03125).maxY - boundBox.contract(0.125).minY) + boundBox.contract(0.125).minY, pos.getZ() + particleXZPos.zCoord);
                for (IParticleFactory particleFactory : particleFactories) {
                    ClientHelper.spawnParticle(worldIn, particlePos, particleFactory);
                }
                break;
            }
        }
    }

    @Override
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (isBurning && rand.nextDouble() < 0.1D) {
            worldIn.playSound(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
        }
    }
    private void playEmptyBucketSound(World worldIn, BlockPos pos, EntityPlayer playerIn) {
        worldIn.playSound(playerIn, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
    }

    @Override
    public void fillWithRain(World worldIn, BlockPos pos) {
        if (worldIn.rand.nextInt(20) == 1) {
            float f = worldIn.getBiomeGenForCoords(pos).getFloatTemperature(pos);
            if (worldIn.getBiomeProvider().getTemperatureAtHeight(f, pos.getY()) >= 0.15F) {
                IBlockState state = worldIn.getBlockState(pos);
                int level = getWaterLevel(state);
                if (level < 3) {
                    TileEntityCauldron cauldronTE = getCauldronTileEntity(worldIn, pos);
                    if (cauldronTE != null) {
                        if (cauldronTE.isPureWater() && !cauldronTE.hasSolidPrecipitate()) {
                            setWaterLevel(worldIn, pos, state, level + 1);
                        }
                    }
                }
            }
        }
    }

    private ItemStack getLiquidBucket(boolean returnPrecursor) {
        if (returnPrecursor) {
            UniversalBucket bucket = ForgeModContainer.getInstance().universalBucket;
            ItemStack bucketStack = new ItemStack(bucket);
            FluidStack fluidStack = new FluidStack(FluidsKaoliniteTest.kaolinitePrecursor, 1000);
            bucket.fill(bucketStack, fluidStack, true);
            return bucketStack;
        }
        return new ItemStack(Items.WATER_BUCKET);
    }

    public void setWaterLevel(World worldIn, BlockPos pos, IBlockState state, int level) {
        double waterTemp = 0;
        int progressTicks = 0;
        TileEntityCauldron cauldronTE = getCauldronTileEntity(worldIn, pos);
        if (cauldronTE != null) {
            waterTemp = cauldronTE.getWaterTemp();
            progressTicks = cauldronTE.getProgressTicks();
        }
        worldIn.setBlockState(pos, state.withProperty(LEVEL, Integer.valueOf(MathHelper.clamp_int(level, 0, 3))), 2);
        cauldronTE = getCauldronTileEntity(worldIn, pos);
        if (cauldronTE != null) {
            cauldronTE.setWaterTemp(waterTemp);
            cauldronTE.setProgressTicks(progressTicks);
        }
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
        return getWaterLevel(blockState);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(LEVEL, Integer.valueOf(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return getWaterLevel(state);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {LEVEL});
    }

    @Override
    public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
        return true;
    }

    @SubscribeEvent
    public void drawBlockHighlight(DrawBlockHighlightEvent event) {
        if(!(event.getTarget().typeOfHit == RayTraceResult.Type.BLOCK && event.getPlayer().worldObj.getBlockState(event.getTarget().getBlockPos()).getBlock() instanceof BlockCauldron)) {
            return;
        }
        EntityPlayer player = ClientHelper.getPlayer();
        BlockPos pos = event.getTarget().getBlockPos();
        ExtendedRayTraceResult rayTraceResult = getExtendedRayTraceResultFromPlayer(player, pos);
        if (rayTraceResult != null) {
            if (rayTraceResult.isLookingAtLogs) {
                event.setCanceled(true);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                GlStateManager.glLineWidth(2.0F);
                GlStateManager.disableTexture2D();
                GlStateManager.depthMask(false);

                double partialTicks = event.getPartialTicks();
                double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
                double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
                double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;
                RenderGlobal.func_189697_a(AABB_WOOD.offset(pos).expandXyz(0.0020000000949949026D).offset(-d0, -d1, -d2), 0.0F, 0.0F, 0.0F, 0.4F);

                GlStateManager.depthMask(true);
                GlStateManager.enableTexture2D();
                GlStateManager.disableBlend();
            }
        }
    }

    private ExtendedRayTraceResult getExtendedRayTraceResultFromPlayer(EntityPlayer player, BlockPos pos) {
        double reach = 5;
        if (player instanceof EntityPlayerMP) {
            reach = ((EntityPlayerMP) player).interactionManager.getBlockReachDistance();
        }
        Vec3d start = new Vec3d(player.posX, player.posY + player.getEyeHeight(), player.posZ);
        Vec3d end = start.add(new Vec3d(player.getLookVec().xCoord * reach, player.getLookVec().yCoord * reach, player.getLookVec().zCoord * reach));
        ExtendedRayTraceResult rayTraceResult = getExtendedRayTraceResult(start, end, pos);
        return rayTraceResult;
    }

    private ExtendedRayTraceResult getExtendedRayTraceResult(Vec3d start, Vec3d end, BlockPos pos) {
        RayTraceResult lookObject = null;
        double shortestDistance = Double.MAX_VALUE;
        int index = 0;
        for (int i = 0; i < BOXES.length; i++) {
            RayTraceResult rayTraceResult = BOXES[i].offset(pos).calculateIntercept(start, end);
            if (rayTraceResult != null) {
                double distance = start.distanceTo(rayTraceResult.hitVec);
                if (distance < shortestDistance) {
                    lookObject = rayTraceResult;
                    shortestDistance = distance;
                    index = i;
                }
            }
        }
        boolean isLookingAtPrecipitate = false;
        RayTraceResult rayTraceResult = AABB_PRECIPITATE.offset(pos).calculateIntercept(start, end);
        if (rayTraceResult != null && start.distanceTo(rayTraceResult.hitVec) < shortestDistance) {
            isLookingAtPrecipitate = true;
        }
        return lookObject == null ? null : new ExtendedRayTraceResult(lookObject, index == 13, isLookingAtPrecipitate);
    }

    private static class ExtendedRayTraceResult extends RayTraceResult {
        private boolean isLookingAtLogs, isLookingAtPrecipitate;

        public ExtendedRayTraceResult(RayTraceResult rayTraceResult, boolean isLookingAtLogs, boolean isLookingAtPrecipitate) {
            super(rayTraceResult.hitVec, rayTraceResult.sideHit, rayTraceResult.getBlockPos());
            this.isLookingAtLogs = isLookingAtLogs;
            this.isLookingAtPrecipitate = isLookingAtPrecipitate;
        }
    }

}