package com.phylogeny.kaolinitetest.proxy;

import net.minecraftforge.common.MinecraftForge;

import com.phylogeny.kaolinitetest.client.renderer.RenderCauldron;
import com.phylogeny.kaolinitetest.init.BlocksKaoliniteTest;
import com.phylogeny.kaolinitetest.init.ModelRegistration;
import com.phylogeny.kaolinitetest.init.SoundsKaoliniteTest;

public class ProxyClient extends ProxyCommon
{
    @Override
    public void preInit()
    {
        super.preInit();
        ModelRegistration.registerModels();
        MinecraftForge.EVENT_BUS.register(BlocksKaoliniteTest.cauldron_lit);
        RenderCauldron.register();
        SoundsKaoliniteTest.registerSounds();
    }

    @Override
    public void init()
    {
        super.init();
    }

}