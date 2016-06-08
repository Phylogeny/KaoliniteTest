package com.phylogeny.kaolinitetest.item;

import com.phylogeny.kaolinitetest.client.creativetab.CreativeTabKaoliniteTest;

import net.minecraft.item.Item;

public class ItemKaoliniteTestBase extends Item
{
    public ItemKaoliniteTestBase(String name)
    {
        setRegistryName(name);
        setUnlocalizedName(getRegistryName().toString());
        setCreativeTab(CreativeTabKaoliniteTest.CREATIVE_TAB);
    }

}