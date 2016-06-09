package com.phylogeny.kaolinitetest.block;

import java.util.List;
import java.util.Random;

import com.phylogeny.kaolinitetest.init.ItemsKaoliniteTest;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockKaoliniteOre extends Block
{
    public BlockKaoliniteOre()
    {
        super(Material.ground);
        setHardness(0.5F);
        setStepSound(SoundType.GROUND);
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
        List<ItemStack> drops = new java.util.ArrayList<ItemStack>();
        drops.add(new ItemStack(Blocks.dirt));
        Random rand = world instanceof World ? ((World)world).rand : RANDOM;
        int n = 1;
        if (rand.nextInt(2) == 0) n++;
        for (int i = 0; i < n; i++)
        {
            drops.add(new ItemStack(ItemsKaoliniteTest.kaoliniteShard));
        }
        return drops;
    }

}