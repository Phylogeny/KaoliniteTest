package com.phylogeny.kaolinitetest.item;

import java.util.List;

import com.phylogeny.kaolinitetest.init.FluidsKaoliniteTest;
import com.phylogeny.kaolinitetest.init.ItemsKaoliniteTest;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ItemAluminumDust extends ItemKaoliniteTestBase {
    public ItemAluminumDust(String name) {
        super(name);
    }

    @Override
    public boolean onEntityItemUpdate(EntityItem entityItem) {
        World world = entityItem.worldObj;
        if (!world.isRemote) {
            int x = MathHelper.floor_double(entityItem.posX);
            int y = MathHelper.floor_double(entityItem.posY);
            int z = MathHelper.floor_double(entityItem.posZ);
            BlockPos pos = new BlockPos(x, y, z);
            IBlockState state = world.getBlockState(pos);
            if (state.getBlock() == Blocks.WATER && (state.getValue(BlockLiquid.LEVEL)).intValue() == 0 && entityItem.getEntityItem() != null && entityItem.getEntityItem().stackSize >= 7) {
                List<Entity> entities = entityItem.worldObj.getEntitiesWithinAABBExcludingEntity(entityItem, new AxisAlignedBB(pos));
                EntityItem silicaEntity = getEntityItem(entities, ItemsKaoliniteTest.silicaDust);
                if (silicaEntity != null) {
                    removeStack(entityItem, this);
                    removeStack(silicaEntity, ItemsKaoliniteTest.silicaDust);
                    world.setBlockState(pos, FluidsKaoliniteTest.kaolinitePrecursorBlock.getDefaultState(), 3);
                }
            }
        }
        return false;
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

}