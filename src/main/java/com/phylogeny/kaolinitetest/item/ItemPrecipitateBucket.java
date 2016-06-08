package com.phylogeny.kaolinitetest.item;

import com.phylogeny.kaolinitetest.init.ItemsKaoliniteTest;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemPrecipitateBucket extends ItemKaoliniteTestBase
{
    public ItemPrecipitateBucket(String name)
    {
        super(name);
        setMaxStackSize(1);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn,
            World worldIn, EntityPlayer playerIn, EnumHand hand)
    {
        if (!worldIn.isRemote)
        {
            playerIn.dropItem(new ItemStack(ItemsKaoliniteTest.kaoliniteBall), false, false);
        }
        return new ActionResult(EnumActionResult.SUCCESS, new ItemStack(Items.bucket));
    }

}