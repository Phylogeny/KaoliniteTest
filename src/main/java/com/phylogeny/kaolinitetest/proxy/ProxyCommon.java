package com.phylogeny.kaolinitetest.proxy;

import com.phylogeny.kaolinitetest.init.ItemsKaoliniteTest;
import com.phylogeny.kaolinitetest.init.RecipesKaoliniteTest;

public class ProxyCommon
{
	public void preInit()
	{
		ItemsKaoliniteTest.itemsInit();
	}
	
	public void init()
	{
		RecipesKaoliniteTest.recipeInit();
	}
	
}