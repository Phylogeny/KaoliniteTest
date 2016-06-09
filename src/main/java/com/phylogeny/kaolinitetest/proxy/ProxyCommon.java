package com.phylogeny.kaolinitetest.proxy;

import com.phylogeny.kaolinitetest.init.BlocksKaoliniteTest;
import com.phylogeny.kaolinitetest.init.FluidsKaoliniteTest;
import com.phylogeny.kaolinitetest.init.ItemsKaoliniteTest;
import com.phylogeny.kaolinitetest.init.RecipesKaoliniteTest;
import com.phylogeny.kaolinitetest.world.WorldGen;

public class ProxyCommon
{
    public void preInit()
    {
        ItemsKaoliniteTest.itemsInit();
        BlocksKaoliniteTest.registerBlocks();
        FluidsKaoliniteTest.registerFluid();
    }

    public void init()
    {
        RecipesKaoliniteTest.recipeInit();
        WorldGen.registerWorldGen();
    }

}