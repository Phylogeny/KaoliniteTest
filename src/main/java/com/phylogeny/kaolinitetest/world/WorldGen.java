package com.phylogeny.kaolinitetest.world;

import java.util.Random;

import com.phylogeny.kaolinitetest.init.BlocksKaoliniteTest;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class WorldGen implements IWorldGenerator {
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        BlockPos blockPos = new BlockPos(chunkX * 16 + random.nextInt(16),
                chunkProvider.provideChunk(chunkX, chunkZ).getTopFilledSegment() + 16,
                chunkZ * 16 + random.nextInt(16));
        Biome biome = world.getBiomeGenForCoords(blockPos);
        if (!(biome == Biomes.JUNGLE || biome == Biomes.JUNGLE_HILLS || biome == Biomes.JUNGLE_EDGE))
            return;
        int minLevel = world.getSeaLevel() - 10;
        while (true) {
            if (world.getBlockState(blockPos).getBlock() == Blocks.DIRT && world.getBlockState(blockPos).getMaterial() != Material.WATER) {
                if (world.getBlockState(blockPos.down()).getBlock() == Blocks.DIRT) {
                    blockPos = blockPos.down();
                }
                break;
            }
            blockPos = blockPos.down();
            if (blockPos.getY() < minLevel)
                return;
        }
        int radius = 2 + random.nextInt(3);
        for (int i = -radius; i <= radius; i++) {
            for (int j = -radius; j <= radius; j++) {
                BlockPos blockPos2 = new BlockPos(i + blockPos.getX(), blockPos.getY(), j + blockPos.getZ());
                Block block = world.getBlockState(blockPos2).getBlock();
                int iAbs = Math.abs(i);
                int jAbs = Math.abs(j);
                if (iAbs + jAbs <= radius && !(iAbs == 0 && jAbs == radius) && !(iAbs == radius && jAbs == 0)
                		&& (block == Blocks.DIRT || block == Blocks.GRASS || block == Blocks.STONE)) {
                    world.setBlockState(blockPos2, BlocksKaoliniteTest.kaoliniteBlock.getDefaultState(), 2);
                }
            }
        }
    }

    public static void registerWorldGen() {
        GameRegistry.registerWorldGenerator(new WorldGen(), 0);
    }

}