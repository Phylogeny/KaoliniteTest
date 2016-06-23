package com.phylogeny.kaolinitetest.init;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class RecipeRegistration {
    public static void recipeInit() {
        Object[] inputs = new Object[6];
        inputs[0] = ItemsKaoliniteTest.kaoliniteDust;
        for (int i = 1; i < inputs.length; i++)
        {
            inputs[i] = ItemsKaoliniteTest.silicaDust;
        }
        addShapelessRecipe(ItemsKaoliniteTest.crucibleClayDust, 1, inputs);
        GameRegistry.addShapedRecipe(new ItemStack(ItemsKaoliniteTest.unfiredCrucible), new Object[]{
            "A A",
            "A A",
            "AAA",
            'A', ItemsKaoliniteTest.wetCrucibleClay
        });
        addSmeltingRecipe(ItemsKaoliniteTest.kaoliniteBall, ItemsKaoliniteTest.kaoliniteBrick);

        //TODO require AL furnace at a minimum temp (real min temp is 3000 deg F) and for an extended period of time
        addSmeltingRecipe(ItemsKaoliniteTest.unfiredCrucible, ItemsKaoliniteTest.crucible);

        //this is just for demonstration - in AL, kaolinite bricks/shards will be pulverized, not crafted
            addShapelessRecipe(ItemsKaoliniteTest.kaoliniteDust, 7, ItemsKaoliniteTest.kaoliniteBrick);
            addShapelessRecipe(ItemsKaoliniteTest.kaoliniteDust, 1, ItemsKaoliniteTest.kaoliniteShard);
    }

    private static void addShapelessRecipe(Item output, int outputAmount, Object... inputs) {
        GameRegistry.addShapelessRecipe(new ItemStack(output, outputAmount), inputs);
    }

    private static void addSmeltingRecipe(Item input, Item output) {
        GameRegistry.addSmelting(new ItemStack(input), new ItemStack(output), 0);
    }

}