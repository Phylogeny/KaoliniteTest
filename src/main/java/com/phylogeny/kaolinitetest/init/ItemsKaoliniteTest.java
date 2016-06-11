package com.phylogeny.kaolinitetest.init;

import com.phylogeny.kaolinitetest.item.ItemAluminumDust;
import com.phylogeny.kaolinitetest.item.ItemCrucibleClayDust;
import com.phylogeny.kaolinitetest.item.ItemKaoliniteTestBase;
import com.phylogeny.kaolinitetest.item.ItemBucketPrecipitate;
import com.phylogeny.kaolinitetest.item.ItemSupernatantAndPrecipitateBucket;
import com.phylogeny.kaolinitetest.item.ItemToolRemoval;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemsKaoliniteTest
{
    public static Item aluminumDust, silicaDust, kaoliniteDust, crucibleClayDust, supernatantAndPrecipitateBucket, bucketPrecipitate,
                        kaoliniteBall, kaoliniteBrick, kaoliniteShard, wetCrucibleClay, unfiredCrucible, crucible, removalTool;

    public static void itemsInit()
    {
        aluminumDust = registerItem(new ItemAluminumDust("dust_aluminum"));
        silicaDust = registerItem("dust_silica");
        kaoliniteDust = registerItem("dust_kaolinite");
        crucibleClayDust = registerItem(new ItemCrucibleClayDust("dust_crucible_clay"));
        supernatantAndPrecipitateBucket = registerItem(new ItemSupernatantAndPrecipitateBucket("bucket_supernatant_precipitate"));
        bucketPrecipitate = registerItem(new ItemBucketPrecipitate("bucket_precipitate"));
        kaoliniteBall = registerItem("ball_kaolinite");
        kaoliniteBrick = registerItem("brick_kaolinite");
        kaoliniteShard = registerItem("shard_kaolinite");
        wetCrucibleClay = registerItem("crucible_clay_wet");
        unfiredCrucible = registerItem("crucible_unfired");
        crucible = registerItem("crucible");
        removalTool = registerItem(new ItemToolRemoval("tool_removal"));
    }

    private static ItemKaoliniteTestBase registerItem(String name)
    {
        return registerItem(new ItemKaoliniteTestBase(name));
    }

    private static ItemKaoliniteTestBase registerItem(ItemKaoliniteTestBase item)
    {
        GameRegistry.register(item);
        return item;
    }

}