package com.phylogeny.kaolinitetest.block;

import java.util.List;

import javax.annotation.Nullable;

import com.phylogeny.kaolinitetest.init.ItemsKaoliniteTest;
import com.phylogeny.kaolinitetest.init.RecipeRegistration;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
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
import net.minecraft.potion.PotionUtils;
import net.minecraft.stats.StatList;
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
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BlockModCauldron extends Block
{
    public static final PropertyInteger LEVEL = PropertyInteger.create("level", 0, 4);
    protected static final AxisAlignedBB AABB_LEG_1_SEGMENT_1 = new AxisAlignedBB(0.125, 0.1875, 0.1875, 0.1875, 0.25, 0.25);
    protected static final AxisAlignedBB AABB_LEG_1_SEGMENT_2 = new AxisAlignedBB(0.125, 0.1875, 0.125, 0.25, 0.25, 0.1875);
    protected static final AxisAlignedBB AABB_LEG_2_SEGMENT_1 = new AxisAlignedBB(0.125, 0.1875, 0.75, 0.1875, 0.25, 0.8125);
    protected static final AxisAlignedBB AABB_LEG_2_SEGMENT_2 = new AxisAlignedBB(0.125, 0.1875, 0.8125, 0.25, 0.25, 0.875);
    protected static final AxisAlignedBB AABB_LEG_3_SEGMENT_1 = new AxisAlignedBB(0.8125, 0.1875, 0.75, 0.875, 0.25, 0.8125);
    protected static final AxisAlignedBB AABB_LEG_3_SEGMENT_2 = new AxisAlignedBB(0.75, 0.1875, 0.8125, 0.875, 0.25, 0.875);
    protected static final AxisAlignedBB AABB_LEG_4_SEGMENT_1 = new AxisAlignedBB(0.8125, 0.1875, 0.1875, 0.875, 0.25, 0.25);
    protected static final AxisAlignedBB AABB_LEG_4_SEGMENT_2 = new AxisAlignedBB(0.75, 0.1875, 0.125, 0.875, 0.25, 0.1875);
    protected static final AxisAlignedBB AABB_BASE = new AxisAlignedBB(0.125, 0.25, 0.125, 0.875, 0.3125, 0.875);
    protected static final AxisAlignedBB AABB_WALL_1 = new AxisAlignedBB(0.125, 0.3125, 0.1875, 0.1875, 0.875, 0.875);
    protected static final AxisAlignedBB AABB_WALL_2 = new AxisAlignedBB(0.8125, 0.3125, 0.125, 0.875, 0.875, 0.8125);
    protected static final AxisAlignedBB AABB_WALL_3 = new AxisAlignedBB(0.125, 0.3125, 0.125, 0.8125, 0.875, 0.1875);
    protected static final AxisAlignedBB AABB_WALL_4 = new AxisAlignedBB(0.1875, 0.3125, 0.8125, 0.875, 0.875, 0.875);
    protected static final AxisAlignedBB AABB_LOG_1 = new AxisAlignedBB(0.40625, -0.000625, 0.0625, 0.59375, 0.186875, 0.9375);
    protected static final AxisAlignedBB AABB_LOG_3 = new AxisAlignedBB(0.0625, 0.0, 0.375, 0.9375, 0.1875, 0.5625);
    protected static final AxisAlignedBB AABB_WOOD = new AxisAlignedBB(0.125, 0.0, 0.125, 0.875, 0.1875, 0.875);
    protected static final AxisAlignedBB[] BOXES = new AxisAlignedBB[]{AABB_LEG_1_SEGMENT_1, AABB_LEG_1_SEGMENT_2, AABB_LEG_2_SEGMENT_1, AABB_LEG_2_SEGMENT_2, AABB_LEG_3_SEGMENT_1,
        AABB_LEG_3_SEGMENT_2, AABB_LEG_4_SEGMENT_1, AABB_LEG_4_SEGMENT_2, AABB_BASE, AABB_WALL_1, AABB_WALL_2, AABB_WALL_3, AABB_WALL_4, AABB_WOOD};
    protected static final AxisAlignedBB AABB_WATER = new AxisAlignedBB(0.1875, 0.3125, 0.1875, 0.8125, 0.8125, 0.8125);
    protected static final AxisAlignedBB AABB_BOUNDING_BOX = new AxisAlignedBB(0.125, 0.1875, 0.125, 0.875, 0.875, 0.875);

    public BlockModCauldron()
    {
        super(Material.IRON, MapColor.STONE);
        this.setDefaultState(this.blockState.getBaseState().withProperty(LEVEL, Integer.valueOf(0)));
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn) {
        for (int i = 0; i < BOXES.length; i++) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, BOXES[i]);
        }
    }

    @Override
    @Deprecated
    @Nullable
    public RayTraceResult collisionRayTrace(IBlockState blockState, World worldIn, BlockPos pos, Vec3d start, Vec3d end)
    {
        RayTraceResult lookObject = null;
        double distance = Double.MAX_VALUE;
        for (int i = 0; i < BOXES.length; i++) {
            RayTraceResult rayTraceResult = BOXES[i].offset(pos.getX(), pos.getY(), pos.getZ()).calculateIntercept(start, end);
            if (rayTraceResult != null && start.distanceTo(rayTraceResult.hitVec) < distance) {
                lookObject = rayTraceResult;
            }
        }

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
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        int level = state.getValue(LEVEL).intValue();
        int actualLevel = Math.min(level, 3);
        double waterHeight = 0.125 + (actualLevel < 2 ? actualLevel * 0.125 : 0.125 + (actualLevel - 1) * 0.1875);
        AxisAlignedBB waterbox = new AxisAlignedBB(AABB_WATER.minX, AABB_WATER.minY, AABB_WATER.minZ, AABB_WATER.maxX, waterHeight, AABB_WATER.maxZ);

        if (!worldIn.isRemote && entityIn.getEntityBoundingBox().intersectsWith(waterbox.offset(pos.getX(), pos.getY(), pos.getZ()))) {
            if (entityIn.isBurning()) {
                entityIn.extinguish();
            }

            if (entityIn instanceof EntityItem) {
                EntityItem entityItem = ((EntityItem) entityIn);
                ItemStack stack = entityItem.getEntityItem();
                if (stack != null && stack.getItem() != null) {
                    if (stack.getItem() == ItemsKaoliniteTest.aluminumDust || stack.getItem() == ItemsKaoliniteTest.silicaDust) {
                        if (level == 3) {
                            entityItem.setPickupDelay(5);
                            entityItem.lifespan = 6000;
                            if (entityItem.getEntityItem() != null && entityItem.getEntityItem().stackSize >= 7) {
                                List<Entity> entities = entityItem.worldObj.getEntitiesWithinAABBExcludingEntity(entityItem, new AxisAlignedBB(pos));
                                Item otherDustItem = stack.getItem() == ItemsKaoliniteTest.aluminumDust ? ItemsKaoliniteTest.silicaDust : ItemsKaoliniteTest.aluminumDust;
                                EntityItem otherDustEntity = getEntityItem(entities, otherDustItem);
                                if (otherDustEntity != null) {
                                    removeStack(entityItem, stack.getItem());
                                    removeStack(otherDustEntity, otherDustItem);
                                    this.setWaterLevel(worldIn, pos, state, 4);
                                }
                            }
                        }
                    } else if (stack.getItem() == ItemsKaoliniteTest.crucibleClayDust) {
                        entityItem.setEntityItemStack(new ItemStack(ItemsKaoliniteTest.wetCrucibleClay, stack.stackSize));
                    }
                }
            }
        }
    }

    private void removeStack(EntityItem entityItem, Item item) {
        ItemStack stack = getStack(entityItem, item);
        stack.stackSize -= 7;
        if (stack.stackSize <= 0) {
            entityItem.setDead();
        }
    }

    private EntityItem getEntityItem(List<Entity> entities, Item item) {
        for (Entity entity : entities) {
            if (entity != null && entity instanceof EntityItem) {
                EntityItem entityItem2 = (EntityItem) entity;
                if (getStack(entityItem2, item) != null) {
                    return entityItem2;
                }
            }
        }
        return null;
    }

    private ItemStack getStack(EntityItem entityItem, Item item) {
        ItemStack stack = entityItem.getEntityItem();
        if (stack != null && stack.getItem() != null && stack.getItem() == item && stack.stackSize >= 7) {
            return stack;
        }
        return null;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (heldItem == null) {
            return true;
        }
        int i = state.getValue(LEVEL).intValue();
        Item item = heldItem.getItem();

        if (item == Items.BUCKET) {
            if (i >= 3) {
                if (!worldIn.isRemote) {
                    if (!playerIn.capabilities.isCreativeMode) {
                        --heldItem.stackSize;

                        if (heldItem.stackSize == 0) {
                            playerIn.setHeldItem(hand, getLiquidBucket(i));
                        } else if (!playerIn.inventory.addItemStackToInventory(getLiquidBucket(i))) {
                            playerIn.dropItem(getLiquidBucket(i), false);
                        }
                    }
                    playerIn.addStat(StatList.CAULDRON_USED);
                    this.setWaterLevel(worldIn, pos, state, 0);
                }
                worldIn.playSound(playerIn, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }
            return true;
        }

        if (i > 3) return true;

        if (item == Items.WATER_BUCKET || item == ItemsKaoliniteTest.supernatantAndPrecipitateBucket) {
            if (i < 3 && !worldIn.isRemote) {
                if (!playerIn.capabilities.isCreativeMode) {
                    playerIn.setHeldItem(hand, new ItemStack(item == Items.WATER_BUCKET ? Items.BUCKET : ItemsKaoliniteTest.bucketPrecipitate));
                }
                playerIn.addStat(StatList.CAULDRON_FILLED);
                this.setWaterLevel(worldIn, pos, state, 3);
            }
            worldIn.playSound(playerIn, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
            return true;
        } else if (item == Items.GLASS_BOTTLE) {
            if (i > 0 && !worldIn.isRemote) {
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
                this.setWaterLevel(worldIn, pos, state, i - 1);
            }
            return true;
        } else {
            if (i > 0 && item instanceof ItemArmor) {
                ItemArmor itemarmor = (ItemArmor)item;
                if (itemarmor.getArmorMaterial() == ItemArmor.ArmorMaterial.LEATHER && itemarmor.hasColor(heldItem) && !worldIn.isRemote) {
                    itemarmor.removeColor(heldItem);
                    this.setWaterLevel(worldIn, pos, state, i - 1);
                    playerIn.addStat(StatList.ARMOR_CLEANED);
                    return true;
                }
            }

            if (i > 0 && item instanceof ItemBanner) {
                if (TileEntityBanner.getPatterns(heldItem) > 0 && !worldIn.isRemote) {
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
                        this.setWaterLevel(worldIn, pos, state, i - 1);
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void fillWithRain(World worldIn, BlockPos pos) {
        if (worldIn.rand.nextInt(20) == 1) {
            float f = worldIn.getBiomeGenForCoords(pos).getFloatTemperature(pos);

            if (worldIn.getBiomeProvider().getTemperatureAtHeight(f, pos.getY()) >= 0.15F) {
                IBlockState iblockstate = worldIn.getBlockState(pos);

                if (iblockstate.getValue(LEVEL).intValue() < 3) {
                    worldIn.setBlockState(pos, iblockstate.cycleProperty(LEVEL), 2);
                }
            }
        }
    }

    private ItemStack getLiquidBucket(int level) {
        return level == 3 ? new ItemStack(Items.WATER_BUCKET) : RecipeRegistration.getKaolinitePrecursorBucketStack();
    }

    public void setWaterLevel(World worldIn, BlockPos pos, IBlockState state, int level) {
        worldIn.setBlockState(pos, state.withProperty(LEVEL, Integer.valueOf(MathHelper.clamp_int(level, 0, 4))), 2);
        worldIn.updateComparatorOutputLevel(pos, this);
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
        return Math.min(blockState.getValue(LEVEL).intValue(), 3);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(LEVEL, Integer.valueOf(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(LEVEL).intValue();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {LEVEL});
    }

    @Override
    public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
        return true;
    }

}