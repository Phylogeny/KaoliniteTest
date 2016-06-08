package com.phylogeny.kaolinitetest.client.creativetab;

import com.phylogeny.kaolinitetest.init.ItemsKaoliniteTest;
import com.phylogeny.kaolinitetest.reference.Reference;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CreativeTabKaoliniteTest
{
    public static final CreativeTabs CREATIVE_TAB = new CreativeTabs(Reference.MOD_ID)
    {
        @Override
        @SideOnly(Side.CLIENT)
        public Item getTabIconItem()
        {
            return ItemsKaoliniteTest.crucible;
        }
    };

}