package com.phylogeny.kaolinitetest.proxy;

import com.phylogeny.kaolinitetest.init.BlocksKaoliniteTest;
import com.phylogeny.kaolinitetest.init.FluidsKaoliniteTest;
import com.phylogeny.kaolinitetest.init.ItemsKaoliniteTest;
import com.phylogeny.kaolinitetest.init.PacketRegistration;
import com.phylogeny.kaolinitetest.init.RecipeRegistration;
import com.phylogeny.kaolinitetest.tileentity.TileEntityCauldron;
import com.phylogeny.kaolinitetest.world.WorldGen;

public class ProxyCommon
{
    public void preInit()
    {
        ItemsKaoliniteTest.itemsInit();
        BlocksKaoliniteTest.registerBlocks();
        FluidsKaoliniteTest.registerFluid();
        PacketRegistration.registerPackets();
        TileEntityCauldron.register();
    }

    public void init()
    {
        RecipeRegistration.recipeInit();
        WorldGen.registerWorldGen();
    }

}