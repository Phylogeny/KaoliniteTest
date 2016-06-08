package com.phylogeny.kaolinitetest.proxy;

import com.phylogeny.kaolinitetest.init.FluidsKaoliniteTest;
import com.phylogeny.kaolinitetest.init.ItemsKaoliniteTest;
import com.phylogeny.kaolinitetest.init.RecipesKaoliniteTest;

public class ProxyCommon
{
    public void preInit()
    {
        ItemsKaoliniteTest.itemsInit();
        FluidsKaoliniteTest.registerFluid();
    }

    public void init()
    {
        RecipesKaoliniteTest.recipeInit();
    }

}