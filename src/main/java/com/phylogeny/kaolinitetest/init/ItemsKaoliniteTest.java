package com.phylogeny.kaolinitetest.init;

import com.phylogeny.kaolinitetest.item.ItemAluminumPowder;
import com.phylogeny.kaolinitetest.item.ItemCrucibleClayPowder;
import com.phylogeny.kaolinitetest.item.ItemKaoliniteTestBase;
import com.phylogeny.kaolinitetest.item.ItemPrecipitateBucket;
import com.phylogeny.kaolinitetest.item.ItemSupernatantAndPrecipitateBucket;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemsKaoliniteTest
{
    public static Item aluminumPowder, silicaPowder, kaolinitePowder, crucibleClayPowder, supernatantAndPrecipitateBucket,
                        precipitateBucket, kaoliniteBall, kaoliniteBrick, wetCrucibleClay, unfiredCrucible, crucible;

    public static void itemsInit()
    {
        aluminumPowder = registerItem(new ItemAluminumPowder("powder_aluminum"));
        silicaPowder = registerItem("powder_silica");
        kaolinitePowder = registerItem("powder_kaolinite");
        crucibleClayPowder = registerItem(new ItemCrucibleClayPowder("powder_crucible_clay"));
        supernatantAndPrecipitateBucket = registerItem(new ItemSupernatantAndPrecipitateBucket("bucket_supernatant_precipitate"));
        precipitateBucket = registerItem(new ItemPrecipitateBucket("precipitate_bucket"));
        kaoliniteBall = registerItem("ball_kaolinite");
        kaoliniteBrick = registerItem("brick_kaolinite");
        wetCrucibleClay = registerItem("crucible_clay_wet");
        unfiredCrucible = registerItem("crucible_unfired");
        crucible = registerItem("crucible");
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