package com.phylogeny.kaolinitetest.item;

import com.phylogeny.kaolinitetest.init.ItemsKaoliniteTest;

import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ItemCrucibleClayDust extends ItemKaoliniteTestBase {
    public ItemCrucibleClayDust(String name) {
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
            if (world.getBlockState(pos).getMaterial() == Material.water) {
                ItemStack stack = entityItem.getEntityItem();
                if (stack != null) {
                    entityItem.setEntityItemStack(new ItemStack(ItemsKaoliniteTest.wetCrucibleClay, stack.stackSize));
                }
            }
        }
        return false;
    }

}