package com.phylogeny.kaolinitetest.world;

import java.util.Random;

import com.phylogeny.kaolinitetest.init.BlocksKaoliniteTest;

import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class WorldGen implements IWorldGenerator
{
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world,
            IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
    {
        chunkX *= 16;
        chunkZ *= 16;
        for (int i = 0; i < 400; i++)
        {
            BlockPos blockPos = new BlockPos(chunkX + random.nextInt(16), random.nextInt(255), chunkZ + random.nextInt(16));
            BiomeGenBase biome = world.getBiomeGenForCoords(blockPos);
            if (biome == Biomes.jungle || biome == Biomes.jungleEdge || biome == Biomes.jungleHills)
            {
                IBlockState state = world.getBlockState(blockPos);
                if (state.getBlock().isReplaceableOreGen(state, world, blockPos, BlockMatcher.forBlock(Blocks.dirt)))
                {
                    world.setBlockState(blockPos, BlocksKaoliniteTest.kaoliniteOre.getDefaultState(), 2);
                }
            }
        }
    }
    
    public static void registerWorldGen()
    {
        GameRegistry.registerWorldGenerator(new WorldGen(), 0);
    }
    
}