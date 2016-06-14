package com.phylogeny.kaolinitetest.proxy;

import net.minecraftforge.common.MinecraftForge;

import com.phylogeny.kaolinitetest.init.BlocksKaoliniteTest;
import com.phylogeny.kaolinitetest.init.ModelRegistration;

public class ProxyClient extends ProxyCommon
{
    @Override
    public void preInit()
    {
        super.preInit();
        ModelRegistration.registerModels();
        MinecraftForge.EVENT_BUS.register(BlocksKaoliniteTest.cauldron_lit);
    }

    @Override
    public void init()
    {
        super.init();
    }

}