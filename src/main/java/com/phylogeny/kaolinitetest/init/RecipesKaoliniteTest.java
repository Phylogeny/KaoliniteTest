package com.phylogeny.kaolinitetest.init;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class RecipesKaoliniteTest
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
		
		//TODO this is just for demonstration - in AL, kaolinite bricks will be pulverized, not crafted
			addShapelessRecipe(ItemsKaoliniteTest.kaolinitePowder, 7, new Object[]{ItemsKaoliniteTest.kaoliniteBrick});
		//TODO this is just for demonstration - in AL, a bucket of a custom fluid will be heated to form the bucket of supernatant/precipitate
			addSmeltingRecipe(Items.lava_bucket, ItemsKaoliniteTest.supernatantAndPrecipitateBucket);
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