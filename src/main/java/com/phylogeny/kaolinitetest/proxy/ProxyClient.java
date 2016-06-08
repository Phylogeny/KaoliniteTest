package com.phylogeny.kaolinitetest.proxy;

import com.phylogeny.kaolinitetest.init.ModelRegistration;

public class ProxyClient extends ProxyCommon
{
    @Override
    public void preInit()
    {
        super.preInit();
        ModelRegistration.registerModels();
    }

    @Override
    public void init()
    {
        super.init();
    }

}