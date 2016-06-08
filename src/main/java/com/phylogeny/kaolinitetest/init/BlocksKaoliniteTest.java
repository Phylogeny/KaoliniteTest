package com.phylogeny.kaolinitetest.init;

import com.phylogeny.kaolinitetest.block.BlockKaoliniteOre;
import com.phylogeny.kaolinitetest.client.creativetab.CreativeTabKaoliniteTest;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlocksKaoliniteTest
{
    public static Block kaoliniteOre = new BlockKaoliniteOre();
    
    public static void registerBlocks()
    {
        registerBlock(kaoliniteOre, "kaolinite_ore");
    }
    
    public static void registerBlock(Block block, String name)
    {
        block.setRegistryName(name);
        block.setUnlocalizedName(block.getRegistryName().toString());
        block.setCreativeTab(CreativeTabKaoliniteTest.CREATIVE_TAB);
        GameRegistry.register(block);
        GameRegistry.register((new ItemBlock(block)).setRegistryName(block.getRegistryName()));
    }
}