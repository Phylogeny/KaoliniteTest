package com.phylogeny.kaolinitetest.init;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class RecipeRegistration
{
    public static void recipeInit()
    {
        Object[] inputs = new Object[6];
        inputs[0] = ItemsKaoliniteTest.kaolinitePowder;
        for (int i = 1; i < inputs.length; i++)
        {
            inputs[i] = ItemsKaoliniteTest.silicaPowder;
        }
        addShapelessRecipe(ItemsKaoliniteTest.crucibleClayPowder, 1, inputs);
        GameRegistry.addShapedRecipe(new ItemStack(ItemsKaoliniteTest.unfiredCrucible), new Object[]{
            "A A",
            "A A",
            "AAA",
            'A', ItemsKaoliniteTest.wetCrucibleClay
        });
        addSmeltingRecipe(ItemsKaoliniteTest.kaoliniteBall, ItemsKaoliniteTest.kaoliniteBrick);
        addSmeltingRecipe(ItemsKaoliniteTest.unfiredCrucible, ItemsKaoliniteTest.crucible);

        UniversalBucket bucket = ForgeModContainer.getInstance().universalBucket;
        ItemStack bucketStack = new ItemStack(bucket);
        FluidStack fluidStack = new FluidStack(FluidsKaoliniteTest.kaolinitePrecursor, 1000);
        bucket.fill(bucketStack, fluidStack, true);
        GameRegistry.addSmelting(bucketStack, new ItemStack(ItemsKaoliniteTest.supernatantAndPrecipitateBucket), 0);

        //this is just for demonstration - in AL, kaolinite bricks/shards will be pulverized, not crafted
            addShapelessRecipe(ItemsKaoliniteTest.kaolinitePowder, 7, new Object[]{ItemsKaoliniteTest.kaoliniteBrick});
            addShapelessRecipe(ItemsKaoliniteTest.kaolinitePowder, 1, new Object[]{ItemsKaoliniteTest.kaoliniteShard});
    }

    private static void addShapelessRecipe(Item output, int outputAmount, Object... inputs)
    {
        GameRegistry.addShapelessRecipe(new ItemStack(output, outputAmount), inputs);
    }

    private static void addSmeltingRecipe(Item input, Item output)
    {
        GameRegistry.addSmelting(new ItemStack(input), new ItemStack(output), 0);
    }

}