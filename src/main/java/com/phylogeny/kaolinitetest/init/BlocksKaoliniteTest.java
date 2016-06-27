package com.phylogeny.kaolinitetest.init;

import com.phylogeny.kaolinitetest.block.BlockKaolinite;
import com.phylogeny.kaolinitetest.block.BlockCauldron;
import com.phylogeny.kaolinitetest.client.creativetab.CreativeTabKaoliniteTest;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlocksKaoliniteTest {
    public static Block kaoliniteBlock = new BlockKaolinite();
    public static Block cauldron = new BlockCauldron();

    public static void registerBlocks() {
        registerBlock(kaoliniteBlock, "kaolinite_block", true);
        registerBlock(cauldron, "cauldron_block", true);
    }

    public static void registerBlock(Block block, String name, boolean setCreativeTab) {
        block.setRegistryName(name);
        block.setUnlocalizedName(block.getRegistryName().toString());
        if (setCreativeTab)
            block.setCreativeTab(CreativeTabKaoliniteTest.CREATIVE_TAB);
        GameRegistry.register(block);
        GameRegistry.register((new ItemBlock(block)).setRegistryName(block.getRegistryName()));
    }

}