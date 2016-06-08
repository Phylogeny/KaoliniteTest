package com.phylogeny.kaolinitetest.item;

import java.util.List;

import com.phylogeny.kaolinitetest.init.FluidsKaoliniteTest;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ItemAluminumPowder extends ItemKaoliniteTestBase
{
	public ItemAluminumPowder(String name)
	{
		super(name);
	}
	
	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem)
	{
		World world = entityItem.worldObj;
		if (!world.isRemote && !entityItem.cannotPickup())
		{
			int x = MathHelper.floor_double(entityItem.posX);
			int y = MathHelper.floor_double(entityItem.posY);
			int z = MathHelper.floor_double(entityItem.posZ);
			BlockPos pos = new BlockPos(x, y, z);
			if (world.getBlockState(pos).getMaterial() == Material.water)
			{
				ItemStack stack = entityItem.getEntityItem();
				if (stack != null && stack.stackSize == 7)
				{
					List<Entity> entities = entityItem.worldObj.getEntitiesWithinAABBExcludingEntity(entityItem, new AxisAlignedBB(pos));
					for (Entity entity : entities)
					{
						if (entity != null && entity instanceof EntityItem)
						{
							EntityItem entityItem2 = (EntityItem) entity;
							if (!entityItem2.cannotPickup())
							{
								ItemStack stack2 = entityItem2.getEntityItem();
								if (stack2 != null && stack2.stackSize == 7)
								{
									entityItem.setDead();
									entityItem2.setDead();
									world.setBlockState(pos, FluidsKaoliniteTest.kaolinitePrecursorBlock.getDefaultState(), 3);
								}
							}
						}
					}
				}
			}
		}
		return false;
	}
	
}