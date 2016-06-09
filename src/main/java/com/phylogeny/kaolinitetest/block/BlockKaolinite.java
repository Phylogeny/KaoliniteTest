package com.phylogeny.kaolinitetest.block;

import java.util.Random;

import com.phylogeny.kaolinitetest.init.ItemsKaoliniteTest;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;

public class BlockKaolinite extends Block
{
    public BlockKaolinite()
    {
        super(Material.ground);
        setHardness(0.6F);
        setStepSound(SoundType.GROUND);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return ItemsKaoliniteTest.kaoliniteShard;
    }

    @Override
    public int quantityDroppedWithBonus(int fortune, Random random)
    {
        return this.quantityDropped(random) + random.nextInt(fortune + 1);
    }

    @Override
    public int quantityDropped(Random random)
    {
        return 3 + random.nextInt(2);
    }

}