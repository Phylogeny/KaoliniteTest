package com.phylogeny.kaolinitetest.init;

import com.phylogeny.kaolinitetest.block.BlockKaolinite;
import com.phylogeny.kaolinitetest.block.BlockCauldron;
import com.phylogeny.kaolinitetest.client.creativetab.CreativeTabKaoliniteTest;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlocksKaoliniteTest {
    public static Block kaoliniteBlock = new BlockKaolinite();
    public static Block cauldron_lit = new BlockCauldron(true);
    public static Block cauldron_unlit = new BlockCauldron(false);

    public static void registerBlocks() {
        registerBlock(kaoliniteBlock, "kaolinite_block", true);
        registerBlock(cauldron_lit, "cauldron_block_lit", false);
        registerBlock(cauldron_unlit, "cauldron_block_unlit", true);
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