package com.phylogeny.kaolinitetest.world;

import java.util.Random;

import com.phylogeny.kaolinitetest.init.BlocksKaoliniteTest;

import net.minecraft.block.Block;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class WorldGen implements IWorldGenerator {
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world,
            IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        BlockPos blockPos = new BlockPos(chunkX * 16 + random.nextInt(16),
                chunkProvider.provideChunk(chunkX, chunkZ).getTopFilledSegment() + 16,
                chunkZ * 16 + random.nextInt(16));
        BiomeGenBase biome = world.getBiomeGenForCoords(blockPos);
        if (!(biome == Biomes.jungle || biome == Biomes.jungleEdge || biome == Biomes.jungleHills))
            return;
        int minLevel = world.getSeaLevel() - 10;
        boolean oneBelow = random.nextBoolean();
        while (true) {
            if (world.getBlockState(blockPos).getBlock() == Blocks.dirt) {
                if (oneBelow && world.getBlockState(blockPos.down()).getBlock() == Blocks.dirt) {
                	blockPos = blockPos.down();
                }
                break;
            }
            blockPos = blockPos.down();
            if (blockPos.getY() < minLevel)
                return;
        }
        int margin = 2;
        int marginSqr = margin * margin;
        for (int i = blockPos.getX() - margin; i <= blockPos.getX() + margin; i++) {
            for (int j = blockPos.getZ() - margin; j <= blockPos.getZ() + margin; j++) {
                int x = i - blockPos.getX();
                int y = j - blockPos.getZ();
                BlockPos blockPos2 = new BlockPos(i, blockPos.getY(), j);
                Block block = world.getBlockState(blockPos2).getBlock();
                if (x * x + y * y <= marginSqr && (block == Blocks.dirt || block == Blocks.grass)) {
                    world.setBlockState(blockPos2, BlocksKaoliniteTest.kaoliniteBlock.getDefaultState(), 2);
                }
            }
        }
    }

    public static void registerWorldGen() {
        GameRegistry.registerWorldGenerator(new WorldGen(), 0);
    }

}